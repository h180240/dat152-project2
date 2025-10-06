/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	
	public Book saveBook(Book book) {
		
		return bookRepository.save(book);
		
	}
	
	public List<Book> findAll(){
		
		return (List<Book>) bookRepository.findAll();
		
	}
	
	
	public Book findByISBN(String isbn) throws BookNotFoundException {
		
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
		
		return book;
	}
	
	// TODO: 1
	public Book updateBook(Book book, String isbn) throws BookNotFoundException, UpdateBookFailedException {
		
		Book existingBook = bookRepository.findBookByISBN(isbn);
		
		existingBook.setTitle(book.getTitle());
		existingBook.setAuthors(book.getAuthors());
		
		return bookRepository.save(existingBook);
	}
	
	// TODO public List<Book> findAllPaginate(Pageable page)
	
	// TODO public Set<Author> findAuthorsOfBookByISBN(String isbn)
	
	// TODO public void deleteById(long id)
	
	// TODO: 2
	public void deleteByISBN(String isbn) throws BookNotFoundException {
		Book existingBook = bookRepository.findBookByISBN(isbn);
		
		if (existingBook == null) {
			throw new BookNotFoundException("Book with ISBN = " + isbn + " not found.");
		}
		
		bookRepository.delete(existingBook);
	
	}
	
}
