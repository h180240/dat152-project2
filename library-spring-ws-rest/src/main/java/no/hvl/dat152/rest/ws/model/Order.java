/**
 * 
 */
package no.hvl.dat152.rest.ws.model;


import java.time.LocalDate;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import no.hvl.dat152.rest.ws.controller.OrderController;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;

/**
 * 
 */
@Entity
@Table(name = "orders")
public class Order extends RepresentationModel<Order>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String isbn;
	
	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiry;
	
	public Order() {
		//default
	}
	
	public Order(String isbn, LocalDate expiry) {
		this.isbn = isbn;
		this.expiry = expiry;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the expiry
	 */
	public LocalDate getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(LocalDate expiry) {
		this.expiry = expiry;
	}
	
	@Override
    public final int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((Long.valueOf(id) == null) ? 0 : Long.hashCode(id));
		result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
		result = prime * result + ((expiry == null) ? 0 : expiry.hashCode());
        return result;
    }
	
	@Override
	public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Order order = (Order) obj;
        
        return this.id == order.id;
	 }
	
	@Override
    public String toString() {
        return "Order [isbn=" + isbn + ", expiry=" + expiry  + "]";
    }
	
	public void addLinks() throws OrderNotFoundException, UpdateOrderFailedException {
		Link linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
				.getBorrowOrder(this.getId()))
				.withRel("self");
		this.add(linkSelf);
		
		Link linkUpdateBorrowOrder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
				.updateOrder(this.getId(), null))
				.withRel("update_borrow_order");
		this.add(linkUpdateBorrowOrder);
		
		Link linkReturn = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
				.deleteBookOrder(this.getId()))
				.withRel("return_book");
		this.add(linkReturn);
	}
	 
}
