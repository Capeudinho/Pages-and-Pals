package br.edu.ufape.poo.backend.business.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import br.edu.ufape.poo.backend.business.entity.Review;
import br.edu.ufape.poo.backend.data.ReviewRepository;
import br.edu.ufape.poo.backend.exceptions.InvalidBookScoreReviewException;


@Service
@Transactional
public class ReviewService implements ReviewServiceInterface {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public Review create(Review review)throws Exception{

		if (review.getBookScore()<0 || review.getBookScore()>5 || review.getBookScore()% 0.5 != 0)
		{
			throw new InvalidBookScoreReviewException();
		}

		review.setCreationDate(LocalDate.now());
		Review newReview = reviewRepository.save(review);
		return newReview;
	}
}