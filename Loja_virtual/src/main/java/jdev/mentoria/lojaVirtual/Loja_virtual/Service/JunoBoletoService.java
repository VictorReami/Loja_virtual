package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.AccessTokenJunoAPI;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.AccessTokenJunoRepository;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;

@Service
public class JunoBoletoService implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AccessTokenJunoService accessTokenJunoService;

    private final AccessTokenJunoRepository accessTokenJunoRepository;

    public JunoBoletoService(AccessTokenJunoService accessTokenJunoService, AccessTokenJunoRepository accessTokenJunoRepository) {
        this.accessTokenJunoService = accessTokenJunoService;
        this.accessTokenJunoRepository = accessTokenJunoRepository;
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
}
