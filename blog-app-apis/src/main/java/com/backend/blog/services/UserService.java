package com.backend.blog.services;

import java.util.List;
import com.backend.blog.payloads.UserDto;


public interface UserService {
	
	UserDto createUser(UserDto user);
	UserDto updateUser(UserDto user, Integer userId);
	UserDto getUserById(Integer id);
	List<UserDto> getAllUsers();
    void deleteUser(Integer id);
		
	

}
