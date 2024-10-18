package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "access_token_junoapi")
@SequenceGenerator(name = "seq_access_token_junoapi", sequenceName = "seq_access_token_junoapi", initialValue = 1, allocationSize = 1)
public class AccessTokenJunoAPI implements Serializable {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_access_token_junoapi")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String access_token;

    private String token_type;

    private String expires_in;

    private String scope;

    private String user_name;

    private String jti;

    private String token_acesso;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro = Calendar.getInstance().getTime();

    public boolean expirado(){
        Date dataAtual = Calendar.getInstance().getTime();

        Long tempo = dataAtual.getTime() - dataCadastro.getTime(); /*Tempo entre as datas*/

        Long minutos = tempo / (1000 * 60); /*diferença de minutos entre as dastas*/

        if(minutos.intValue() > 50){
            return true;
        }else{
            return false;
        }
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessTokenJunoAPI that = (AccessTokenJunoAPI) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getAccess_token(), that.getAccess_token()) && Objects.equals(getToken_type(), that.getToken_type()) && Objects.equals(getExpires_in(), that.getExpires_in()) && Objects.equals(getScope(), that.getScope()) && Objects.equals(getUser_name(), that.getUser_name()) && Objects.equals(getJti(), that.getJti()) && Objects.equals(getToken_acesso(), that.getToken_acesso()) && Objects.equals(getDataCadastro(), that.getDataCadastro());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAccess_token(), getToken_type(), getExpires_in(), getScope(), getUser_name(), getJti(), getToken_acesso(), getDataCadastro());
    }
}
