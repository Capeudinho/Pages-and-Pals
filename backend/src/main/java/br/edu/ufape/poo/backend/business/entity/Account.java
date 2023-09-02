package br.edu.ufape.poo.backend.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String email;
	private String password;
	@Column(columnDefinition = "TEXT")
	private String biography;
	private boolean privacy;
	private String picture;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getBiography() {
		return this.biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public boolean isPrivacy() {
		return this.privacy;
	}

	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}