package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static ru.hogwarts.school.controller.SchoolProjectTestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    private final Student newHarry = new Student(null, "Harry", 12, GRYFFINDOR);
    private final Student newHermione = new Student(null, "Hermione", 12, GRYFFINDOR);
    private final Student newRon = new Student(null,  "Ron", 13, GRYFFINDOR);
    private final Student newDraco = new Student(null, "Draco", 13, SLYTHERIN);
    private final Student newCrab = new Student(null, "Crab", 14, SLYTHERIN);
    private final Student newGoyle = new Student(null,  "Goyle", 13, SLYTHERIN);
    private final Student newCedric = new Student(null, "Cedric", 16, HUFFLEPUFF);
    private final Student newSusan = new Student(null,  "Susan", 12, HUFFLEPUFF);
    private final Student newZhou = new Student(null, "Zhou", 14, RAVENCLAW);
    private final Student newLuna = new Student(null,  "Luna", 12, RAVENCLAW);

    @LocalServerPort
    private int port;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void init() {

        studentService.createStudent(newHarry);
        studentService.createStudent(newHermione);
        studentService.createStudent(newRon);
        studentService.createStudent(newDraco);
        studentService.createStudent(newCrab);
        studentService.createStudent(newGoyle);
        studentService.createStudent(newCedric);
        studentService.createStudent(newSusan);
        studentService.createStudent(newZhou);
        studentService.createStudent(newLuna);

        ResponseEntity<List<Student>> responseList = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        List<Student> listResponseEntity = responseList.getBody();

        assert listResponseEntity != null;
        listResponseEntity.sort(Comparator.comparing(Student::getId));

        newHarry.setId(listResponseEntity.get(listResponseEntity.size() - 10).getId());
        newHermione.setId(listResponseEntity.get(listResponseEntity.size() - 9).getId());
        newRon.setId(listResponseEntity.get(listResponseEntity.size() - 8).getId());
        newDraco.setId(listResponseEntity.get(listResponseEntity.size() - 7).getId());
        newCrab.setId(listResponseEntity.get(listResponseEntity.size() - 6).getId());
        newGoyle.setId(listResponseEntity.get(listResponseEntity.size() - 5).getId());
        newCedric.setId(listResponseEntity.get(listResponseEntity.size() - 4).getId());
        newSusan.setId(listResponseEntity.get(listResponseEntity.size() - 3).getId());
        newZhou.setId(listResponseEntity.get(listResponseEntity.size() - 2).getId());
        newLuna.setId(listResponseEntity.get(listResponseEntity.size() - 1).getId());

    }

    @AfterEach
    void tearDown() {
        studentService.removeStudent(newHarry.getId());
        studentService.removeStudent(newHermione.getId());
        studentService.removeStudent(newRon.getId());
        studentService.removeStudent(newDraco.getId());
        studentService.removeStudent(newCrab.getId());
        studentService.removeStudent(newGoyle.getId());
        studentService.removeStudent(newCedric.getId());
        studentService.removeStudent(newSusan.getId());
        studentService.removeStudent(newZhou.getId());
        studentService.removeStudent(newLuna.getId());
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void getAllStudentsTest() {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotNull();

        List<Student> students = Collections.emptyList();

        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student", List.class))
                .isNotEqualTo(students);
    }

    @Test
    void getAllStudentsByAgeInTheIntervalTest() {

        List<Student> students = new ArrayList<>(List.of(newHarry,
                newHermione, newSusan, newLuna, newRon, newDraco,
                newGoyle));


        int min = 12;
        int max = 13;
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getByAgeInTheInterval?min={min}&max={max}", String.class, min, max))
                .isNotNull();

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/getByAgeInTheInterval?min={min}&max={max}"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, min, max);

        List<Student> listResponseEntity = response.getBody();

        Assertions.assertThat(listResponseEntity).containsAll(students);

    }

    @Test
    void getStudentTest() {
        long id = newRon.getId();
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", String.class, id))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", Student.class, id))
                .isEqualTo(newRon);

        id = newLuna.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", Student.class, id))
                .isEqualTo(newLuna);
    }

    @Test
    void getAllStudentsByAgeTest() {

        List<Student> students = new ArrayList<>(List.of(newCrab,
                newZhou));

        int ageForAllStudentTest = 14;
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getByAge/{age}", String.class, ageForAllStudentTest))
                .isNotNull();

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/getByAge/{age}"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, ageForAllStudentTest);

        List<Student> listResponseEntity = response.getBody();

        Assertions.assertThat(listResponseEntity).containsAll(students);

    }

    @Test
    void getFacultyTest() {
        long id = newHermione.getId();
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", String.class, id))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(GRYFFINDOR);

        id = newCedric.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(SchoolProjectTestConstants.HUFFLEPUFF);

        id = newDraco.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(SLYTHERIN);

        id = newZhou.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(SchoolProjectTestConstants.RAVENCLAW);
    }

    @Test
    void createStudentTest() {
        Student student = new Student(null, "Voldemort", 30, SLYTHERIN);

        Assertions
                .assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/student",
                        student, String.class))
                .isNotNull();

        ResponseEntity<List<Student>> responseList = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        List<Student> listResponseEntity = responseList.getBody();

        assert listResponseEntity != null;
        listResponseEntity.sort(Comparator.comparing(Student::getId));
        Assertions.assertThat(listResponseEntity.get(listResponseEntity.size() - 1).getName())
                .isEqualTo("Voldemort");

        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/{id}"), HttpMethod.DELETE, null,
                Student.class, listResponseEntity.get(listResponseEntity.size() - 1).getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void editStudentTest() {

        int ageForEditStudentTest = 17;
        newCedric.setAge(ageForEditStudentTest);

        HttpEntity<Student> entity = new HttpEntity<>(newCedric);

        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.PUT, entity,
                Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId())
                .isEqualTo(newCedric.getId());
        Assertions.assertThat(response.getBody().getName()).isEqualTo(newCedric.getName());
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(ageForEditStudentTest);

    }

    @Test
    void deleteStudentTest() {

        Student student = new Student(null, "Voldemort", 30, SLYTHERIN);

        Assertions
                .assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/student",
                        student, String.class))
                .isNotNull();

        ResponseEntity<List<Student>> responseList = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        List<Student> listResponseEntity = responseList.getBody();

        assert listResponseEntity != null;
        listResponseEntity.sort(Comparator.comparing(Student::getId));

        long id = listResponseEntity.get(listResponseEntity.size() - 1).getId();

        student.setId(id);

        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/{id}"), HttpMethod.DELETE, null,
                Student.class, id);


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        responseList = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        listResponseEntity = responseList.getBody();

        assert listResponseEntity != null;
        Assertions.assertThat(listResponseEntity).doesNotContain(student);
    }
}