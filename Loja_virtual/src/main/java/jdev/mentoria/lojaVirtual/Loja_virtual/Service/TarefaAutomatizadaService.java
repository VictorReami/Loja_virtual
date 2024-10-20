package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Usuario;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
@Service
public class TarefaAutomatizadaService {
    private final UsuarioRepository usuarioRepository;
    private final ServiceSendEmail serviceSendEmail;

    public TarefaAutomatizadaService(UsuarioRepository usuarioRepository, ServiceSendEmail serviceSendEmail) {
        this.usuarioRepository = usuarioRepository;
        this.serviceSendEmail = serviceSendEmail;
    }


    //Os Parametros do tipo CRON são: Segundo, Minuto, Hora, Dia do mês, Mês, Dia da semana
    //@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") /* Vai rodar todos os dias as 11horas da manhã no horario de são paulo */
     @Scheduled(initialDelay = 2000, fixedDelay = 8640000) /*Será executado a cada 24 horas */ //86400000
    public void notificarUsuarioTrocarSenha() throws MessagingException, UnsupportedEncodingException, InterruptedException {

         List<Usuario> usuarios = usuarioRepository.usuarioSenhvencida();

         for (Usuario usuario: usuarios){

             StringBuilder msg = new StringBuilder();
             msg.append("olá, ").append(usuario.getPessoa().getNome()).append("</br>");
             msg.append("Está na gora de trocar sua senha, já passou 90 dias de validade.").append("<br/>");
             msg.append("Troque sua senha da loja virtual do Victor - JDEV treinamentos");

             serviceSendEmail.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());

             Thread.sleep(3000);

         }

    }
}
