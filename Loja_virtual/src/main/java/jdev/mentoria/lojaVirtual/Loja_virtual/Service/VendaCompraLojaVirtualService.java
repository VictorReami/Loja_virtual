package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendaCompraLojaVirtualService {


    private final JdbcTemplate jdbcTemplate;

    public VendaCompraLojaVirtualService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void exclusaoTotalCompraVendaBanco2(Long idVenda){
        String value =  "BEGIN;" +
                        "   UPDATE vd_cp_loja_virt set excluido = true" +
                        "   WHERE id = " + idVenda + ";" +
                        "   Commit;" +
                        " END; ";
        jdbcTemplate.execute(value);
    }

    public void ativaRegistroCompraVendaBanco(Long idVenda){
        String value =  "BEGIN;" +
                "   UPDATE vd_cp_loja_virt set excluido = false" +
                "   WHERE id = " + idVenda + ";" +
                "   Commit;" +
                " END; ";
        jdbcTemplate.execute(value);
    }



    public void exclusaoTotalCompraVendaBanco(Long idVenda) {

        String value =
                " BEGIN;"
                        + " UPDATE nota_fiscal_venda set venda_compra_loja_virt_id = null where venda_compra_loja_virt_id = "+idVenda+"; "
                        + " delete from nota_fiscal_venda where venda_compra_loja_virt_id = "+idVenda+"; "
                        + " delete from item_venda_loja where venda_compra_loja_virt_id = "+idVenda+"; "
                        + " delete from status_rastreio where venda_compra_loja_virt_id = "+idVenda+"; "
                        + " delete from vd_cp_loja_virt where id = "+idVenda+"; "
                        + " commit; " +
                " END;";

        jdbcTemplate.execute(value);
    }







}
