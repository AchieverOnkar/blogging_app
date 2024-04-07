package com.backend.blog.services.impl;

import java.util.List;


import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.blog.entities.User;
import com.backend.blog.exceptions.ResourceNotFoundException;
import com.backend.blog.payloads.UserDto;
import com.backend.blog.repositories.UserRepo;
import com.backend.blog.services.UserService;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto createUser(UserDto user) {
		// convert dto to user
		User usr = this.dtoToUser(user);
		// check if user already exist in db
		//encode password before storing
		usr.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		// if not then store the user in db
		User savedUsr = this.userRepo.save(usr);

		return this.userToDto(usr);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		// get the user using id
		// if user with that id is not found then exception will generete
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

		// update the user
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		user.setAbout(userDto.getAbout());
		// save the updated user
		this.userRepo.save(user);
		return this.userToDto(user);
	}

	@Override
	public UserDto getUserById(Integer id) {
		// if user with that id is not found then exception will generete
		User user = this.userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
        List<User> userList = this.userRepo.findAll();
        //convert each
        //converting each user to dto and collecting it in list
        List<UserDto> list = userList.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		return list;
	}

	@Override
	public void deleteUser(Integer id) {
		 User user = this.userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("user"," id ", id)); 
		//delete the user
		this.userRepo.delete(user);
	}

	//this convert userDto to user i.e map the two model/entities/class
	public User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		
//				User user = new User();
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
		return user;
	}

	public UserDto userToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		
		return userDto;
	}

}
