/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
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
	
	public Book updateBook(Book book, String isbn) throws BookNotFoundException, UpdateBookFailedException {
		Book managedBook = findByISBN(isbn);
		if (book.getTitle() == null) throw new UpdateBookFailedException("Title can not be null");
		managedBook.setTitle(book.getTitle());
		if (book.getAuthors() == null) throw new UpdateBookFailedException("Authors can not be null");
		managedBook.setAuthors(book.getAuthors());
		return bookRepository.save(managedBook);
	}
	
	// TODO public List<Book> findAllPaginate(Pageable page)
	
	// TODO public Set<Author> findAuthorsOfBookByISBN(String isbn)
	
	public void deleteById(long id) {
		Optional<Book> managedBook = bookRepository.findById(id);
		if (managedBook.isEmpty()) return;
		
		bookRepository.delete(managedBook.get());
	}
	
	public void deleteByISBN(String isbn) {
		Optional<Book> managedBook = bookRepository.findByIsbn(isbn);
		if (managedBook.isEmpty()) return;
		
		bookRepository.delete(managedBook.get());
	}
	
}
