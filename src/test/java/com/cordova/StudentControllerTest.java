package com.cordova;

import com.cordova.controller.StudentController;
import com.cordova.model.Student;
import com.cordova.repo.IStudentRepo;
import com.cordova.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = StudentController.class)
@Import(StudentServiceImpl.class)
class StudentControllerTest {

	@MockBean
	private IStudentRepo repo;

	@MockBean
	private Resources resources;

	@Autowired
	private WebTestClient client;

	@Test
	void findAllTest() {
		Student student = new Student();
		student.setId("1");
		student.setNames("Yenuri");
		student.setLastNames("Cordova");
		student.setDni("70099876");
		student.setAge(27);

		List<Student> list = new ArrayList<>();
		list.add(student);

		Mockito.when(repo.findAll()).thenReturn(Flux.fromIterable(list));

		client.get()
				.uri("/students")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Student.class)
				.hasSize(1);
	}

	@Test
	void registerTest() {
		Student student = new Student();
		student.setId("1");
		student.setNames("Yenuri");
		student.setLastNames("Cordova");
		student.setDni("70099876");
		student.setAge(27);

		Mockito.when(repo.save(any())).thenReturn(Mono.just(student));

		client.post()
				.uri("/students")
				.body(Mono.just(student), Student.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}


	@Test
	void modifyTest() {
		Student student = new Student();
		student.setId("1");
		student.setNames("Yenuri");
		student.setLastNames("Cordova");
		student.setDni("70099876");
		student.setAge(27);

		Mockito.when(repo.findById("1")).thenReturn(Mono.just(student));
		Mockito.when(repo.save(any())).thenReturn(Mono.just(student));

		client.put()
				.uri("/students/" + student.getId())
				.body(Mono.just(student), Student.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	@Test
	void removeTest() {
		Student student = new Student();
		student.setId("1");

		Mockito.when(repo.findById("1")).thenReturn(Mono.just(student));
		Mockito.when(repo.deleteById("1")).thenReturn(Mono.empty());

		client.delete()
				.uri("/students/" + student.getId())
				.exchange()
				.expectStatus().isNoContent();
	}

}
