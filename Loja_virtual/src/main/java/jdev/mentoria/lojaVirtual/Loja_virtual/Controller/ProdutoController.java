package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Produto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.ProdutoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.ServiceSendEmail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final ServiceSendEmail serviceSendEmail;

    public ProdutoController(ProdutoRepository produtoRepository, ServiceSendEmail serviceSendEmail) {
        this.produtoRepository = produtoRepository;
        this.serviceSendEmail = serviceSendEmail;
    }

    @ResponseBody
    @PostMapping("/salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionMentoriaJava, MessagingException, IOException {

        if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {
            throw new ExceptionMentoriaJava("Tipo da unidade deve ser informada.");
        }
        if (produto.getNome().length() < 10) {
            throw new ExceptionMentoriaJava("O nome do produto deve ter mais de 10 letras.");
        }

        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionMentoriaJava("Empresa responsável deve ser informada.");
        }

        if (produto.getId() == null) {
            List<Produto> produtoList  = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());

            if (produtoList.size() != 0) {
                throw new ExceptionMentoriaJava("Já existe Produto com a descrição: " + produto.getNome());
            }
        }

        if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionMentoriaJava("Categoria deve ser informada.");
        }

        if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionMentoriaJava("Marca deve ser informada.");
        }

        if(produto.getQtdeEstoque() < 1 ){
            throw new ExceptionMentoriaJava("O produto deve ter no minímo 1 item no estoque.");
        }

        if(produto.getImagemProduto() == null || produto.getImagemProduto().isEmpty() == true || produto.getImagemProduto().size() == 0 ){
            throw new ExceptionMentoriaJava("Deve ser informado imagens para o produto.");
        }

        if(produto.getImagemProduto().size() < 1){
            throw new ExceptionMentoriaJava("Deve ser informado pelo menos 3 imagens para o produto.");
        }

        if(produto.getImagemProduto().size() > 6){
            throw new ExceptionMentoriaJava("Deve ser informado no máximo 6 imagens.");
        }

        if(produto.getId() == null){
            for(int i = 0; i < produto.getImagemProduto().size(); i++){
                produto.getImagemProduto().get(i).setProduto(produto);
                produto.getImagemProduto().get(i).setEmpresa(produto.getEmpresa());

                String base64Image = "";

                //Verifica se no texto da imagem possui "data:image"
                if(produto.getImagemProduto().get(i).getImagemOriginal().contains("data:image")){
                    //Busca informações depois da primeira virgula
                    base64Image = produto.getImagemProduto().get(i).getImagemOriginal().split(",")[1];
                }else{
                    base64Image = produto.getImagemProduto().get(i).getImagemOriginal();
                }

                //Converte a imagem em Array Byte
                byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

                //Joga o Array Byte para um Buffer, para armazenar
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

                if(bufferedImage != null){
                    //Seta os tamanhos da Imagem
                                                          //IF                          //ELSE
                    int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
                    int largura = Integer.parseInt("800");
                    int altura = Integer.parseInt("600");

                    //Redimenciona a imagem original para 800x600
                    BufferedImage resizedImage = new BufferedImage(largura, altura, type);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bufferedImage, 0, 0, largura, altura, null);
                    g.dispose();

                    //Trata os Bytes para exportar para uma nova variavel
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, "png", baos);

                    //Converte os bytes para a variavel de Imagem menor
                    String miniImage64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());

                    //Atribui o a imagem menor para a o parametro produto
                    produto.getImagemProduto().get(i).setImagemMiniatura(miniImage64);

                    bufferedImage.flush();
                    resizedImage.flush();
                    baos.flush();
                    baos.close();
                }
            }
        }

        Produto produtoSalvo = produtoRepository.save(produto);

        if(produto.getAlertaQtdeEstoque()  == true && produto.getQtdeEstoque() <= 1 ){

            StringBuilder html = new StringBuilder();
            html.append("<h2>").append("Produto: " + produto.getNome()).append(" com estoque baixo: " + produto.getQtdeEstoque());
            html.append("<p> Id Prod.:").append(produto.getId()).append("<p>");

            if(produto.getEmpresa().getEmail() != null){
                serviceSendEmail.enviarEmailHtml("Produto sem estoque", html.toString(), produto.getEmpresa().getEmail());
            }
        }

        return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/deleteProduto")
    public ResponseEntity<String> deleteProduto(@RequestBody Produto produto){
        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity<String>("Produto Removido.", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteProdutoPorId/{id}")
    public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) {

        produtoRepository.deleteById(id);

        return new ResponseEntity<String>("Produto Removido.",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterProduto/{id}")
    public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if(produto == null){
            throw new ExceptionMentoriaJava("Não encontrou o produto com o código: " + id);
        }

        return new ResponseEntity<Produto>(produto,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarProdutoNome/{desc}")
    public ResponseEntity<List<Produto>> buscarProdutoNome(@PathVariable("desc") String desc) {

        List<Produto> produtos = produtoRepository.buscarProdutoNome(desc.toUpperCase());

        return new ResponseEntity<List<Produto>>(produtos,HttpStatus.OK);
    }
}
