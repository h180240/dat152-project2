/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {
	
	@Autowired
	OrderService orderService;

	// TODO - getAllBorrowOrders (@Mappings, URI=/orders, and method) + filter by expiry and paginate 
	@GetMapping("/orders")
	public ResponseEntity<List<Order>> getAllBorrowOrders(@RequestParam(required = false) LocalDate expiry, Pageable page) {
		List<Order> orders;
		if (expiry == null) {
			orders = orderService.findByPageable(page);
		} else {			
			orders = orderService.findByExpiryDate(expiry, page);
		}
		return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
	}
	
	// TODO - getBorrowOrder (@Mappings, URI=/orders/{id}, and method)
	// TODO - HATEOAS
	@GetMapping("/orders/{id}")
	public ResponseEntity<Order> getBorrowOrder(@PathVariable Long id) throws OrderNotFoundException, UpdateOrderFailedException {
		Order order = orderService.findOrder(id);
		
		Link linkSelf = linkTo(methodOn(OrderController.class)
				.getBorrowOrder(order.getId()))
				.withRel("self");
		order.add(linkSelf);
		
		Link linkUpdateBorrowOrder = linkTo(methodOn(OrderController.class)
				.updateOrder(order.getId(), null))
				.withRel("update_borrow_order");
		order.add(linkUpdateBorrowOrder);
		
		Link linkReturn = linkTo(methodOn(OrderController.class)
				.deleteBookOrder(order.getId()))
				.withRel("return_book");
		order.add(linkReturn);
		
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}
	
	// TODO - updateOrder (@Mappings, URI=/orders/{id}, and method)
	// TODO HATEOAS
	@PutMapping("/orders/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) throws OrderNotFoundException, UpdateOrderFailedException {
		Order updatedOrder = orderService.updateOrder(order, id);
		
		Link linkSelf = linkTo(methodOn(OrderController.class)
				.getBorrowOrder(updatedOrder.getId()))
				.withRel("self");
		updatedOrder.add(linkSelf);
		
		Link linkUpdateBorrowOrder = linkTo(methodOn(OrderController.class)
				.updateOrder(updatedOrder.getId(), null))
				.withRel("update_borrow_order");
		updatedOrder.add(linkUpdateBorrowOrder);
		
		Link linkReturn = linkTo(methodOn(OrderController.class)
				.deleteBookOrder(updatedOrder.getId()))
				.withRel("return_book");
		updatedOrder.add(linkReturn);
		
		return new ResponseEntity<Order>(updatedOrder, HttpStatus.OK);
	}
	
	// TODO - deleteBookOrder (@Mappings, URI=/orders/{id}, and method)
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Void> deleteBookOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
