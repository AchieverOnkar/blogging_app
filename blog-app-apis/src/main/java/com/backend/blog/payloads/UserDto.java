package com.backend.blog.payloads;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private Integer id;
	
	
	@NotEmpty
	@Size(min = 3,message = "Name must be greater than 2 chars !!")
	private String name;

	@Email(message = "Entered Email is invalid !!")
	private String email;
	

	//@Pattern(regexp = ) u can use reg ex also
	@NotEmpty
	@Size(min = 6,max = 15,message = "Password must be greater than 5 chars and less than 15 chars")
	private String password;
	
	
	@NotNull
	private String about;

}
