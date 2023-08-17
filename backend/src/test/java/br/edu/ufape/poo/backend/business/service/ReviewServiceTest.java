package br.edu.ufape.poo.backend.business.service;

import static org.junit.jupiter.api.Assertions.*;
import br.edu.ufape.poo.backend.business.service.ReviewService;
import br.edu.ufape.poo.backend.exceptions.InvalidBookScoreReviewException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.edu.ufape.poo.backend.business.entity.Review;

@SpringBootTest
@Transactional
class ReviewServiceTest {
	
	@Autowired
	private ReviewService reviewService;
	

	@Test
	void testBookScoreLessThanZero() {
		Review r;
		r = new Review();
		r.setBookScore(-2);
		
		Exception exception = assertThrows(InvalidBookScoreReviewException.class, () -> {
            reviewService.create(r);
        });

        assertEquals("invalid book score", exception.getMessage());
	}
	
	@Test
	void testBookScoreGreaterThanFive() {
		Review r;
		r = new Review();
		r.setBookScore(10.5);
		
		Exception exception = assertThrows(InvalidBookScoreReviewException.class, () -> {
            reviewService.create(r);
        });

        assertEquals("invalid book score", exception.getMessage());
	}
	
	@Test
	void testBookScoreDivisibleByMeans() {
		Review r;
		r = new Review();
		r.setBookScore(3.7);
		
		Exception exception = assertThrows(InvalidBookScoreReviewException.class, () -> {
            reviewService.create(r);
        });

        assertEquals("invalid book score", exception.getMessage());
	}

}
