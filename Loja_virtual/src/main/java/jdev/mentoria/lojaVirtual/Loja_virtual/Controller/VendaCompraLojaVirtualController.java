package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.ItemVendaLojaDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.VendaCompraLojaVirtualDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.VendaCompraLojaVirtualService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class VendaCompraLojaVirtualController {

    private final VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
    private final EnderecoRepository enderecoRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final NotaFiscalVendaRepository notaFiscalVendaRepository;
    private final PessoaController pessoaController;
    private final StatusRastreioRepository statusRastreioRepository;

    private final VendaCompraLojaVirtualService vendaCompraLojaVirtualService;

    public VendaCompraLojaVirtualController(VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, EnderecoRepository enderecoRepository, PessoaFisicaRepository pessoaFisicaRepository, NotaFiscalVendaRepository notaFiscalVendaRepository, PessoaController pessoaController, StatusRastreioRepository statusRastreioRepository, VendaCompraLojaVirtualService vendaCompraLojaVirtualService) {
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.enderecoRepository = enderecoRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.notaFiscalVendaRepository = notaFiscalVendaRepository;
        this.pessoaController = pessoaController;
        this.statusRastreioRepository = statusRastreioRepository;
        this.vendaCompraLojaVirtualService = vendaCompraLojaVirtualService;
    }

    @ResponseBody
    @PostMapping(value = "/salvarCompraVendaLoja")
    public ResponseEntity<VendaCompraLojaVirtualDTO> salvarCompraVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionMentoriaJava {

        vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        PessoaFisica pessoaFisica = pessoaController.salvarCpf(vendaCompraLojaVirtual.getPessoa()).getBody();
        vendaCompraLojaVirtual.setPessoa(pessoaFisica);

        vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

        vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

        vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

        for(int i = 0; i < vendaCompraLojaVirtual.getItemVendaLoja().size(); i++){

            vendaCompraLojaVirtual.getItemVendaLoja().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            vendaCompraLojaVirtual.getItemVendaLoja().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

        }

        /*Persiste novamente as nota fiscal novamente pra ficar amarrada na venda*/
        notaFiscalVendaRepository.save(vendaCompraLojaVirtual.getNotaFiscalVenda());

        /*Salva primeiro a venda e todo os dados*/
        vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.save(vendaCompraLojaVirtual);

        /*Associa a venda gravada no banco com a nota fiscal*/
        vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

        StatusRastreio statusRastreio = new StatusRastreio();

        statusRastreio.setCentroDestribuicao("Joja local");
        statusRastreio.setCidade("Americana");
        statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        statusRastreio.setEstado("SP");
        statusRastreio.setStatus("Inicio Compra");
        statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        statusRastreioRepository.save(statusRastreio);

        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

        vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
        vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
        vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

        for(ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLoja()){

            ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

            itemVendaLojaDTO.setProduto(item.getProduto());
            itemVendaLojaDTO.setQuantidade(item.getQuantidade());

            vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
        }

        return new ResponseEntity<VendaCompraLojaVirtualDTO>( vendaCompraLojaVirtualDTO, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/buscaCompraVendaLoja/{id}")
    public ResponseEntity<VendaCompraLojaVirtualDTO> buscaCompraVendaLoja (@PathVariable("id") Long id){

        VendaCompraLojaVirtual vendaCompraLojaVirtual  = vendaCompraLojaVirtualRepository.findById(id).orElse(new VendaCompraLojaVirtual());

        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

        vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
        vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
        vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
        vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

        for(ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLoja()){

            ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

            itemVendaLojaDTO.setProduto(item.getProduto());
            itemVendaLojaDTO.setQuantidade(item.getQuantidade());

            vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);
        }

        return new ResponseEntity<VendaCompraLojaVirtualDTO>( vendaCompraLojaVirtualDTO, HttpStatus.OK);
    }


    @ResponseBody
    @DeleteMapping(value = "/deleteCompraVendaLoja/{idVenda}")
    public ResponseEntity<String> deleteCompraVendaLoja(@PathVariable("idVenda") Long idVenda){

        vendaCompraLojaVirtualService.exclusaoTotalVendaBanco(idVenda);

        return new ResponseEntity<String>("CompraVenda deletada com sucesso.", HttpStatus.OK);

    }

}
