package org.amz.agenda.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.amz.agenda.models.Contato;
import org.amz.agenda.repository.ContatoRepository;
import org.amz.agenda.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ContatoApiController.class)
@Import(SecurityConfig.class)
class ContatoApiControllerTest {

@Autowired
private MockMvc mockMvc;

@MockBean
private ContatoRepository contatoRepository;

@Test
void deveListarContatosViaApi() throws Exception {
Contato contato = contato(1L, "Maria", "(11) 99999-9999");
when(contatoRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
.thenReturn(new PageImpl<>(java.util.List.of(contato)));

mockMvc.perform(get("/api/contatos"))
.andExpect(status().isOk())
.andExpect(jsonPath("$.content[0].nome").value("Maria"));
}

@Test
void deveBuscarContatoPorCodigoViaApi() throws Exception {
when(contatoRepository.findByCodigo(10L)).thenReturn(contato(10L, "Ana", "(31) 99999-0000"));

mockMvc.perform(get("/api/contatos/10"))
.andExpect(status().isOk())
.andExpect(jsonPath("$.codigo").value(10))
.andExpect(jsonPath("$.nome").value("Ana"));
}

@Test
@WithMockUser(roles = "EDITOR")
void deveCriarContatoViaApi() throws Exception {
Contato salvo = contato(11L, "Paulo", "(41) 98888-7777");
when(contatoRepository.save(any(Contato.class))).thenReturn(salvo);

mockMvc.perform(post("/api/contatos")
.with(csrf())
.contentType(MediaType.APPLICATION_JSON)
.content("{\"nome\":\"Paulo\",\"numero\":\"(41) 98888-7777\"}"))
.andExpect(status().isCreated())
.andExpect(jsonPath("$.codigo").value(11));

verify(contatoRepository, times(1)).save(any(Contato.class));
}

@Test
void naoDeveCriarContatoSemAutenticacaoViaApi() throws Exception {
mockMvc.perform(post("/api/contatos")
.with(csrf())
.contentType(MediaType.APPLICATION_JSON)
.content("{\"nome\":\"Paulo\",\"numero\":\"(41) 98888-7777\"}"))
.andExpect(status().isUnauthorized());

verify(contatoRepository, never()).save(any(Contato.class));
}

@Test
@WithMockUser(roles = "EDITOR")
void deveAtualizarContatoViaApi() throws Exception {
when(contatoRepository.findByCodigo(12L)).thenReturn(contato(12L, "Carlos", "(41) 90000-0000"));
when(contatoRepository.save(any(Contato.class))).thenReturn(contato(12L, "Carlos Atualizado", "(41) 91111-1111"));

mockMvc.perform(put("/api/contatos/12")
.with(csrf())
.contentType(MediaType.APPLICATION_JSON)
.content("{\"nome\":\"Carlos Atualizado\",\"numero\":\"(41) 91111-1111\"}"))
.andExpect(status().isOk())
.andExpect(jsonPath("$.nome").value("Carlos Atualizado"));
}

@Test
@WithMockUser(roles = "EDITOR")
void naoDeveAtualizarContatoInexistenteViaApi() throws Exception {
when(contatoRepository.findByCodigo(999L)).thenReturn(null);

mockMvc.perform(put("/api/contatos/999")
.with(csrf())
.contentType(MediaType.APPLICATION_JSON)
.content("{\"nome\":\"X\",\"numero\":\"(11) 99999-0000\"}"))
.andExpect(status().isNotFound());

verify(contatoRepository, never()).save(any(Contato.class));
}

@Test
@WithMockUser(roles = "EDITOR")
void deveExcluirContatoViaApi() throws Exception {
Contato contato = contato(20L, "Joana", "(11) 90000-1111");
when(contatoRepository.findByCodigo(20L)).thenReturn(contato);

mockMvc.perform(delete("/api/contatos/20").with(csrf()))
.andExpect(status().isNoContent());

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
