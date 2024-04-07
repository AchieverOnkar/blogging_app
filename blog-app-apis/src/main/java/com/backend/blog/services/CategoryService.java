package com.backend.blog.services;


import java.util.List;

import com.backend.blog.payloads.CategoryDto;

public interface CategoryService {

	
	//create category
	public CategoryDto createCategory(CategoryDto categoryDto);
	
	//get category
	public CategoryDto getCategoryById(Integer id);
	//get all category
	public List<CategoryDto> getAllCategory();
	//update category
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	//deleteCategory
	public void deleteCategory(Integer id);

}
