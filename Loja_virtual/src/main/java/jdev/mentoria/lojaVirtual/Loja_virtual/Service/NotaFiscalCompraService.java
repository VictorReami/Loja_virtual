package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.NotaFiscalCompraRelatorioDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.RelatorioStatusCompra;
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

    public List<RelatorioStatusCompra> relatorioStatusVendaLojaVirtual(RelatorioStatusCompra relatorioStatusCompra){

        List<RelatorioStatusCompra> retorno = new ArrayList<RelatorioStatusCompra>();

        String sql = "select p.id as codigoProduto, "
                + " p.nome as nomeProduto, "
                + " pf.email as emailCliente, "
                + " pf.telefone as foneCliente, "
                + " p.valor_venda as valorVendaProduto, "
                + " pf.id as codigoCliente, "
                + " pf.nome as nomeCliente,"
                + " p.qtde_estoque as qtdEstoque, "
                + " cfc.id as codigoVenda, "
                + " cfc.status_venda_loja_virtual as statusVenda "
                + " from  vd_cp_loja_virt as cfc "
                + " inner join item_venda_loja as ntp on  ntp.venda_compra_loja_virt_id = cfc.id "
                + " inner join produto as p on p.id = ntp.produto_id "
                + " inner join pessoa_fisica as pf on pf.id = cfc.pessoa_id ";


        sql+= " where cfc.data_venda >= '"+relatorioStatusCompra.getDataInicial()+"' and cfc.data_venda  <= '"+relatorioStatusCompra.getDataFinal()+"' ";

        if(!relatorioStatusCompra.getNomeProduto().isEmpty()) {
            sql += " and upper(p.nome) like upper('%"+relatorioStatusCompra.getNomeProduto()+"%') ";
        }

        if (!relatorioStatusCompra.getStatusVenda().isEmpty()) {
            sql+= " and cfc.status_venda_loja_virtual in ('"+relatorioStatusCompra.getStatusVenda()+"') ";
        }

        if (!relatorioStatusCompra.getNomeCliente().isEmpty()) {
            sql += " and pf.nome like '%"+relatorioStatusCompra.getNomeCliente()+"%' ";
        }

        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RelatorioStatusCompra.class));

        return retorno;
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

        if(notaFiscalCompraRelatorioDTO.getCodigoNota().isEmpty() == false){
        sql = sql + " AND cfc.id = " + notaFiscalCompraRelatorioDTO.getCodigoNota() +"";
        }

        if(notaFiscalCompraRelatorioDTO.getCodigoProduto().isEmpty() == false){
            sql = sql + " AND p.id = " + notaFiscalCompraRelatorioDTO.getCodigoProduto() + "";
        }

        if(notaFiscalCompraRelatorioDTO.getNomeProduto().isEmpty() == false){
            sql = sql + " AND UPPER(p.nome) like '%" + notaFiscalCompraRelatorioDTO.getNomeProduto().toUpperCase() + "%'";
        }

        if(notaFiscalCompraRelatorioDTO.getNomeFornecedor().isEmpty() == false){
            sql = sql + " AND UPPER(pj.nome) like '%" + notaFiscalCompraRelatorioDTO.getNomeFornecedor().toUpperCase() + "%'";
        }

        List<NotaFiscalCompraRelatorioDTO> retorno = new ArrayList<NotaFiscalCompraRelatorioDTO>();

        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NotaFiscalCompraRelatorioDTO.class));

        return retorno;
    }

    public List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO> gerarRelatorioAlertaEstoque(NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO alertaEstoque ){

        List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO> retorno = new ArrayList<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO>();

        String sql = "select p.id as codigoProduto, p.nome as nomeProduto, "
                + " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada, "
                + " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra, "
                + " p.qtde_estoque as qtdEstoque, p.qtde_alerta_estoque as qtdAlertaEstoque "
                + " from nota_fiscal_compra as cfc "
                + " inner join nota_item_produto as ntp on  cfc.id = nota_fiscal_compra_id "
                + " inner join produto as p on p.id = ntp.produto_id "
                + " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where ";

        sql += " cfc.data_compra >='"+alertaEstoque.getDataInicial()+"' and ";
        sql += " cfc.data_compra <= '" + alertaEstoque.getDataFinal() +"' ";
        sql += " and p.alerta_qtde_estoque = true and p.qtde_estoque <= p.qtde_alerta_estoque ";

        if (!alertaEstoque.getCodigoNota().isEmpty()) {
            sql += " and cfc.id = " + alertaEstoque.getCodigoNota() + " ";
        }


        if (!alertaEstoque.getCodigoProduto().isEmpty()) {
            sql += " and p.id = " + alertaEstoque.getCodigoProduto() + " ";
        }

        if (!alertaEstoque.getNomeProduto().isEmpty()) {
            sql += " upper(p.nome) like upper('%"+alertaEstoque.getNomeProduto()+"')";
        }

        if (!alertaEstoque.getNomeFornecedor().isEmpty()) {
            sql += " upper(pj.nome) like upper('%"+alertaEstoque.getNomeFornecedor()+"')";
        }

        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.class));

        return retorno;
    }
}
