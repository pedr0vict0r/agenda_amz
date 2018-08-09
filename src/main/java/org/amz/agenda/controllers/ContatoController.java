package org.amz.agenda.controllers;

import javax.validation.Valid;

import org.amz.agenda.models.Contato;
import org.amz.agenda.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContatoController {
	
	@Autowired
	private ContatoRepository cr;
	
	@RequestMapping(value="/cadastrarContato", method=RequestMethod.GET)
	public String form() {
		return "contato/formContato";
	}
	
	@RequestMapping(value="/cadastrarContato", method=RequestMethod.POST)
	public String form(@Valid Contato contato, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("msgErro", "Verifique os campos!");
			return "redirect:/cadastrarContato";
		}
		cr.save(contato);
		attributes.addFlashAttribute("msgSucesso", "Contato adicionado com sucesso!");
		return "redirect:/cadastrarContato";
	}
	
	@RequestMapping("/contatos")
	public ModelAndView listaContatos() {
		ModelAndView mv = new ModelAndView("/contato/listaContatos");
		Iterable<Contato> contatos = cr.findAll();
		mv.addObject("contatos", contatos);
		return mv;
	}
	
	
	@RequestMapping("/{codigo}")
	public ModelAndView detalhesContato(@PathVariable("codigo") long codigo) {
		Contato contato = cr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("contato/detalhesContato");
		mv.addObject("contato", contato);
		System.out.println("contato" + contato);
		return mv;
	}
	
	@RequestMapping("/deletar")
	public String deletarContato(long codigo) {
		Contato contato = cr.findByCodigo(codigo);
		cr.delete(contato);
		return "redirect:/contatos";
	}
	
	@RequestMapping("/editar")
	public ModelAndView editarContato(long codigo) {
		Contato contato = cr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("contato/editarContato");
		mv.addObject("contato", contato);
		System.out.println("contato" + contato);
		return mv;
	}
	
	@RequestMapping("/salvar")
	public String salvarAlteracao(@Valid Contato contato, BindingResult result, RedirectAttributes attributes) {
	
			if(result.hasErrors()) {
				attributes.addFlashAttribute("msgErro", "Verifique os campos!");
				return "redirect:/contatos";
			}
			cr.save(contato);
			attributes.addFlashAttribute("msgSucesso", "Contato alterado com sucesso!");
			return "redirect:/contatos";
	}

}
