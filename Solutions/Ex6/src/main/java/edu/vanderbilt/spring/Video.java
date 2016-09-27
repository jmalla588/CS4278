package edu.vanderbilt.spring;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private Double size;
	private String genre;
	private String url;
	
	public String getURL() {
		return url;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Double getSize() {
		return size;
	}
	
	public String getGenre() {
		return genre;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSize(Double size) {
		this.size = size;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public void setURL(String URL) {
		this.url = URL;
	}
}
