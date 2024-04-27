package jdev.mentoria.lojaVirtual.Loja_virtual.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*Filtro onde todas as requisições serão capturadas para autenticar*/
public class JwtApiAutenticacaoFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*Estabelece a autenticação do user*/

        Authentication authentication = new JWTTokenAutenticacaoService()
                .getAuthentication((HttpServletRequest)request, (HttpServletResponse) response);

        /*Coloca o processo de autenticacao para o spring security*/
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);

    }
}
