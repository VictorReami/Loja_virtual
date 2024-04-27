package jdev.mentoria.lojaVirtual.Loja_virtual.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.servlet.http.HttpServletResponse;
import jdev.mentoria.lojaVirtual.Loja_virtual.ApplicationContextLoad;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Usuario;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
                     .signWith(SignatureAlgorithm.HS512, SECRET) /*tipo de criptografia + chave secreta*/
                     .compact(); /*Compila tudo e compacta em uma String*/


        String token = TOKEN_PREFIX + " " + JWT;

        liberacaoCors(response);

        /*Da a resposta para a tela e para o cliente, outra API, Navegador, Aplicativo, Qualquer dispositivo que fizer a chamada.*/
        response.addHeader(HEADER_STRING, token);

        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }

    /*Retorna o usuário validado com token ou caso não seja valido retorn null*/
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response){

        String token = request.getHeader(HEADER_STRING);

        if(token != null){
            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

            /*faz a validação do token do usuário na requisição e obtem o USER*/
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(tokenLimpo)
                    .getBody()
                    .getSubject(); /*ADM ou Victor*/

            if(user != null){
                Usuario usuario = ApplicationContextLoad
                        .getApplicationContext()
                        .getBean(UsuarioRepository.class)
                        .findUserByLogin(user);

                if(usuario != null){
                    return new UsernamePasswordAuthenticationToken(usuario.getLogin()
                                                                  ,usuario.getSenha()
                                                                  ,usuario.getAuthorities());
                }

            }
        }

        liberacaoCors(response);
        return null;

    }

    /*Fazendo liberação contra erro de Cors no navegador*/
    private void liberacaoCors(HttpServletResponse response){
        if (response.getHeader("Access-Control-Allow-Origin") == null){
            response.addHeader("Access-Control-Allow-Origin", "*");
        }

        if (response.getHeader("Access-Control-Allow-Headers") == null){
            response.addHeader("Access-Control-Allow-Headers", "*");
        }

        if (response.getHeader("Access-Control-Request-Headers") == null){
            response.addHeader("Access-Control-Request-Headers", "*");
        }

        if (response.getHeader("Access-Control-Allow-Methods") == null){
            response.addHeader("Access-Control-Allow-Methods", "*");
        }

    }
}
