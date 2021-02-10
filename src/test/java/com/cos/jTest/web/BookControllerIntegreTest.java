package com.cos.jTest.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.jTest.domain.Book;
import com.cos.jTest.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 통합 테스트 (모든 bean들을 똑같이 IoC올리고 테스트 하는것)
 * WebEnvironment.MOCK = 실제 톰켓을 올리는게 아니라, 다른톰켓으로 테스트
 * WebEnvironment.RANDOM_PORT = 실제 톰켓으로 테스트
 * @AutoConfigureMockMvc = MocMvc를 IoC에 등록해줌
 * @Transactional  = 각각의 테스트함수가 종료될 때마다 트랜잭션을 rollback해주는 어노테이션!!
*/

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // 실제 톰캣을 올리는게 아니라, 다른 톰캣으로 테스트
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BookRepository bookRepository;


	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	public void init() {

		entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
	}

	@Test
	public void save테스트() throws Exception {
		// given (테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링따라하기", 4.8, 50000);
		String content = new ObjectMapper().writeValueAsString(book);

		// when(테스트 실행)
		ResultActions resultAction = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then (검증)
		resultAction.andExpect(status().isCreated()).andExpect(jsonPath("$.title").value("스프링따라하기"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findById_테스트() throws Exception {
		// given
		int id = 1;

		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트따라하기", 4.8, 50000));
		books.add(new Book(null, "리액트따라하기", 3.0, 25000));
		books.add(new Book(null, "JUnit따라하기", 2.2, 15800));
		bookRepository.saveAll(books);

		// when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("스프링부트따라하기"))
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	public void update_테스트() throws Exception {
		// given
		int id = 1;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트따라하기", 4.8, 50000));
		books.add(new Book(null, "리액트따라하기", 3.0, 25000));
		books.add(new Book(null, "JUnit따라하기", 2.2, 15800));
		bookRepository.saveAll(books);

		Book book = new Book(null, "c++따라하기", 4.4, 550000);
		String content = new ObjectMapper().writeValueAsString(book);

		// when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}", id).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("c++따라하기"))
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	public void delete_테스트() throws Exception {
		// given
		int id = 1;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트따라하기", 4.8, 50000));
		books.add(new Book(null, "리액트따라하기", 3.0, 25000));
		books.add(new Book(null, "JUnit따라하기", 2.2, 15800));
		bookRepository.saveAll(books);

		// when
				ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$").value("ok"))
				.andDo(MockMvcResultHandlers.print());

	}
	
	
}
