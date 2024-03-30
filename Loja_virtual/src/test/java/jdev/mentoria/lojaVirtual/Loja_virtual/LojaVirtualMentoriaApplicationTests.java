package jdev.mentoria.lojaVirtual.Loja_virtual;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdev.mentoria.lojaVirtual.Loja_virtual.Controller.AcessoController;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.AcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;


@SpringBootTest(classes = LojaVirtualApplication.class)
public class LojaVirtualMentoriaApplicationTests {

	@Autowired
	private AcessoController acessoController;

	@Autowired
	private AcessoRepository acessoRepository;

	@Autowired
	private WebApplicationContext wac;

	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {

		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_COMPRADOR");

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/salvarAcesso")
						.content(objectMapper.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());

		/*Conveter o retorno da API para um obejto de acesso*/

		Acesso objetoRetorno = objectMapper.
				readValue(retornoApi.andReturn().getResponse().getContentAsString(),
						Acesso.class);

		Assertions.assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());

	}



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

	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {

		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_TESTE_DELETE");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/deleteAcesso")
						.content(objectMapper.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());

		Assertions.assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
		Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());


	}

	@Test
	public void testRestApiDeletePorIDAcesso() throws JsonProcessingException, Exception {

		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_TESTE_DELETE_ID");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())
						.content(objectMapper.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());

		Assertions.assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
		Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());


	}

	@Test
	public void testRestApiObterAcessoID() throws JsonProcessingException, Exception {

		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_OBTER_ID");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/obterAcesso/" + acesso.getId())
						.content(objectMapper.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());


		Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);

		Assertions.assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());

		Assertions.assertEquals(acesso.getId(), acessoRetorno.getId());

	}

	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {

		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_TESTE_OBTER_LIST");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/buscarPorDesc/OBTER_LIST")
						.content(objectMapper.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());


		List<Acesso> retornoApiList = Collections.singletonList(objectMapper.
				readValue(retornoApi.andReturn()
						.getResponse().getContentAsString(), Acesso.class));
						//new TypeReference<List<>> () {});

		//Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);

		Assertions.assertEquals(1, retornoApiList.size());

		Assertions.assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());


		acessoRepository.deleteById(acesso.getId());

	}

}
