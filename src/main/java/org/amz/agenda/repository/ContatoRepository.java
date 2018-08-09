package org.amz.agenda.repository;

import org.amz.agenda.models.Contato;
import org.springframework.data.repository.CrudRepository;

public interface ContatoRepository extends CrudRepository<Contato, String>{
	
	Contato findByCodigo(long codigo);

}
