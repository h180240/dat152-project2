/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat152.rest.ws.model.Author;

/**
 * @author tdoy
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
	

}
