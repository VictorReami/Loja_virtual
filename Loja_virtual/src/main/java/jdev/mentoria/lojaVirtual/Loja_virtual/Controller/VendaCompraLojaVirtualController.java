package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.StatusContaReceber;
import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.ItemVendaLojaDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.VendaCompraLojaVirtualDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.ServiceSendEmail;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.VendaCompraLojaVirtualService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class VendaCompraLojaVirtualController {

    private final VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
    private final EnderecoRepository enderecoRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final NotaFiscalVendaRepository notaFiscalVendaRepository;
    private final PessoaController pessoaController;
    private final StatusRastreioRepository statusRastreioRepository;
    private final VendaCompraLojaVirtualService vendaCompraLojaVirtualService;
    private final ContaReceberRepository contaReceberRepository;
    private final ServiceSendEmail serviceSendEmail;

    public VendaCompraLojaVirtualController(VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, EnderecoRepository enderecoRepository, PessoaFisicaRepository pessoaFisicaRepository, NotaFiscalVendaRepository notaFiscalVendaRepository, PessoaController pessoaController, StatusRastreioRepository statusRastreioRepository, VendaCompraLojaVirtualService vendaCompraLojaVirtualService, ContaReceberRepository contaReceberRepository, ServiceSendEmail serviceSendEmail) {
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.enderecoRepository = enderecoRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.notaFiscalVendaRepository = notaFiscalVendaRepository;
        this.pessoaController = pessoaController;
        this.statusRastreioRepository = statusRastreioRepository;
        this.vendaCompraLojaVirtualService = vendaCompraLojaVirtualService;
        this.contaReceberRepository = contaReceberRepository;
        this.serviceSendEmail = serviceSendEmail;
    }

    @ResponseBody
    @PostMapping(value = "/salvarCompraVendaLoja")
    public ResponseEntity<VendaCompraLojaVirtualDTO> salvarCompraVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionMentoriaJava, MessagingException, UnsupportedEncodingException {

        vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        PessoaFisica pessoaFisica = pessoaController.salvarCpf(vendaCompraLojaVirtual.getPessoa()).getBody();
        vendaCompraLojaVirtual.setPessoa(pessoaFisica);

        vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

        vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

        vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

        for(int i = 0; i < vendaCompraLojaVirtual.getItemVendaLoja().size(); i++){

            vendaCompraLojaVirtual.getItemVendaLoja().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            vendaCompraLojaVirtual.getItemVendaLoja().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

        }

        /*Persiste novamente as nota fiscal novamente pra ficar amarrada na venda*/
        notaFiscalVendaRepository.save(vendaCompraLojaVirtual.getNotaFiscalVenda());

        /*Salva primeiro a venda e todo os dados*/
        vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.save(vendaCompraLojaVirtual);

        /*Associa a venda gravada no banco com a nota fiscal*/
        vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

        StatusRastreio statusRastreio = new StatusRastreio();

        statusRastreio.setCentroDestribuicao("Joja local");
        statusRastreio.setCidade("Americana");
        statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        statusRastreio.setEstado("SP");
        statusRastreio.setStatus("Inicio Compra");
        statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        statusRastreioRepository.save(statusRastreio);

        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

        vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
        vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
        vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

        for(ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLoja()){

            ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

            itemVendaLojaDTO.setProduto(item.getProduto());
            itemVendaLojaDTO.setQuantidade(item.getQuantidade());

            vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
        }

        ContaReceber contaReceber = new ContaReceber();
        contaReceber.setDescricao("Venda da loja virtual nº: " + vendaCompraLojaVirtual.getId());
        contaReceber.setDtPagamento(Calendar.getInstance().getTime());
        contaReceber.setDtVencimento(Calendar.getInstance().getTime());
        contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        contaReceber.setPessoa(vendaCompraLojaVirtual.getPessoa());
        contaReceber.setStatus(StatusContaReceber.QUITADA);
        contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        contaReceberRepository.save(contaReceber);

        /*Emil para o comprador*/
        StringBuilder msgemail = new StringBuilder();
        msgemail.append("Olá, ").append(pessoaFisica.getNome()).append("</br>");
        msgemail.append("Você realizou a compra de nº: ").append(vendaCompraLojaVirtual.getId()).append("</br>");
        msgemail.append("Na loja ").append(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());
        /*assunto, msg, destino*/
        serviceSendEmail.enviarEmailHtml("Compra Realizada", msgemail.toString(), pessoaFisica.getEmail());

        /*Email para o vendedor*/
        msgemail = new StringBuilder();
        msgemail.append("Você realizou uma venda, nº " ).append(vendaCompraLojaVirtual.getId());
        serviceSendEmail.enviarEmailHtml("Venda Realizada", msgemail.toString(), vendaCompraLojaVirtual.getEmpresa().getEmail());

        return new ResponseEntity<VendaCompraLojaVirtualDTO>( vendaCompraLojaVirtualDTO, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/buscaCompraVendaLoja/{id}")
    public ResponseEntity<VendaCompraLojaVirtualDTO> buscaCompraVendaLoja (@PathVariable("id") Long id){

        VendaCompraLojaVirtual vendaCompraLojaVirtual  = vendaCompraLojaVirtualRepository.findByIdExclusao(id);

        if(vendaCompraLojaVirtual == null){
            vendaCompraLojaVirtual = new VendaCompraLojaVirtual();
        }

        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

        vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
        vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
        vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

        for(ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLoja()){

            ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

            itemVendaLojaDTO.setProduto(item.getProduto());
            itemVendaLojaDTO.setQuantidade(item.getQuantidade());

            vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
        }

        return new ResponseEntity<VendaCompraLojaVirtualDTO>( vendaCompraLojaVirtualDTO, HttpStatus.OK);
    }


    @ResponseBody
    @DeleteMapping(value = "/deleteCompraVenda/{idVenda}")
    public ResponseEntity<String> deleteCompraVenda(@PathVariable("idVenda") Long idVenda){

        vendaCompraLojaVirtualService.exclusaoTotalCompraVendaBanco(idVenda);

        return new ResponseEntity<String>("CompraVenda deletada com sucesso.", HttpStatus.OK);

    }

    @ResponseBody
    @DeleteMapping(value = "/deleteCompraVenda2/{idVenda}")
    public ResponseEntity<String> deleteCompraVenda2(@PathVariable("idVenda") Long idVenda){

        vendaCompraLojaVirtualService.exclusaoTotalCompraVendaBanco2(idVenda);

        return new ResponseEntity<String>("CompraVenda deletada com sucesso Logicamente.", HttpStatus.OK);

    }

    @ResponseBody
    @PutMapping(value = "/ativaCompraVenda/{idVenda}")
    public ResponseEntity<String> ativaCompraVenda(@PathVariable("idVenda") Long idVenda){

        vendaCompraLojaVirtualService.ativaRegistroCompraVendaBanco(idVenda);

        return new ResponseEntity<String>("CompraVenda Ativada com sucesso Logicamente.", HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/buscaCompraVendaPorID/{id}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscaCompraVendaPorID (@PathVariable("id") Long id){

        List<VendaCompraLojaVirtual> vendaCompraLojaVirtual  = vendaCompraLojaVirtualRepository.vendaPorProduto(id);

        if(vendaCompraLojaVirtual == null){
            vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : vendaCompraLojaVirtual) {


            VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            vendaCompraLojaVirtualDTO.setId(vcl.getId());
            vendaCompraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
            vendaCompraLojaVirtualDTO.setPessoa(vcl.getPessoa());
            vendaCompraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            vendaCompraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());
            vendaCompraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
            vendaCompraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());

            for (ItemVendaLoja item : vcl.getItemVendaLoja()) {

                ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

                itemVendaLojaDTO.setProduto(item.getProduto());
                itemVendaLojaDTO.setQuantidade(item.getQuantidade());

                vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
            }
            vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
        }

        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>( vendaCompraLojaVirtualDTOList, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscaCompraVendaPorCliente/{idCliente}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscaCompraVendaPorCliente (@PathVariable("idCliente") Long idCliente){

        List<VendaCompraLojaVirtual> vendaCompraLojaVirtual  = vendaCompraLojaVirtualRepository.vendaPorCliente(idCliente);

        if(vendaCompraLojaVirtual == null){
            vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : vendaCompraLojaVirtual) {


            VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            vendaCompraLojaVirtualDTO.setId(vcl.getId());
            vendaCompraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
            vendaCompraLojaVirtualDTO.setPessoa(vcl.getPessoa());
            vendaCompraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            vendaCompraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());
            vendaCompraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
            vendaCompraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());

            for (ItemVendaLoja item : vcl.getItemVendaLoja()) {

                ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

                itemVendaLojaDTO.setProduto(item.getProduto());
                itemVendaLojaDTO.setQuantidade(item.getQuantidade());

                vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
            }
            vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
        }

        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>( vendaCompraLojaVirtualDTOList, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscaCompraVendaDinamica/{valor}/{tipoConsulta}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscaCompraVendaDinamica (@PathVariable("valor") String valor,
                                                                                     @PathVariable("tipoConsulta") String tipoConsulta){

        List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = null;

        if(tipoConsulta.equalsIgnoreCase("POR_ID_PROD")){
            vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(Long.parseLong(valor));
        }else if (tipoConsulta.equalsIgnoreCase("POR_NOME_PROD")){
            vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeProduto(valor.toUpperCase().trim());
        }else if (tipoConsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
            vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeCliente(valor.toUpperCase().trim());
        }else if (tipoConsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {
            vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEndereCobranca(valor.toUpperCase().trim());
        }else if (tipoConsulta.equalsIgnoreCase("POR_ENDERECO_ENTREGA")) {
            vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEnderecoEntrega(valor.toUpperCase().trim());
        }
        
        if(vendaCompraLojaVirtual == null){
            vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : vendaCompraLojaVirtual) {


            VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            vendaCompraLojaVirtualDTO.setId(vcl.getId());
            vendaCompraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
            vendaCompraLojaVirtualDTO.setPessoa(vcl.getPessoa());
            vendaCompraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            vendaCompraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());
            vendaCompraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
            vendaCompraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());

            for (ItemVendaLoja item : vcl.getItemVendaLoja()) {

                ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

                itemVendaLojaDTO.setProduto(item.getProduto());
                itemVendaLojaDTO.setQuantidade(item.getQuantidade());

                vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
            }
            vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
        }

        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>( vendaCompraLojaVirtualDTOList, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscaCompraVendaDinamicaFaixaData/{data1}/{data2}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscaCompraVendaDinamicaFaixaData (@PathVariable("data1") String data1,
                                                                                              @PathVariable("data2") String data2) throws ParseException {

        List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = null;

        vendaCompraLojaVirtual = vendaCompraLojaVirtualService.consultaVendaFaixaData(data1, data2);

        if(vendaCompraLojaVirtual == null){
            vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : vendaCompraLojaVirtual) {

            VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            vendaCompraLojaVirtualDTO.setId(vcl.getId());
            vendaCompraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
            vendaCompraLojaVirtualDTO.setPessoa(vcl.getPessoa());
            vendaCompraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            vendaCompraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());
            vendaCompraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
            vendaCompraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());

            for (ItemVendaLoja item : vcl.getItemVendaLoja()) {

                ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

                itemVendaLojaDTO.setProduto(item.getProduto());
                itemVendaLojaDTO.setQuantidade(item.getQuantidade());

                vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
            }
            vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
        }

        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>( vendaCompraLojaVirtualDTOList, HttpStatus.OK);
    }


    }
