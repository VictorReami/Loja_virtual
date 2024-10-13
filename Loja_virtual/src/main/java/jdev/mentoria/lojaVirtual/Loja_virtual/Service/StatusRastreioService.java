package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.StatusRastreioRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatusRastreioService {

    private final StatusRastreioRepository statusRastreioRepository;

    private final JdbcTemplate jdbcTemplate;

    public StatusRastreioService(StatusRastreioRepository statusRastreioRepository, JdbcTemplate jdbcTemplate) {
        this.statusRastreioRepository = statusRastreioRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void salvaUrlRastreio(String urlRastreio, Long idVenda) {

        String value =
                " BEGIN;"
                    + " UPDATE status_rastreio SET url_rastreio = '" +urlRastreio+ "' WHERE venda_compra_loja_virt_id = " +idVenda+"; "
                    + " commit; " +
                " END;";
        jdbcTemplate.execute(value);
    }



}
