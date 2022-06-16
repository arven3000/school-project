package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

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

        int max = 13;
        int min = 12;

        List<Student> students = new ArrayList<>(List.of(SchoolProjectTestConstants.HARRY,
                SchoolProjectTestConstants.HERMIONE,
                SchoolProjectTestConstants.SUSAN,
                SchoolProjectTestConstants.LUNA,
                SchoolProjectTestConstants.RON,
                SchoolProjectTestConstants.DRACO,
                SchoolProjectTestConstants.GOYLE));


        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getByAgeInTheInterval?min={min}&max={max}", String.class, min, max))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getByAgeInTheInterval?min={min}&max={max}", List.class, min, max))
                .hasSize(students.size());

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/getByAgeInTheInterval?min={min}&max={max}"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, min, max);

        List<Student> listResponseEntity = response.getBody();

        Assertions.assertThat(listResponseEntity).containsAll(students);

        assert listResponseEntity != null;
        listResponseEntity.sort(Comparator.comparing(Student::getName));
        students.sort(Comparator.comparing(Student::getName));
        assertArrayEquals(listResponseEntity.toArray(), students.toArray());

    }

    @Test
    void getStudentTest() {
        long id = 32L;
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", String.class, id))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", Student.class, id))
                .isEqualTo(SchoolProjectTestConstants.RON);

        id = 39L;

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", Student.class, id))
                .isEqualTo(SchoolProjectTestConstants.LUNA);
    }

    @Test
    void getAllStudentsByAgeTest() {

        int age = 14;

        List<Student> students = new ArrayList<>(List.of(SchoolProjectTestConstants.CRAB,
                SchoolProjectTestConstants.ZHOU));

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getByAge/{age}", String.class, age))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getByAge/{age}", List.class, age))
                .hasSize(students.size());

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/getByAge/{age}"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, age);

        List<Student> listResponseEntity = response.getBody();

        Assertions.assertThat(listResponseEntity).containsAll(students);

        assert listResponseEntity != null;
        listResponseEntity.sort(Comparator.comparing(Student::getName));
        students.sort(Comparator.comparing(Student::getName));
        assertArrayEquals(listResponseEntity.toArray(), students.toArray());

    }

    @Test
    void getFacultyTest() {
        long id = 32L;
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", String.class, id))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(SchoolProjectTestConstants.GRYFFINDOR);

        id = 36;

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(SchoolProjectTestConstants.HUFFLEPUFF);

        id = 33;

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(SchoolProjectTestConstants.SLYTHERIN);

        id = 38;

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/getFaculty/{id}", Faculty.class, id))
                .isEqualTo(SchoolProjectTestConstants.RAVENCLAW);
    }

    @Test
    void createStudentTest() {
        Student student = new Student(null, "Voldemort", 30, SchoolProjectTestConstants.SLYTHERIN);

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

    }

    @Test
    void editStudentTest() {

        int age = 17;

        SchoolProjectTestConstants.CEDRIC.setAge(age);

        HttpEntity<Student> entity = new HttpEntity<>(SchoolProjectTestConstants.CEDRIC);

        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.PUT, entity,
                Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(36L);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Cedric");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(age);

    }

    @Test
    void deleteStudentTest() {
        long id = 72L;

        ResponseEntity<Student> responseGet = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/{id}"), HttpMethod.GET, null,
                Student.class, id);

        Student student = responseGet.getBody();


        assert student != null;
        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/{id}"), HttpMethod.DELETE, null,
                Student.class, student.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<List<Student>> responseList = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        List<Student> listResponseEntity = responseList.getBody();

        assert listResponseEntity != null;
        Assertions.assertThat(listResponseEntity).doesNotContain(student);
    }
}