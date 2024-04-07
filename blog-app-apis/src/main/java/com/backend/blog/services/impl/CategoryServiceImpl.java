package com.backend.blog.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.blog.entities.Category;
import com.backend.blog.exceptions.ResourceNotFoundException;
import com.backend.blog.payloads.CategoryDto;
import com.backend.blog.repositories.CategoryRepo;
import com.backend.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		// convert dto to category/entity
		Category category = this.dtoToCategory(categoryDto);
		CategoryDto savedCategoryDto = this.categoryToDto(this.categoryRepo.save(category));
		return savedCategoryDto;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) {
		// get category
		Category category = this.categoryRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
		
		CategoryDto categoryDto = this.categoryToDto(category);
		return categoryDto;
	}

	@Override
	public List<CategoryDto> getAllCategory() {

		List<Category> categories = this.categoryRepo.findAll();
		// convert each category into dto
		List<CategoryDto> categoriesDtos = categories.stream().map(cat -> this.categoryToDto(cat))
				.collect(Collectors.toList());
		
		return categoriesDtos;
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		//update category obj
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		//save in the db
		Category updatedCategory = this.categoryRepo.save(category);
		CategoryDto updatedCategoryDto = this.categoryToDto(updatedCategory);
		return updatedCategoryDto;
	}

	@Override
	public void deleteCategory(Integer id) {
		this.categoryRepo.deleteById(id);
	}

	public Category dtoToCategory(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		return category;
	}

	public CategoryDto categoryToDto(Category category) {
		return this.modelMapper.map(category, CategoryDto.class);
	}

}
