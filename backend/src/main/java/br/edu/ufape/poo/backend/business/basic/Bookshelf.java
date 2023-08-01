package br.edu.ufape.poo.backend.business.basic;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Bookshelf
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	@Column(columnDefinition = "TEXT")
	private String description;
	private List<String> bookApiIds;
	private LocalDate creationDate;
	private boolean privacy;
	@ManyToOne
	private Account owner;
	
	public long getId()
	{
		return this.id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public List<String> getBookApiIds()
	{
		return this.bookApiIds;
	}
	
	public void setBookApiIds(List<String> bookApiIds)
	{
		this.bookApiIds = bookApiIds;
	}
	
	public LocalDate getCreationDate()
	{
		return this.creationDate;
	}
	
	public void setCreationDate(LocalDate creationDate)
	{
		this.creationDate = creationDate;
	}
	
	public boolean isPrivacy()
	{
		return this.privacy;
	}
	
	public void setPrivacy(boolean privacy)
	{
		this.privacy = privacy;
	}
	
	public Account getOwner()
	{
		return this.owner;
	}
	
	public void setOwner(Account owner)
	{
		this.owner = owner;
	}
}