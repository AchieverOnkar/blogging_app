package com.backend.blog.entities;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer categoryID;
  @Column(name = "title",length = 100)
  private String categoryTitle;
  @Column(name = "description")
  private String categoryDescription;
  
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch  = FetchType.LAZY)
  private List<Post> posts = new ArrayList<>();
}
