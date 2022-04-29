package com.cordova;

import com.cordova.controller.CourseController;
import com.cordova.model.Course;
import com.cordova.repo.ICourseRepo;
import com.cordova.service.impl.CourseServiceImpl;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CourseController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(CourseServiceImpl.class)
class CourseControllerTest {

	@MockBean
	private ICourseRepo repo;

	@MockBean
	private Resources resources;

	@Autowired
	private WebTestClient client;

	@Test
	void findAllTest() {
		Course course = new Course();
		course.setId("1");
		course.setName("Matematica");
		course.setAcronym("MAT");
		course.setStatus(true);

		List<Course> list = new ArrayList<>();
		list.add(course);

		Mockito.when(repo.findAll()).thenReturn(Flux.fromIterable(list));

		client.get()
				.uri("/courses")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Course.class)
				.hasSize(1);
	}

	@Test
	void registerTest() {
		Course course = new Course();
		course.setId("1");
		course.setName("Matematica");
		course.setAcronym("MAT");
		course.setStatus(true);

		Mockito.when(repo.save(any())).thenReturn(Mono.just(course));

		client.post()
				.uri("/courses")
				.body(Mono.just(course), Course.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}


	@Test
	void modifyTest() {
		Course course = new Course();
		course.setId("1");
		course.setName("Matematica");
		course.setAcronym("MAT");
		course.setStatus(true);

		Mockito.when(repo.findById("1")).thenReturn(Mono.just(course));
		Mockito.when(repo.save(any())).thenReturn(Mono.just(course));

		client.put()
				.uri("/courses/" + course.getId())
				.body(Mono.just(course), Course.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	@Test
	void removeTest() {
		Course course = new Course();
		course.setId("1");

		Mockito.when(repo.findById("1")).thenReturn(Mono.just(course));
		Mockito.when(repo.deleteById("1")).thenReturn(Mono.empty());

		client.delete()
				.uri("/courses/" + course.getId())
				.exchange()
				.expectStatus().isNoContent();
	}

}
