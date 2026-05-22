package org.amz.agenda.controllers;

import javax.validation.Valid;

import org.amz.agenda.models.Contato;
import org.amz.agenda.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContatoController {
	
	@Autowired
	private ContatoRepository cr;
	
	@GetMapping("/cadastrarContato")
	public String form() {
		return "contato/formContato";
	}
	
	@PostMapping("/cadastrarContato")
	public String form(@Valid Contato contato, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("msgErro", "Verifique os campos!");
			return "redirect:/cadastrarContato";
		}
		cr.save(contato);
		attributes.addFlashAttribute("msgSucesso", "Contato adicionado com sucesso!");
		return "redirect:/cadastrarContato";
	}
	
	@GetMapping("/contatos")
	public ModelAndView listaContatos() {
		ModelAndView mv = new ModelAndView("/contato/listaContatos");
		Iterable<Contato> contatos = cr.findAll();
		mv.addObject("contatos", contatos);
		return mv;
	}
	
	
	@GetMapping("/contatos/{codigo}")
	public ModelAndView detalhesContato(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
		Contato contato = cr.findByCodigo(codigo);
		ModelAndView mv;
		if (contato == null) {
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
			attributes.addFlashAttribute("msgErro", "Contato não encontrado!");
			return "redirect:/contatos";
		}
		cr.delete(contato);
		attributes.addFlashAttribute("msgSucesso", "Contato excluído com sucesso!");
		return "redirect:/contatos";
	}
	
	@GetMapping("/contatos/{codigo}/editar")
	public ModelAndView editarContato(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
		Contato contato = cr.findByCodigo(codigo);
		if (contato == null) {
			attributes.addFlashAttribute("msgErro", "Contato não encontrado!");
			return new ModelAndView("redirect:/contatos");
		}
		ModelAndView mv = new ModelAndView("contato/editarContato");
		mv.addObject("contato", contato);
		return mv;
	}
	
	@PostMapping("/contatos/{codigo}/editar")
	public ModelAndView salvarAlteracao(@PathVariable("codigo") long codigo, @Valid Contato contato, BindingResult result, RedirectAttributes attributes) {

			Contato contatoAtual = cr.findByCodigo(codigo);
			if (contatoAtual == null) {
				attributes.addFlashAttribute("msgErro", "Contato não encontrado!");
				return new ModelAndView("redirect:/contatos");
			}
	
			if(result.hasErrors()) {
				ModelAndView mv = new ModelAndView("contato/editarContato");
				mv.addObject("contato", contato);
				return mv;
			}
			contato.setCodigo(codigo);
			cr.save(contato);
			attributes.addFlashAttribute("msgSucesso", "Contato alterado com sucesso!");
			return new ModelAndView("redirect:/contatos");
	}

}
