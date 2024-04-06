package jdev.mentoria.lojaVirtual.Loja_virtual.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/*Criar a autenticação e reotornar tambem a autenticação JWT */
@Service
@Component
public class JWTTokenAutenticacaoService {

    /**token de validade de 11 dias */
    private static final long EXPIRATION_TIME = 959990000;

    /*Chave de senha para ajustar com o JWT*/
    private static final String SECRET = "asdasdasdasdasd-asd5165";

    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    /*Gerar o token e da a resposta para o cliente com o JWT*/
    public void addAuthentication(HttpServletResponse response, String username) throws Exception{

        /*Montagem do Token*/
        String JWT = Jwts.builder() /*Chama o gerador de Token*/
                     .setSubject(username)  /*Adiciona o user*/
                     .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /*Temp de expiração*/
                     .signWith(SignatureAlgorithm.ES512, SECRET) /*tipo de criptografia + chave secreta*/
                     .compact(); /*Compila tudo e compacta em uma String*/


        String token = TOKEN_PREFIX + " " + JWT;

        /*Da a resposta para a tela e para o cliente, outra API, Navegador, Aplicativo, Qualquer dispositivo que fizer a chamada.*/
        response.addHeader(HEADER_STRING, token);

        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }









}
