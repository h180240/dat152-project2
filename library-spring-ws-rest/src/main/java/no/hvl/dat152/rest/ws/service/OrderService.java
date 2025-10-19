/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Order saveOrder(Order order) {
		
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findOrder(Long id) throws OrderNotFoundException {
		
		Order order = orderRepository.findById(id)
				.orElseThrow(()-> new OrderNotFoundException("Order with id: "+id+" not found in the order list!"));
		
		return order;
	}
	
	public void deleteOrder(Long id) {
		Optional<Order> managedOrder = orderRepository.findById(id);
		if (managedOrder.isEmpty()) return;
		
		orderRepository.delete(managedOrder.get());
	}
	
	// TODO public List<Order> findAllOrders()
	public List<Order> findAllOrders(){
		List<Order> allOrders = (List<Order>) orderRepository.findAll();
		
		return allOrders;
	}
	
	// TODO public List<Order> findByExpiryDate(LocalDate expiry, Pageable page)
	public List<Order> findByExpiryDate(LocalDate expiry, Pageable page) {
		return orderRepository.findOrderByExpiry(expiry, page.getPageSize(), (int) page.getOffset());
	}
	
	public List<Order> findByPageable(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}
	
	// TODO public Order updateOrder(Order order, Long id)
	public Order updateOrder(Order order, Long id) throws OrderNotFoundException, UpdateOrderFailedException {
		Order managedOrder = this.findOrder(id);
		if (order.getExpiry() == null) throw new UpdateOrderFailedException("Expiry can not be null");
		managedOrder.setExpiry(order.getExpiry());
		return orderRepository.save(managedOrder);
	}

}
