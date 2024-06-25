package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.VendaCompraLojaVirtual;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.VendaCompraLojaVirtualRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class VendaCompraLojaVirtualService {


    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    private final VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    public VendaCompraLojaVirtualService(JdbcTemplate jdbcTemplate, EntityManager entityManager, VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
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

    /*HQL (Hibernate) ou JPQL (JPA ou Spring Data)*/
    @SuppressWarnings("unchecked")
    public List<VendaCompraLojaVirtual> consultaVendaFaixaData(String data1, String data2) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = dateFormat.parse(data1);
        Date date2 = dateFormat.parse(data2);

        return vendaCompraLojaVirtualRepository.consultaVendaFaixaData(date1, date2);

        /*String sql = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
                + " where i.vendaCompraLojaVirtual.excluido = false "
                + " and i.vendaCompraLojaVirtual.dataVenda >= '" + data1 + "'"
                + " and i.vendaCompraLojaVirtual.dataVenda <= '" + data2 + "'";

        return entityManager.createQuery(sql).getResultList();*/
    }







}
