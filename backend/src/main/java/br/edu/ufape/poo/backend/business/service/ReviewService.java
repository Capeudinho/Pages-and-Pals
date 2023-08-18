package br.edu.ufape.poo.backend.business.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import br.edu.ufape.poo.backend.business.entity.Review;
import br.edu.ufape.poo.backend.data.ReviewRepository;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdReviewException;
import br.edu.ufape.poo.backend.exceptions.InvalidBookScoreReviewException;
import br.edu.ufape.poo.backend.exceptions.InvalidReviewException;
import br.edu.ufape.poo.backend.exceptions.InvalidTextReviewException;
import br.edu.ufape.poo.backend.util.OffsetPageRequest;

@Service
@Transactional
public class ReviewService implements ReviewServiceInterface {

	@Autowired
	private ReviewRepository reviewRepository;

	public Review create(Review review) throws Exception {
		if(review.getBookScore() == null && (review.getText() == null || review.getText().isBlank())){
			throw new InvalidReviewException();
		}
		if (review.getBookScore() <= 0 || review.getBookScore() > 5 || review.getBookScore() % 0.5 != 0) {
			throw new InvalidBookScoreReviewException();
		}
		if (review.getText() == null) {
			throw new InvalidTextReviewException();
		}
		review.setCreationDate(LocalDate.now());
		Review newReview = reviewRepository.save(review);
		return newReview;
	}

	public Review findById(Long id) throws Exception {

		Review review = reviewRepository.findById(id).orElse(null);

		if (review == null) {
			throw new IncorrectIdReviewException();
		}

		return review;
	}
	
	//Buscar por Reviews de um usuário
	public List<Review> findByOwnerIdPaginate(Long id, int offset, int limit) {
		Pageable pageable = new OffsetPageRequest(offset, limit);
		List<Review> reviews = reviewRepository.findByOwnerIdOrderByCreationDateDesc(id, pageable);
		
		return reviews;
	}

	//Buscar por Reviews de um livro
	public List<Review> findByBookApiIdPaginate(String bookApiId, int offset, int limit){
		Pageable pageable = new OffsetPageRequest(offset, limit);
		List<Review> reviews = reviewRepository.findByBookApiIdOrderByCreationDateDesc(bookApiId, pageable);
		
		return reviews;
	}
	
	public Review edit(Review review) throws Exception {

		Review oldReview = findById(review.getId());

		review.setBookApiId(oldReview.getBookApiId());
		review.setCreationDate(oldReview.getCreationDate());
		review.setEditionDate(LocalDate.now());

		if(review.getBookScore() == null && (review.getText() == null || review.getText().isBlank())){
			throw new InvalidReviewException();
		}
		if (review.getBookScore() <= 0 || review.getBookScore() > 5 || review.getBookScore() % 0.5 != 0) {
			throw new InvalidBookScoreReviewException();
		}
		if (review.getText() == null) {
			throw new InvalidTextReviewException();
		}

		review = reviewRepository.save(review);

		return review;

	}

	public Review deleteById(Long id) throws Exception {

		Review review = reviewRepository.findById(id).orElse(null);

		if (review == null) {
			throw new IncorrectIdReviewException();
		}

		reviewRepository.delete(review);
		return review;
	}

	// Testar
	public int countByOwnerId(Long id) {
		int reviewCount = reviewRepository.countByOwnerId(id);
		return reviewCount;
	}
	
	//Testar
	public int countByBookApiId(String id) {
		int reviewCount = reviewRepository.countByBookApiId(id);
		return reviewCount;
	}


}