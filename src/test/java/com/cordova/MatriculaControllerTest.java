package com.cordova;

import com.cordova.controller.MatriculaController;
import com.cordova.model.Course;
import com.cordova.model.Matricula;
import com.cordova.model.Student;
import com.cordova.repo.IMatriculaRepo;
import com.cordova.service.impl.MatriculaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = MatriculaController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(MatriculaServiceImpl.class)
class MatriculaControllerTest {

	@MockBean
	private IMatriculaRepo repo;

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

		Course course = new Course();
		course.setId("1");
		course.setName("Matematica");
		course.setAcronym("MAT");
		course.setStatus(true);

		List<Course> courseList = new ArrayList<>();
		courseList.add(course);

		Matricula matricula = new Matricula();
		matricula.setId("1");
		matricula.setStudent(student);
		matricula.setCourseList(courseList);
		matricula.setMatriculaDate(LocalDate.of(2022,04,27));

		List<Matricula> matriculaList = new ArrayList<>();
		matriculaList.add(matricula);

		Mockito.when(repo.findAll()).thenReturn(Flux.fromIterable(matriculaList));

		client.get()
				.uri("/matriculas")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Matricula.class)
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

		Course course = new Course();
		course.setId("1");
		course.setName("Matematica");
		course.setAcronym("MAT");
		course.setStatus(true);

		List<Course> courseList = new ArrayList<>();
		courseList.add(course);

		Matricula matricula = new Matricula();
		matricula.setId("1");
		matricula.setStudent(student);
		matricula.setCourseList(courseList);
		matricula.setMatriculaDate(LocalDate.of(2022,04,27));

		Mockito.when(repo.save(any())).thenReturn(Mono.just(matricula));

		client.post()
				.uri("/matriculas")
				.body(Mono.just(matricula), Matricula.class)
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

		Course course = new Course();
		course.setId("1");
		course.setName("Matematica");
		course.setAcronym("MAT");
		course.setStatus(true);

		List<Course> courseList = new ArrayList<>();
		courseList.add(course);

		Matricula matricula = new Matricula();
		matricula.setId("1");
		matricula.setStudent(student);
		matricula.setCourseList(courseList);
		matricula.setMatriculaDate(LocalDate.of(2022,04,27));

		Mockito.when(repo.findById("1")).thenReturn(Mono.just(matricula));
		Mockito.when(repo.save(any())).thenReturn(Mono.just(matricula));

		client.put()
				.uri("/matriculas/" + matricula.getId())
				.body(Mono.just(matricula), Matricula.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	@Test
	void removeTest() {

		Matricula matricula = new Matricula();
		matricula.setId("1");


		Mockito.when(repo.findById("1")).thenReturn(Mono.just(matricula));
		Mockito.when(repo.deleteById("1")).thenReturn(Mono.empty());

		client.delete()
				.uri("/matriculas/" + matricula.getId())
				.exchange()
				.expectStatus().isNoContent();
	}

}
