/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
		
	public Author findById(long id) throws AuthorNotFoundException {
		
		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + "not found"));
		
		return author;
	}
	
	// TODO
	public Author saveAuthor(Author author) throws AuthorNotFoundException {
		return authorRepository.save(author);
	}
		
	// TODO
	public Author updateAuthor(Author author, long id) throws AuthorNotFoundException {
		Author existingAuthor = findById(id);
		
		existingAuthor.setFirstname(author.getFirstname());
		existingAuthor.setLastname(author.getLastname());
		existingAuthor.setBooks(author.getBooks());
		
		return authorRepository.save(existingAuthor);
	}
		
	// TODO
	public List<Author> findAll() {
		return (List<Author>) authorRepository.findAll();
	}
	
	// TODO
	public void deleteById(Long id) throws AuthorNotFoundException {
		Author existingAuthor = findById(id);
		authorRepository.delete(existingAuthor);
	}

	// TODO
	public Set<Book> findBooksByAuthorId(Long id) throws AuthorNotFoundException {
		Author author = findById(id);
		return author.getBooks();
	}
}
