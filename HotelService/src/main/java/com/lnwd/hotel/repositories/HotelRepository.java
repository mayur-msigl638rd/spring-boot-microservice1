package com.lnwd.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lnwd.hotel.entities.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, String>{
	

}
