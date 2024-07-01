package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.NotaFiscalCompraRelatorioDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.NotaFiscalCompraRepository;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaFiscalCompraService {

    private final JdbcTemplate jdbcTemplate;

    public NotaFiscalCompraService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<NotaFiscalCompraRelatorioDTO> geraRelatorioNotaFiscalCompraProduto(NotaFiscalCompraRelatorioDTO notaFiscalCompraRelatorioDTO) {

        String sql = " select p.id as codigoProduto, p.nome as nomeProduto," +
                     " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada," +
                     " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra  " +
                     " from nota_fiscal_compra as cfc" +
                     " inner join nota_item_produto as ntp on  cfc.id = nota_fiscal_compra_id" +
                     " inner join produto as p on p.id = ntp.produto_id " +
                     " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id" +
                     "";

        sql = sql + " WHERE  cfc.data_compra >= '" + notaFiscalCompraRelatorioDTO.getDataInicial() + "'" +
                    " AND    cfc.data_compra <= '" + notaFiscalCompraRelatorioDTO.getDataFinal() + "'" +
                    "";

        if(notaFiscalCompraRelatorioDTO.getCodigoNota().length() != 0){
        sql = sql + " AND cfc.id = " + notaFiscalCompraRelatorioDTO.getCodigoNota() +"";
        }

        if(notaFiscalCompraRelatorioDTO.getCodigoProduto().length() != 0 ){
            sql = sql + " AND p.id = " + notaFiscalCompraRelatorioDTO.getCodigoProduto() + "";
        }

        if(notaFiscalCompraRelatorioDTO.getNomeProduto().length() != 0){
            sql = sql + " AND UPPER(p.nome) like '%" + notaFiscalCompraRelatorioDTO.getNomeProduto().toUpperCase() + "%'";
        }

        if(notaFiscalCompraRelatorioDTO.getNomeFornecedor().length() != 0){
            sql = sql + " AND UPPER(pj.nome) like '%" + notaFiscalCompraRelatorioDTO.getNomeFornecedor().toUpperCase() + "%'";
        }

        List<NotaFiscalCompraRelatorioDTO> retorno = new ArrayList<NotaFiscalCompraRelatorioDTO>();

        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NotaFiscalCompraRelatorioDTO.class));

        return retorno;


    }
}
