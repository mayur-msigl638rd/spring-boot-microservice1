package com.lcwd.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exception.ResourceNotFoundException;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService  {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		
		return userRepository.findAll();
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		User user =  userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given id not found on server !! : " + userId ));
	
	 Rating[] ratingsOfUser = restTemplate.getForObject("http://localhost:8084/ratings/users/"+user.getUserId(), Rating[].class);
	 logger.info("{} ",ratingsOfUser);
	 
	List<Rating> ratings =  Arrays.stream(ratingsOfUser).toList();
	List<Rating> ratingList =  ratings.stream().map(rating -> {
		 
		//api call to hotel service to get the hotel
		//http://localhost:8082/hotels/5b59d4de-f1d2-4bd3-98f3-e664f774667e
		ResponseEntity<Hotel>forEntity = restTemplate.getForEntity("http://localhost:8082/hotels/"+rating.getHotelId(), Hotel.class);
		Hotel hotel = forEntity.getBody(); 
		logger.info("response satus code:{} ", forEntity.getStatusCode());
		
		//set the hotel to rating
		rating.setHotel(hotel);
		
		//return the rating
		return rating;
		
	 }).collect(Collectors.toList());
	 
	 user.setRatings(ratingList);
	
	 return user;
	}

}
