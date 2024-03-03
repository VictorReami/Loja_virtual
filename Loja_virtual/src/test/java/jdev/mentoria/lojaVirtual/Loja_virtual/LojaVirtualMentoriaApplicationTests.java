package jdev.mentoria.lojaVirtual.Loja_virtual;

import jdev.mentoria.lojaVirtual.Loja_virtual.Controller.AcessoController;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.AcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;


@SpringBootTest(classes = LojaVirtualApplication.class)
public class LojaVirtualMentoriaApplicationTests {

	@Autowired
	private AcessoController acessoController;

	@Autowired
	private AcessoRepository acessoRepository;


    @Test
	public void testCadastraAcesso() {

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_ADMIN");

		Assertions.assertEquals(true, acesso.getId() == null);

		/*Gravou no banco de dados*/
		acesso = this.acessoController.salvarAcesso(acesso).getBody();

		Assertions.assertEquals(true,acesso.getId() > 0);

		/*Validar dados salvos da forma correta*/
		Assertions.assertEquals("ROLE_ADMIN", acesso.getDescricao());

		/*Teste de carregamento*/

		Acesso acesso2 = this.acessoRepository.findById(acesso.getId()).get();

		Assertions.assertEquals(acesso.getId(), acesso2.getId());


		/*Teste de delete*/

		this.acessoRepository.deleteById(acesso2.getId());

		this.acessoRepository.flush(); /*Roda esse SQL de delete no banco de dados*/

		Acesso acesso3 = this.acessoRepository.findById(acesso2.getId()).orElse(null);

		Assertions.assertEquals(true, acesso3 == null);


		/*Teste de query*/

		acesso = new Acesso();

		acesso.setDescricao("ROLE_ALUNO");

		acesso = this.acessoController.salvarAcesso(acesso).getBody();

		List<Acesso> acessos = this.acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());

		Assertions.assertEquals(1, acessos.size());

		this.acessoRepository.deleteById(acesso.getId());



	}

}
