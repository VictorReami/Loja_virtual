package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.AccessTokenJunoAPI;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Service
public class AccessTokenJunoService {

    @PersistenceContext
    private EntityManager entityManager;

    public AccessTokenJunoAPI buscaTokenAtivo() {
        AccessTokenJunoAPI accessTokenJunoAPI;
        try {
            accessTokenJunoAPI = (AccessTokenJunoAPI)
                    entityManager.createQuery("SELECT a FROM AccessTokenJunoAPI a")
                            .setMaxResults(1)
                            .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return accessTokenJunoAPI;
    }
}
