package com.backend.blog.payloads;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
	private Integer categoryId;
	@NotEmpty(message = "title can not be empty or null !!")
	private String categoryTitle;
	@Size(min = 15,max = 300, message = "Description should be between 15 to 300 chars !!")
	private String categoryDescription;
}
