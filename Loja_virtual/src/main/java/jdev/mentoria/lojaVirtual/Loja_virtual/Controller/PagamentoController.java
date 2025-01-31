package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.VendaCompraLojaVirtualDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.VendaCompraLojaVirtual;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.VendaCompraLojaVirtualRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.VendaCompraLojaVirtualService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;

@Controller
public class PagamentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
    private final VendaCompraLojaVirtualService vendaCompraLojaVirtualService;

    public PagamentoController(VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, VendaCompraLojaVirtualService vendaCompraLojaVirtualService) {
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.vendaCompraLojaVirtualService = vendaCompraLojaVirtualService;
    }


    //@RequestMapping(method = RequestMethod.GET, value = "/pagamento/{idVendaCompra}")
    @GetMapping("/pagamento/{idVendaCompra}")
    public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {

        ModelAndView modelAndView = new ModelAndView("pagamento");

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(Long.parseLong(idVendaCompra));

        if (vendaCompraLojaVirtual == null) {
            modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
        }else {
            modelAndView.addObject("venda", vendaCompraLojaVirtualService.consultaVenda(vendaCompraLojaVirtual));
        }

        return modelAndView;
    }
}
