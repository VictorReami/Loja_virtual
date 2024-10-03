package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.ApiTokenIntegracao;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.StatusContaReceber;
import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.ServiceSendEmail;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.VendaCompraLojaVirtualService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;

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
    @GetMapping(value = "/cancelaEtiqueta/{idEtiqueta}/{descricao}")
    public ResponseEntity<String> cancelaEtiqueta(@PathVariable String idEtiqueta, @PathVariable String reason_id, @PathVariable String descricao) throws IOException{

        OkHttpClient client = new OkHttpClient().newBuilder() .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\n    \"order\": {\n        \"id\": \""+idEtiqueta+"\",\n        \"reason_id\": \""+reason_id+"\",\n        \"description\": \""+descricao+"\"\n    }\n}");
        okhttp3.Request request = new Request.Builder()
                .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX+"api/v2/me/shipment/cancel")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
                .addHeader("User-Agent", "suporte@jdevtreinamento.com.br")
                .build();

        okhttp3.Response response = client.newCall(request).execute();

        return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
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

    @ResponseBody
    @PostMapping(value = "/consultaFreteLojaVirtual")
    public ResponseEntity<List<EmpresaTransporteDTO>> consultaFrete (@RequestBody @Valid ConsultaFreteDTO consultaFreteDTO) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(consultaFreteDTO);

        OkHttpClient client = new OkHttpClient().newBuilder() .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX +"api/v2/me/shipment/calculate")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
                .addHeader("User-Agent", "suporte@jdevtreinamento.com.br")
                .build();

        okhttp3.Response response = client.newCall(request).execute();

        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());

        Iterator<JsonNode> iterator = jsonNode.iterator();

        List<EmpresaTransporteDTO> empresaTransporteDTOs = new ArrayList<EmpresaTransporteDTO>();

        while(iterator.hasNext()) {
            JsonNode node = iterator.next();

            EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();

            if (node.get("id") != null) {
                empresaTransporteDTO.setId(node.get("id").asText());
            }

            if (node.get("name") != null) {
                empresaTransporteDTO.setNome(node.get("name").asText());
            }

            if (node.get("price") != null) {
                empresaTransporteDTO.setValor(node.get("price").asText());
            }

            if (node.get("company") != null) {
                empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
                empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
            }

            if (empresaTransporteDTO.dadosOK()) {
                empresaTransporteDTOs.add(empresaTransporteDTO);
            }
        }

        return new ResponseEntity<List<EmpresaTransporteDTO>>(empresaTransporteDTOs, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/imprimeCompraEtiquetaFrete/{idVenda}")
    public ResponseEntity<String> imprimeCompraEtiquetaFrete(@PathVariable long idVenda) throws ExceptionMentoriaJava, IOException {

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).orElseGet(null);

        if(vendaCompraLojaVirtual == null){
            return new ResponseEntity<>("Venda não encontrada.", HttpStatus.OK);
        }

        EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();

        envioEtiquetaDTO.setService((vendaCompraLojaVirtual.getServicoTransportadora()));
        envioEtiquetaDTO.setAgency("49");
        envioEtiquetaDTO.getFrom().setName(vendaCompraLojaVirtual.getEmpresa().getNome());
        envioEtiquetaDTO.getFrom().setPhone(vendaCompraLojaVirtual.getEmpresa().getTelefone());
        envioEtiquetaDTO.getFrom().setEmail(vendaCompraLojaVirtual.getEmpresa().getEmail());
        envioEtiquetaDTO.getFrom().setCompany_document(vendaCompraLojaVirtual.getEmpresa().getCnpj());
        envioEtiquetaDTO.getFrom().setState_register(vendaCompraLojaVirtual.getEmpresa().getInscEstadual());
        envioEtiquetaDTO.getFrom().setAddress(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getRuaLogradouro());
        envioEtiquetaDTO.getFrom().setComplement(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
        envioEtiquetaDTO.getFrom().setNumber(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
        envioEtiquetaDTO.getFrom().setDistrict(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getEstado());
        envioEtiquetaDTO.getFrom().setCity(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
        envioEtiquetaDTO.getFrom().setCountry_id(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());
        envioEtiquetaDTO.getFrom().setPostal_code(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
        envioEtiquetaDTO.getFrom().setNote("Não há");


        envioEtiquetaDTO.getTo().setName(vendaCompraLojaVirtual.getPessoa().getNome());
        envioEtiquetaDTO.getTo().setPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());
        envioEtiquetaDTO.getTo().setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        envioEtiquetaDTO.getTo().setDocument(vendaCompraLojaVirtual.getPessoa().getCpf());
        envioEtiquetaDTO.getTo().setAddress(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getRuaLogradouro());
        envioEtiquetaDTO.getTo().setComplement(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getComplemento());
        envioEtiquetaDTO.getTo().setNumber(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getNumero());
        envioEtiquetaDTO.getTo().setDistrict(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
        envioEtiquetaDTO.getTo().setCity(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getCidade());
        envioEtiquetaDTO.getTo().setState_abbr(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
        envioEtiquetaDTO.getTo().setCountry_id(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getUf());
        envioEtiquetaDTO.getTo().setPostal_code(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getCep());
        envioEtiquetaDTO.getTo().setNote("Não há");

        List<ProductsEnvioEtiquetaDTO> proutos = new ArrayList<ProductsEnvioEtiquetaDTO>();

        for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItemVendaLoja()){

            ProductsEnvioEtiquetaDTO dto = new ProductsEnvioEtiquetaDTO();

            dto.setName(itemVendaLoja.getProduto().getNome());
            dto.setQuantity(itemVendaLoja.getQuantidade().toString());
            dto.setUnitary_value("" + itemVendaLoja.getProduto().getValorVenda().doubleValue());

            proutos.add(dto);
        }

        envioEtiquetaDTO.setProducts(proutos);


        List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();

        for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItemVendaLoja()) {

            VolumesEnvioEtiquetaDTO dto = new VolumesEnvioEtiquetaDTO();

            dto.setHeight(itemVendaLoja.getProduto().getAltura().toString());
            dto.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
            dto.setWeight(itemVendaLoja.getProduto().getPeso().toString());
            dto.setWidth(itemVendaLoja.getProduto().getLargura().toString());

            volumes.add(dto);
        }

        envioEtiquetaDTO.setVolumes(volumes);

        envioEtiquetaDTO.getOptions().setInsurance_value("" + vendaCompraLojaVirtual.getValorTotal().doubleValue());
        envioEtiquetaDTO.getOptions().setReceipt(false);
        envioEtiquetaDTO.getOptions().setOwn_hand(false);
        envioEtiquetaDTO.getOptions().setReverse(false);
        envioEtiquetaDTO.getOptions().setNon_commercial(false);
        envioEtiquetaDTO.getOptions().getInvoice().setKey(vendaCompraLojaVirtual.getNotaFiscalVenda().getNumero());
        envioEtiquetaDTO.getOptions().setPlatform(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());

        TagsEnvioDto dtoTagEnvio = new TagsEnvioDto();
        dtoTagEnvio.setTag("Identificação do pedido na plataforma, exemplo:" + vendaCompraLojaVirtual.getId());
        dtoTagEnvio.setUrl(null);
        envioEtiquetaDTO.getOptions().getTags().add(dtoTagEnvio);

        String jsonEnvio = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvio);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/cart")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
                .addHeader("User-Agent", "mestrezeh@gmail.com.br")
                .build();

        okhttp3.Response response = client.newCall(request).execute();

        String respostaJson = response.body().string();

        if(respostaJson.contains("error")){
            throw new ExceptionMentoriaJava(respostaJson);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(respostaJson);

        Iterator<JsonNode> iterator = jsonNode.iterator();

        String idEtiqueta = "";

        while(iterator.hasNext()) {
            JsonNode node = iterator.next();
            if(node.get("id") != null){
                idEtiqueta = node.get("id").asText();
            }else{
                idEtiqueta = node.asText();
            }
            break;
        }

        /*Salvando o código da etiqueta*/
        vendaCompraLojaVirtualRepository.updateEtiqueta(idEtiqueta, vendaCompraLojaVirtual.getId());

        OkHttpClient clientCompra = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaTypeC =  okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyC =  okhttp3.RequestBody.create(mediaTypeC, "{\n    \"orders\": [\n        \""+idEtiqueta+"\"\n    ]\n}");
        okhttp3.Request requestC = new  okhttp3.Request.Builder()
                .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX  + "api/v2/me/shipment/checkout")
                .method("POST", bodyC)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
                .addHeader("User-Agent", "mestrezeh@gmail.com.br")
                .build();

        okhttp3.Response responseC = clientCompra.newCall(requestC).execute();

        if (!responseC.isSuccessful()) {
            return new ResponseEntity<String>("Não foi possível realizar a compra da etiqueta", HttpStatus.OK);
        }

        OkHttpClient clientGe = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaTypeGe =  okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyGe =  okhttp3.RequestBody.create(mediaTypeGe, "{\n    \"orders\":[\n        \""+idEtiqueta+"\"\n    ]\n}");
        okhttp3.Request requestGe = new  okhttp3.Request.Builder()
                .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX  + "api/v2/me/shipment/generate")
                .method("POST", bodyGe)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " +  ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
                .addHeader("User-Agent", "mestrezeh@gmail.com.br")
                .build();

        okhttp3.Response responseGe = clientGe.newCall(requestGe).execute();

        if (!responseGe.isSuccessful()) {
            return new ResponseEntity<String>("Não foi possível gerar a etiqueta", HttpStatus.OK);
        }

        /*Faz impresão das etiquetas*/

        OkHttpClient clientIm = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaTypeIm = MediaType.parse("application/json");
        okhttp3.RequestBody bodyIm = okhttp3.RequestBody.create(mediaTypeIm, "{\n    \"mode\": \"private\",\n    \"orders\": [\n        \""+idEtiqueta+"\"\n    ]\n}");
        okhttp3.Request requestIm = new Request.Builder()
                .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX  + "api/v2/me/shipment/print")
                .method("POST", bodyIm)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
                .addHeader("User-Agent", "mestrezeh@gmail.com.br")
                .build();

        okhttp3.Response responseIm = clientIm.newCall(requestIm).execute();


        if (!responseIm.isSuccessful()) {
            return new ResponseEntity<String>("Não foi imprimir a etiqueta.", HttpStatus.OK);
        }

        String urlEtiqueta = responseIm.body().string();

        vendaCompraLojaVirtualRepository.updateURLEtiqueta(urlEtiqueta, vendaCompraLojaVirtual.getId());

        return new ResponseEntity<>("sucesso", HttpStatus.OK);

    }


}
