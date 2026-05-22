package org.amz.agenda.repository;

import org.amz.agenda.models.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

Contato findByCodigo(long codigo);

Page<Contato> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

}
