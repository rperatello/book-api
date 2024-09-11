package br.com.rperatello.book_api.data.vo.v1;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@JsonPropertyOrder({"id", "title", "mainGenre", "subGenre", "type", "currency", "price", "rating", "peopleRated", "url"})
public class BookResponseVO extends RepresentationModel<BookResponseVO>  implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long key;
	
	private String title;

	private String author;

	private String mainGenre;

	private String subGenre;

	private String type;

	private String currency;

	private double price;

	private double rating;

	private int peopleRated;	

	private String url;
	
	
	public BookResponseVO() {}
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public int getPeopleReted() {
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
		return Objects.hash(key, title, author, mainGenre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookResponseVO other = (BookResponseVO) obj;
		return Objects.equals(key, other.key) && Objects.equals(title, other.title) && Objects.equals(author, other.author) && Objects.equals(mainGenre, other.mainGenre);
	}

	
	
}


