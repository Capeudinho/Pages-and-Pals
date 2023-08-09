package br.edu.ufape.poo.backend.business.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Statement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	@Column(columnDefinition = "TEXT")
	private String text;
	private int scoreTotal;
	private LocalDate creationDate;
	private LocalDate editionDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getScoreTotal() {
		return scoreTotal;
	}
	public void setScoreTotal(int scoreTotal) {
		this.scoreTotal = scoreTotal;
	}
	public LocalDate getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDate getEditionDate() {
		return editionDate;
	}
	public void setEditionDate(LocalDate editionDate) {
		this.editionDate = editionDate;
	}

}