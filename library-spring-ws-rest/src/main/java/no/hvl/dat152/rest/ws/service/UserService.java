/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Book;
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

	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	
	// TODO public User saveUser(User user)
	
	// TODO public void deleteUser(Long id) throws UserNotFoundException 
	public void deleteUser(Long id) throws UserNotFoundException {
		Optional<User> managedUser = userRepository.findById(id);
		if (managedUser.isEmpty()) return;
		
		userRepository.delete(managedUser.get());
	}
	
	// TODO public User updateUser(User user, Long id)
	public User updateUser(User user, Long id) throws UserNotFoundException {
		User managedUser = this.findUser(id);
		managedUser.setFirstname(user.getFirstname());
		managedUser.setLastname(user.getLastname());
		managedUser.setOrders(user.getOrders());
		return userRepository.save(user);
	}
	
	// TODO public Set<Order> getUserOrders(Long userid) 
	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		User managedUser = this.findUser(userid);
		return managedUser.getOrders();
	}
	
	// TODO public Order getUserOrder(Long userid, Long oid)
	public Order getUserOrder(Long userid, Long orderid) throws UserNotFoundException, OrderNotFoundException {
		User managedUser = this.findUser(userid);
		for (Order order : managedUser.getOrders()) {
			if (order.getId() == orderid) return order;
		}
		throw new OrderNotFoundException("No order with id: "+orderid+" found");
	}
	
	// TODO public void deleteOrderForUser(Long userid, Long oid)
	
	// TODO public User createOrdersForUser(Long userid, Order order)
	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User managedUser = this.findUser(userid);
		managedUser.addOrder(order);
		return userRepository.save(managedUser);
	}
}
