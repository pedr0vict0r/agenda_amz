package org.amz.agenda.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.amz.agenda.models.Contato;
import org.amz.agenda.repository.ContatoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ContatoController.class)
class ContatoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContatoRepository contatoRepository;

	@Test
	void deveListarContatos() throws Exception {
		when(contatoRepository.findAll()).thenReturn(java.util.List.of());

		mockMvc.perform(get("/contatos"))
				.andExpect(status().isOk())
				.andExpect(view().name("/contato/listaContatos"))
				.andExpect(model().attributeExists("contatos"));
	}

	@Test
	void deveCadastrarContatoComDadosValidos() throws Exception {
		mockMvc.perform(post("/cadastrarContato")
				.param("nome", "Maria Silva")
				.param("numero", "(11) 99999-9999"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cadastrarContato"));

		verify(contatoRepository, times(1)).save(any(Contato.class));
	}

	@Test
	void naoDeveCadastrarContatoComDadosInvalidos() throws Exception {
		mockMvc.perform(post("/cadastrarContato")
				.param("nome", "")
				.param("numero", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cadastrarContato"));

		verify(contatoRepository, never()).save(any(Contato.class));
	}

	@Test
	void deveExibirDetalhesDoContato() throws Exception {
		Contato contato = contato(1L, "João", "(21) 98888-7777");
		when(contatoRepository.findByCodigo(1L)).thenReturn(contato);

		mockMvc.perform(get("/contatos/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("contato/detalhesContato"))
				.andExpect(model().attributeExists("contato"));
	}

	@Test
	void deveRedirecionarQuandoContatoNaoExistirNosDetalhes() throws Exception {
		when(contatoRepository.findByCodigo(99L)).thenReturn(null);

		mockMvc.perform(get("/contatos/99"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contatos"));
	}

	@Test
	void deveExibirTelaDeEdicao() throws Exception {
		Contato contato = contato(2L, "Ana", "(31) 97777-6666");
		when(contatoRepository.findByCodigo(2L)).thenReturn(contato);

		mockMvc.perform(get("/contatos/2/editar"))
				.andExpect(status().isOk())
				.andExpect(view().name("contato/editarContato"))
				.andExpect(model().attributeExists("contato"));
	}

	@Test
	void deveSalvarAlteracaoDoContato() throws Exception {
		when(contatoRepository.findByCodigo(3L)).thenReturn(contato(3L, "Carlos", "(41) 96666-5555"));

		mockMvc.perform(post("/contatos/3/editar")
				.param("codigo", "3")
				.param("nome", "Carlos")
				.param("numero", "(41) 96666-5555"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contatos"));

		verify(contatoRepository, times(1)).save(any(Contato.class));
	}

	@Test
	void deveExcluirContato() throws Exception {
		Contato contato = contato(4L, "Paula", "(51) 95555-4444");
		when(contatoRepository.findByCodigo(eq(4L))).thenReturn(contato);

		mockMvc.perform(post("/contatos/4/excluir"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contatos"));

		verify(contatoRepository, times(1)).delete(contato);
	}

	private Contato contato(long codigo, String nome, String numero) {
		Contato contato = new Contato();
		contato.setCodigo(codigo);
		contato.setNome(nome);
		contato.setNumero(numero);
		return contato;
	}
}
