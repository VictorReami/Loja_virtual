package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.ApiTokenIntegracao;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.AccessTokenJunoAPI;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.BoletoJuno;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.VendaCompraLojaVirtual;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.AccessTokenJunoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.BoletoJunoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.VendaCompraLojaVirtualRepository;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class JunoBoletoService implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AccessTokenJunoService accessTokenJunoService;

    private final AccessTokenJunoRepository accessTokenJunoRepository;

    private final VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    private final BoletoJunoRepository boletoJunoRepository;

    public JunoBoletoService(AccessTokenJunoService accessTokenJunoService, AccessTokenJunoRepository accessTokenJunoRepository, VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, BoletoJunoRepository boletoJunoRepository) {
        this.accessTokenJunoService = accessTokenJunoService;
        this.accessTokenJunoRepository = accessTokenJunoRepository;
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.boletoJunoRepository = boletoJunoRepository;
    }

    public String cancelarBoleto(String code) throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenJunoAPI();

        Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
        WebResource webResource = client.resource("https://api.juno.com.br/charges/"+code+"/cancelation");

        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON)
                .header("X-Api-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .put(ClientResponse.class);

        if (clientResponse.getStatus() == 204) {

            boletoJunoRepository.deleteByCode(code);

            return "Cancelado com sucesso";
        }

        return clientResponse.getEntity(String.class);

    }

    public String gerarCarneApi(ObjetoPostCarneJunoDTO objetoPostCarneJunoDTO) throws Exception {

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(objetoPostCarneJunoDTO.getIdVenda()).get();

        CobrancaJunoApiDTO cobrancaJunoApiDTO = new CobrancaJunoApiDTO();

        cobrancaJunoApiDTO.getCharge().setPixKey(ApiTokenIntegracao.CHAVE_BOLETO_PIX);
        cobrancaJunoApiDTO.getCharge().setDescription(objetoPostCarneJunoDTO.getDescription());
        cobrancaJunoApiDTO.getCharge().setAmount(Float.valueOf(objetoPostCarneJunoDTO.getTotalAmount()));
        cobrancaJunoApiDTO.getCharge().setInstallments(Integer.parseInt(objetoPostCarneJunoDTO.getInstallments()));

        //Seta Data de vencimento para 7 dias
        Calendar dataVencimento = Calendar.getInstance();
        dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cobrancaJunoApiDTO.getCharge().setDueDate(dateFormat.format(dataVencimento.getTime()));

        cobrancaJunoApiDTO.getCharge().setFine(BigDecimal.valueOf(1.00));
        cobrancaJunoApiDTO.getCharge().setInterest(BigDecimal.valueOf(1.00));
        cobrancaJunoApiDTO.getCharge().setMaxOverdueDays(10);
        cobrancaJunoApiDTO.getCharge().getPaymentTypes().add("BOLETO_PIX");

        cobrancaJunoApiDTO.getBilling().setName(objetoPostCarneJunoDTO.getPayerName());
        cobrancaJunoApiDTO.getBilling().setDocument(objetoPostCarneJunoDTO.getPayerCpfCnpj());
        cobrancaJunoApiDTO.getBilling().setEmail(objetoPostCarneJunoDTO.getPayerEmail());
        cobrancaJunoApiDTO.getBilling().setPhone(objetoPostCarneJunoDTO.getPayerPhone());

        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenJunoAPI();

        if(accessTokenJunoAPI != null){

            Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
            WebResource webResource = client.resource("https://api.juno.com.br/pix/keys");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(cobrancaJunoApiDTO);

            ClientResponse clientResponse = webResource
                    .accept("application/json;charset=UTF-8")
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("X-API-Version", 2)
                    .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                    .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                    .post(ClientResponse.class, json);

            String retornString = clientResponse.getEntity(String.class);

            if(clientResponse.getStatus() == 200){/*Sucesso*/
                clientResponse.close();
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);/*Converte relacionamentos um para muito dentro desse JSON*/

                BoletoGeradoApiJunoDTO boletoGeradoApiJunoDTO = objectMapper.readValue(retornString, new TypeReference<BoletoGeradoApiJunoDTO>() {});

                int recorrencia = 1;

                List<BoletoJuno> boletoJunoList = new ArrayList<BoletoJuno>();

                for(ConteudoBoletoJunoDTO c : boletoGeradoApiJunoDTO.getEmbedded().getCharges()){
                    BoletoJuno boletoJuno = new BoletoJuno();

                    boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
                    boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
                    boletoJuno.setCode(c.getCode());
                    boletoJuno.setLink(c.getLink());
                    boletoJuno.setDataVencimento(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate())));
                    boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
                    boletoJuno.setValor(new BigDecimal(c.getAmount()));
                    boletoJuno.setIdChrBoleto(c.getId());
                    boletoJuno.setInstallmentLink(c.getInstallmentLink());
                    boletoJuno.setIdPix(c.getPix().getId());
                    boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
                    boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
                    boletoJuno.setRecorrencia(recorrencia);

                    boletoJunoList.add(boletoJuno);
                    recorrencia++;
                }

                boletoJunoRepository.saveAll(boletoJunoList);

                return boletoJunoList.get(0).getLink();

            }else{
                return retornString;
            }
        }else{
            return "Não existe chave de acesso para a API";
        }

    }


    public String gerarChaveBoletoPix() throws Exception {
        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenJunoAPI();

        Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
        WebResource webResource = client.resource("https://api.juno.com.br/pix/keys");


        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .post(ClientResponse.class, "{ \"type\": \"RANDOM_KEY\" }");

        //.header("X-Idempotency-Key", "chave-boleto-pix")
        return clientResponse.getEntity(String.class);




    }

    public AccessTokenJunoAPI obterTokenJunoAPI() throws Exception {
        AccessTokenJunoAPI accessTokenJunoAPI = accessTokenJunoService.buscaTokenAtivo();

        if(accessTokenJunoAPI == null || (accessTokenJunoAPI != null && accessTokenJunoAPI.expirado())){

            String clientID = "ClienteIDTESTE";
            String secretID = "secretIDTESTE";

            Client client = new HostIgnoringCliente("LinkServidorJuno?grant_type=client_credentials").hostIgnoringCliente();

            WebResource webResource = client.resource("LinkServidorJuno?grant_type=client_credentials");

            String basicChave = clientID + ":" + secretID;
            String token_autenticacao = DatatypeConverter.printBase64Binary(basicChave.getBytes());

            ClientResponse clientResponse = webResource
                    .accept(MediaType.APPLICATION_FORM_URLENCODED)
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Content-Type", "application/x-www.form-urlencoded")
                    .header("Authorization", "Basic" + token_autenticacao)
                    .post(ClientResponse.class);

            if(clientResponse.getStatus() == 200){/*Sucesso*/
                accessTokenJunoRepository.deleteAll();

                AccessTokenJunoAPI accessTokenJunoAPI1 = clientResponse.getEntity(AccessTokenJunoAPI.class);
                accessTokenJunoAPI1.setToken_acesso(token_autenticacao);

                accessTokenJunoAPI1 = accessTokenJunoRepository.save(accessTokenJunoAPI1);

                return accessTokenJunoAPI1;
            }else{
                return null;
            }
        }else{
            return accessTokenJunoAPI;
        }
    }

    public String criarWebhook(CriarWebHook criarWebHook) throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = obterTokenJunoAPI();

        Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
        WebResource webResource = client.resource("https://api.juno.com.br/notification/webhooks");

        String json = new ObjectMapper().writeValueAsString(criarWebHook);

        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .post(ClientResponse.class, json);

        String resposta = clientResponse.getEntity(String.class);
        clientResponse.close();

        return resposta;
    }

    public String listaWebHook() throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = obterTokenJunoAPI();

        Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
        WebResource webResource = client.resource("https://api.juno.com.br/notification/webhooks");

        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .get(ClientResponse.class);

        String resposta = clientResponse.getEntity(String.class);
        return resposta;

    }

    public void deleteWebHook(String idWebHook) throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = obterTokenJunoAPI();

        Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
        WebResource webResource = client.resource("https://api.juno.com.br/notification/webhooks" + idWebHook);

        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .delete(ClientResponse.class);

    }
}
