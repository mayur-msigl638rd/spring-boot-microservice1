package com.lnwd.hotel.services;

import java.util.List;

import com.lnwd.hotel.entities.Hotel;

public interface HotelService {

	Hotel create(Hotel hotel);
	List<Hotel>getAll();
	Hotel get(String id);
}
