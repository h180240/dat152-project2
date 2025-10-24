/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.security.IsAdmin;
import no.hvl.dat152.rest.ws.security.IsAdminOrThisUser;
import no.hvl.dat152.rest.ws.security.UserDetailsImpl;
import no.hvl.dat152.rest.ws.service.OrderService;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
@IsAdmin
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(){
		
		List<User> users = userService.findAllUsers();
		
		if(users.isEmpty())
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User managedUser = userService.saveUser(user);
		return new ResponseEntity<>(managedUser, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/users/{userid}")
	@IsAdminOrThisUser
	public ResponseEntity<Object> getUser(@PathVariable Long userid, Authentication auth) throws UserNotFoundException, OrderNotFoundException{
		User user = userService.findUser(userid);
		return new ResponseEntity<>(user, HttpStatus.OK);	
	}
	
	@PutMapping("/users/{userid}")
	@IsAdminOrThisUser
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long userid) throws UserNotFoundException {
		User updatedUser = userService.updateUser(user, userid);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{userid}")
	@IsAdminOrThisUser
	public ResponseEntity<Void> deleteUser(@PathVariable Long userid) {
		userService.deleteUser(userid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/users/{userid}/orders")
	@IsAdminOrThisUser
	public ResponseEntity<Set<Order>> getUserOrders(@PathVariable Long userid) throws UserNotFoundException {
		Set<Order> orders = userService.getUserOrders(userid);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/users/{userid}/orders/{oid}")
	@IsAdminOrThisUser
	public ResponseEntity<Order> getUserOrder(@PathVariable Long userid, @PathVariable Long oid) throws UserNotFoundException, OrderNotFoundException {
		Order order = userService.getUserOrder(userid, oid);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping("/users/{userid}/orders/{oid}")
	@IsAdminOrThisUser
	public ResponseEntity<Void> deleteUserOrder(@PathVariable Long userid, @PathVariable Long oid) throws UserNotFoundException, OrderNotFoundException {
		userService.deleteOrderForUser(userid, oid);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/users/{userid}/orders")
	@IsAdminOrThisUser
	public ResponseEntity<List<Order>> createUserOrder(@PathVariable Long userid, @RequestBody Order order) throws UserNotFoundException, OrderNotFoundException, UpdateOrderFailedException {
		Order managedOrder = orderService.saveOrder(order);
		userService.createOrdersForUser(userid, managedOrder);
		
		Order updatedOrder = orderService.updateOrder(order, userid);
		
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
		
		return new ResponseEntity<>(List.of(managedOrder), HttpStatus.CREATED);
	}
	
}
