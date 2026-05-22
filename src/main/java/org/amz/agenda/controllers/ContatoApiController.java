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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contatos")
public class ContatoApiController {

private static final Logger LOGGER = LoggerFactory.getLogger(ContatoApiController.class);

@Autowired
private ContatoRepository contatoRepository;

@GetMapping
public Page<Contato> listar(@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
@RequestParam(value = "page", required = false, defaultValue = "0") int page,
@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
int safePage = Math.max(page, 0);
int safeSize = Math.min(Math.max(size, 1), 50);
String filtroNome = nome == null ? "" : nome.trim();
Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("nome").ascending());

return filtroNome.isEmpty() ? contatoRepository.findAll(pageable)
: contatoRepository.findByNomeContainingIgnoreCase(filtroNome, pageable);
}

@GetMapping("/{codigo}")
public ResponseEntity<Contato> buscarPorCodigo(@PathVariable("codigo") long codigo) {
Contato contato = contatoRepository.findByCodigo(codigo);
if (contato == null) {
LOGGER.warn("API: contato não encontrado. codigo={}", codigo);
return ResponseEntity.notFound().build();
}
return ResponseEntity.ok(contato);
}

@PostMapping
public ResponseEntity<Contato> criar(@Valid @RequestBody Contato contato) {
Contato salvo = contatoRepository.save(contato);
LOGGER.info("API: contato criado. codigo={} nome={}", salvo.getCodigo(), salvo.getNome());
return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
}

@PutMapping("/{codigo}")
public ResponseEntity<Contato> atualizar(@PathVariable("codigo") long codigo, @Valid @RequestBody Contato contato) {
Contato existente = contatoRepository.findByCodigo(codigo);
if (existente == null) {
LOGGER.warn("API: tentativa de atualização em contato inexistente. codigo={}", codigo);
return ResponseEntity.notFound().build();
}
contato.setCodigo(codigo);
Contato salvo = contatoRepository.save(contato);
LOGGER.info("API: contato atualizado. codigo={} nome={}", salvo.getCodigo(), salvo.getNome());
return ResponseEntity.ok(salvo);
}

@DeleteMapping("/{codigo}")
public ResponseEntity<Void> deletar(@PathVariable("codigo") long codigo) {
Contato contato = contatoRepository.findByCodigo(codigo);
if (contato == null) {
LOGGER.warn("API: tentativa de exclusão em contato inexistente. codigo={}", codigo);
return ResponseEntity.notFound().build();
}
contatoRepository.delete(contato);
LOGGER.info("API: contato excluído. codigo={} nome={}", contato.getCodigo(), contato.getNome());
return ResponseEntity.noContent().build();
}

}
