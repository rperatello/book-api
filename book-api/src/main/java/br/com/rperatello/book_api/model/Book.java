package br.com.rperatello.book_api.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_book")
public class Book implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "title", nullable = false, length = 500)
	private String title;
	
	@Column(name = "author", nullable = true, length = 500)
	private String author;
	
	@Column(name = "mainGenre", nullable = true)
	private String mainGenre;
	
	@Column(name = "subGenre", nullable = true)
	private String subGenre;
	
	@Column(name = "type", nullable = true)
	private String type;
	
	@Column(name = "currency", nullable = true, length = 10)
	private String currency;
	
	@Column(name = "price", nullable = true)
	private double price;
	
	@Column(name = "rating", nullable = true)
	private double rating;
	
	@Column(name = "peopleRated", nullable = true)
	private int peopleRated;	
	
	@Column(name = "url", nullable = true, length = 500)
	private String url;
	
	
	public Book() {}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMainGenre() {
		return mainGenre;
	}

	public void setMainGenre(String mainGenre) {
		this.mainGenre = mainGenre;
	}

	public String getSubGenre() {
		return subGenre;
	}

	public void setSubGenre(String subGenre) {
		this.subGenre = subGenre;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public int getPeopleRated() {
		return peopleRated;
	}

	public void setPeopleRated(int peopleRated) {
		this.peopleRated = peopleRated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, author, mainGenre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(id, other.id) && Objects.equals(title, other.title) && Objects.equals(author, other.author) && Objects.equals(mainGenre, other.mainGenre);
	}

	
	
}


