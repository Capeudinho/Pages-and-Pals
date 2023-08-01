package br.edu.ufape.poo.backend.business.basic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String apiId;
	private int scoreTotal;
	private int reviewCount;
	
	public long getId()
	{
		return this.id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getApiId()
	{
		return this.apiId;
	}
	
	public void setApiId(String apiId)
	{
		this.apiId = apiId;
	}
	
	public int getScoreTotal()
	{
		return this.scoreTotal;
	}
	
	public void setScoreTotal(int scoreTotal)
	{
		this.scoreTotal = scoreTotal;
	}
	
	public int getReviewCount()
	{
		return this.reviewCount;
	}
	
	public void setReviewCount(int reviewCount)
	{
		this.reviewCount = reviewCount;
	}
	
	
}