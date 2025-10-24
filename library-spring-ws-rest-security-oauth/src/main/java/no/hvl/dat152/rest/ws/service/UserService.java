/**
 * 
 */
package no.hvl.dat152.rest.ws.service;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderService orderService;
	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	public void deleteUser(Long id) {
		Optional<User> managedUser = userRepository.findById(id);
		if (managedUser.isEmpty()) return;
		
		userRepository.delete(managedUser.get());
	}
	
	public User updateUser(User user, Long id) throws UserNotFoundException {
		User managedUser = this.findUser(id);
		managedUser.setFirstname(user.getFirstname());
		managedUser.setLastname(user.getLastname());
		managedUser.setOrders(user.getOrders());
		return userRepository.save(user);
	}
	
	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		User managedUser = this.findUser(userid);
		return managedUser.getOrders();
	}
	
	public Order getUserOrder(Long userid, Long orderid) throws UserNotFoundException, OrderNotFoundException {
		User managedUser = this.findUser(userid);
		for (Order order : managedUser.getOrders()) {
			if (order.getId() == orderid) return order;
		}
		throw new OrderNotFoundException("No order with id: "+orderid+" found");
	}
	
	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = this.findUser(userid);
		Order order = orderService.findOrder(oid);
		user.getOrders().remove(order);
	}
	
	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User managedUser = this.findUser(userid);
		managedUser.addOrder(order);
		return userRepository.save(managedUser);
	}
	
}
