package org.amz.agenda.controllers;

import javax.validation.Valid;

import org.amz.agenda.models.Contato;
import org.amz.agenda.repository.ContatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContatoController {

private static final Logger LOGGER = LoggerFactory.getLogger(ContatoController.class);

@Autowired
private ContatoRepository cr;

@GetMapping("/cadastrarContato")
public String form() {
return "contato/formContato";
}

@PostMapping("/cadastrarContato")
public String form(@Valid Contato contato, BindingResult result, RedirectAttributes attributes) {

if (result.hasErrors()) {
attributes.addFlashAttribute("msgErro", "Verifique os campos!");
return "redirect:/cadastrarContato";
}
cr.save(contato);
LOGGER.info("Contato cadastrado. nome={}", contato.getNome());
attributes.addFlashAttribute("msgSucesso", "Contato adicionado com sucesso!");
return "redirect:/cadastrarContato";
}

@GetMapping("/contatos")
public ModelAndView listaContatos(@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
@RequestParam(value = "page", required = false, defaultValue = "0") int page,
@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
int safePage = Math.max(page, 0);
int safeSize = Math.min(Math.max(size, 1), 50);
String filtroNome = nome == null ? "" : nome.trim();
Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("nome").ascending());
Page<Contato> contatos = filtroNome.isEmpty() ? cr.findAll(pageable)
: cr.findByNomeContainingIgnoreCase(filtroNome, pageable);

ModelAndView mv = new ModelAndView("/contato/listaContatos");
mv.addObject("contatos", contatos);
mv.addObject("nome", filtroNome);
mv.addObject("size", safeSize);
return mv;
}

@GetMapping("/contatos/{codigo}")
public ModelAndView detalhesContato(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
Contato contato = cr.findByCodigo(codigo);
ModelAndView mv;
if (contato == null) {
LOGGER.warn("Tentativa de visualizar contato inexistente. codigo={}", codigo);
attributes.addFlashAttribute("msgErro", "Contato não encontrado!");
return new ModelAndView("redirect:/contatos");
}
mv = new ModelAndView("contato/detalhesContato");
mv.addObject("contato", contato);
return mv;
}

@PostMapping("/contatos/{codigo}/excluir")
public String deletarContato(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
Contato contato = cr.findByCodigo(codigo);
if (contato == null) {
LOGGER.warn("Tentativa de excluir contato inexistente. codigo={}", codigo);
attributes.addFlashAttribute("msgErro", "Contato não encontrado!");
return "redirect:/contatos";
}
cr.delete(contato);
LOGGER.info("Contato excluído. codigo={} nome={}", contato.getCodigo(), contato.getNome());
attributes.addFlashAttribute("msgSucesso", "Contato excluído com sucesso!");
return "redirect:/contatos";
}

@GetMapping("/contatos/{codigo}/editar")
public ModelAndView editarContato(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
Contato contato = cr.findByCodigo(codigo);
if (contato == null) {
LOGGER.warn("Tentativa de editar contato inexistente. codigo={}", codigo);
attributes.addFlashAttribute("msgErro", "Contato não encontrado!");
return new ModelAndView("redirect:/contatos");
}
ModelAndView mv = new ModelAndView("contato/editarContato");
mv.addObject("contato", contato);
return mv;
}

@PostMapping("/contatos/{codigo}/editar")
public ModelAndView salvarAlteracao(@PathVariable("codigo") long codigo, @Valid Contato contato, BindingResult result,
RedirectAttributes attributes) {

Contato contatoAtual = cr.findByCodigo(codigo);
if (contatoAtual == null) {
LOGGER.warn("Tentativa de salvar alteração em contato inexistente. codigo={}", codigo);
attributes.addFlashAttribute("msgErro", "Contato não encontrado!");
return new ModelAndView("redirect:/contatos");
}

if (result.hasErrors()) {
ModelAndView mv = new ModelAndView("contato/editarContato");
mv.addObject("contato", contato);
return mv;
}
contato.setCodigo(codigo);
cr.save(contato);
LOGGER.info("Contato alterado. codigo={} nome={}", contato.getCodigo(), contato.getNome());
attributes.addFlashAttribute("msgSucesso", "Contato alterado com sucesso!");
return new ModelAndView("redirect:/contatos");
}

}
