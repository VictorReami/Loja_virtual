package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.ApiTokenIntegracao;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.AccessTokenJunoAPI;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.BoletoJuno;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.AtributosNotificacaoPagamentoApiJunoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.CriarWebHook;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.DataNotificacaoPagamentoApiJunoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.NotificacaoApiJunoPagamentoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.BoletoJunoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.HostIgnoringCliente;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.JunoBoletoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PagamentoWebHookApiJuno {

    private final BoletoJunoRepository boletoJunoRepository;

    private final JunoBoletoService junoBoletoService;

    public PagamentoWebHookApiJuno(BoletoJunoRepository boletoJunoRepository, JunoBoletoService junoBoletoService) {
        this.boletoJunoRepository = boletoJunoRepository;
        this.junoBoletoService = junoBoletoService;
    }

    @ResponseBody
    @RequestMapping(value = "requisicaoJunoBoleto/notificacaoPagamentoJunoApiV2", consumes = {"application/json;charset=UTF-8"},
            headers = "Content-Type=application/json;charset=UTF-8", method = RequestMethod.POST)
    private HttpStatus notificacaoPagamentoJunoApiV2(@RequestBody NotificacaoApiJunoPagamentoDTO notificacaoApiJunoPagamentoDTO){

        for(DataNotificacaoPagamentoApiJunoDTO data : notificacaoApiJunoPagamentoDTO.getData() ){

            String codigoBoletoPix = data.getAtributos().getCharge().getCode();

            String status = data.getAtributos().getStatus();

            boolean boletoPago;
            if(status.equalsIgnoreCase("CONFIRMED") == true){
                boletoPago = true;
            }else{
                boletoPago = false;
            }
            //boolean boletoPago = status.equalsIgnoreCase("CONFIRMED") ? true : false;

            BoletoJuno boletoJuno = boletoJunoRepository.findByCode(codigoBoletoPix);

            if(!boletoJuno.isQuitado() && boletoPago ){
                boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
                System.out.println("Boleto: " + boletoJuno.getCode() + " foi quitado ");
            }
        }
        return HttpStatus.OK;
    }

    public String criarWebhook(CriarWebHook criarWebHook) throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = junoBoletoService.obterTokenJunoAPI();

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


}
