package com.backend.blog.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Getter
@Setter
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;
	
	@Column(name = "title")
	private String postTitle;
	
	@Column(name = "content")
	private String postContent;
	
	@Column(name = "image")
    private String postImage;
	
	@Column(name = "date")
	private Date postDate;

	@ManyToOne
	private User user;
	
    @ManyToOne
	private Category category;
    
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	

}
