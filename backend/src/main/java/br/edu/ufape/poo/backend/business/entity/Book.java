package br.edu.ufape.poo.backend.business.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table
@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String apiId;
	private Double scoreTotal;
	private int reviewCount;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApiId() {
		return this.apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public Double getScoreTotal() {
		return this.scoreTotal;
	}

	public void setScoreTotal(Double scoreTotal) {
		this.scoreTotal = scoreTotal;
	}

	public int getReviewCount() {
		return this.reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
}