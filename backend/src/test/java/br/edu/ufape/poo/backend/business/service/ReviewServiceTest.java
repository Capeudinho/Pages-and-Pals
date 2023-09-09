package br.edu.ufape.poo.backend.business.service;

import static org.junit.jupiter.api.Assertions.*;
import br.edu.ufape.poo.backend.data.AccountRepository;
import br.edu.ufape.poo.backend.data.ReviewRepository;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidBookScoreException;
import br.edu.ufape.poo.backend.exceptions.InvalidReviewException;
import br.edu.ufape.poo.backend.exceptions.InvalidTextException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.business.entity.Review;

@SpringBootTest
@Transactional
public class ReviewServiceTest {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private AccountRepository accountRepository;
	
	@Test
	void testCreate() {
		Review r = new Review();
		Account a = new Account();
		a = accountRepository.save(a);
		
		r.setBookScore(4.0);
		r.setText("aaaaaaaaaa");
		r.setOwner(a);
		
		try {
			r = reviewService.create(r);
		} catch (Exception e) {
			fail();
		}
		assertEquals(4.0, r.getBookScore());
	}
	
	@Test
	void testValidationReview() {
		Review r = new Review();
		Account a = new Account();
		a = accountRepository.save(a);
		r.setBookScore(null);
		r.setText("");
		r.setOwner(a);
		assertThrows(InvalidReviewException.class, () -> {
            reviewService.create(r);
        });
		
	}

	@Test
	void testBookScoreLessThanZero() {
		Review r = new Review();
		Account a = new Account();
		a = accountRepository.save(a);
		r.setBookScore(-2.0);
		r.setOwner(a);
		
		assertThrows(InvalidBookScoreException.class, () -> {
            reviewService.create(r);
        });
	}
	
	@Test
	void testBookScoreGreaterThanFive() {
		Review r = new Review();
		Account a = new Account();
		a = accountRepository.save(a);
		r.setBookScore(10.5);
		r.setOwner(a);
		
		assertThrows(InvalidBookScoreException.class, () -> {
            reviewService.create(r);
        });
	}
	
	@Test
	void testBookScoreDivisibleByMeans() {
		Review r = new Review();
		Account a = new Account();
		a = accountRepository.save(a);
		r.setBookScore(3.7);
		r.setOwner(a);
		
		assertThrows(InvalidBookScoreException.class, () -> {
            reviewService.create(r);
        });
	}
	
	@Test
	void testText() {
		Review r = new Review();
		Account a = new Account();
		a = accountRepository.save(a);
		r.setBookScore(5.0);
		r.setText(null);
		r.setOwner(a);
		
		assertThrows(InvalidTextException.class, () -> {
            reviewService.create(r);
        });
	}
	
	@Test
	void testFindById() {
		
		Review r = new Review();
		Account a = new Account();
		a = accountRepository.save(a);
		r.setText("text");
		r.setBookScore(4.5);
		r.setOwner(a);
		
		try {
			reviewService.create(r);
		} catch (Exception e) {
			fail();
		}
		
		assertThrows(IncorrectIdException.class, () -> {
            reviewService.findById(5515L);
        });
		
	}
	
	@Test
	void testEdit (){
		Review r;
		String text = "text";
		r = new Review();
		r.setText(text);
		r.setBookScore(4.5);
		
		reviewRepository.save(r);
	
		r.setText("aaaaaaaa");
		Review rUpdate = new Review();
		
		try {
			r = reviewService.update(r);
			rUpdate = reviewService.findById(r.getId());
		} catch (Exception e) {
			fail();
		}
		
		assertNotEquals(rUpdate.getText(), text);
	}
	
	@Test	
	void testDeleteById() {
		Review r;
		r = new Review();
		r.setBookScore(4.0);
		r.setText("aaaaaaa");
		long id = r.getId();
		
		reviewRepository.save(r);
		
		try {
			reviewService.deleteById(r.getId());
		} catch (Exception e) {
			fail();
		}
		
		assertThrows(IncorrectIdException.class, () -> {
			reviewService.findById(id);
        });
		
	}
	
	@Test
	void testCountByOwnerId () {
		int count;
		Account user;
		Review r1, r2, r3;
		
		user = new Account();
		
		user = accountRepository.save(user);
		
		r1 = new Review();
		r2 = new Review();
		r3 = new Review();
		
		r1.setOwner(user);
		r2.setOwner(user);
		r3.setOwner(user);
		
		reviewRepository.save(r1);
		reviewRepository.save(r2);
		reviewRepository.save(r3);
		
		count = reviewService.countByOwnerId(user.getId());
		
		assertEquals(count, 3);
	}
}
