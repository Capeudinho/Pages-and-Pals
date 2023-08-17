package br.edu.ufape.poo.backend.business.entity;


import jakarta.persistence.Entity;

@Entity
public class Review extends Statement {
	
	private double bookScore;
	private boolean privacy;
	private String bookApiId;
	
	public String getBookApiId() {
		return bookApiId;
	}
	public void setBookApiId(String bookApiId) {
		this.bookApiId = bookApiId;
	}
	public double getBookScore() {
		return bookScore;
	}
	public void setBookScore(double bookScore) {
		this.bookScore = bookScore;
	}
	public boolean isPrivacy() {
		return privacy;
	}
	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}
	

}
