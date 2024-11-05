package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.BoletoJuno;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.AtributosNotificacaoPagamentoApiJunoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.DataNotificacaoPagamentoApiJunoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.NotificacaoApiJunoPagamentoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.BoletoJunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PagamentoWebHookApiJuno {

    private final BoletoJunoRepository boletoJunoRepository;

    public PagamentoWebHookApiJuno(BoletoJunoRepository boletoJunoRepository) {
        this.boletoJunoRepository = boletoJunoRepository;
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


}
