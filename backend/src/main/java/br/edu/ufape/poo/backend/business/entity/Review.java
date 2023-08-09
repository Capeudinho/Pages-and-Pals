package br.edu.ufape.poo.backend.business.entity;


import jakarta.persistence.Entity;

@Entity
public class Review extends Statement {
	
	private int bookScore;
	private boolean privacy;
	
	public int getBookScore() {
		return bookScore;
	}
	public void setBookScore(int bookScore) {
		this.bookScore = bookScore;
	}
	public boolean isPrivacy() {
		return privacy;
	}
	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}
	
	

}
