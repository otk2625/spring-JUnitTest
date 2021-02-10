package com.cos.jTest.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jTest.domain.Book;
import com.cos.jTest.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookController {
	private final BookRepository bookRepository;
	
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book){
		return new ResponseEntity<>(bookRepository.save(book),HttpStatus.CREATED);
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable int id){
		return new ResponseEntity<>(bookRepository.findById(id),HttpStatus.OK);
	}
	
	@PutMapping("/book/{id}")
	public ResponseEntity<?> update(@PathVariable int id, @RequestBody Book book){
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요!!")); //영속화 (book 오브젝트) -> 영속성 컨텍스트보관
		//스프링내부 메모리공간에 따로 들고있음
		bookEntity.setTitle(book.getTitle());
		bookEntity.setPrice(book.getPrice());
		bookEntity.setRating(book.getRating());
		return new ResponseEntity<>(bookEntity,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable int id){
		bookRepository.deleteById(id);
		return new ResponseEntity<>("ok",HttpStatus.OK);
	}
}
