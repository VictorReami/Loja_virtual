package jdev.mentoria.lojaVirtual.Loja_virtual;

import Model.DTO.ObjetoErroDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.ServiceSendEmail;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.coyote.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

    private final ServiceSendEmail serviceSendEmail;

    public ControleExcecoes(ServiceSendEmail serviceSendEmail) {
        this.serviceSendEmail = serviceSendEmail;
    }

    @ExceptionHandler(ExceptionMentoriaJava.class)
    public ResponseEntity<Object> handleExcptionCustom(ExceptionMentoriaJava ex){
        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

        objetoErroDTO.setError(ex.getMessage());
        objetoErroDTO.setCode(HttpStatus.OK.toString());

        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.OK);
    }

    /*Captura exceções*/
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

        String msg = "";

        if (ex instanceof MethodArgumentNotValidException){
            List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
            for (ObjectError objectError: list){
                msg += objectError.getDefaultMessage() + "\n";
            }
        }else if (ex instanceof HttpMessageNotReadableException){
            msg = "Não está sendo enviado dados para o BODY corpo da requisição";
        }else{
            msg = ex.getMessage();
        }

        objetoErroDTO.setError(msg);
        objetoErroDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());

        ex.printStackTrace();

        try {
            serviceSendEmail.enviarEmailHtml("Erro na loja virtual", ExceptionUtils.getStackTrace(ex), "mestrezeh@gmail.com");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**Captura erro na parte de Banco de dados*/
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex){
        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

        String msg = "";

        if(ex instanceof DataIntegrityViolationException){
            msg = "Erro de integridade no banco:" + ((SQLException)  ex).getCause().getCause().getMessage();
        }if(ex instanceof ConstraintViolationException){
            msg = "Erro de chave estrangeira" + ((SQLException)  ex).getCause().getCause().getMessage();
        }if(ex instanceof SQLException){
            msg = "Erro de SQL do banco" + ((SQLException)  ex).getCause().getCause().getMessage();
        }else{
            msg = ex.getMessage();
        }

        objetoErroDTO.setError(msg);
        objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

        ex.printStackTrace();

        try {
            serviceSendEmail.enviarEmailHtml("Erro na loja virtual", ExceptionUtils.getStackTrace(ex), "mestrezeh@gmail.com");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
