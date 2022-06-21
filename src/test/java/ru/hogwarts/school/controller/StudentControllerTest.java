package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.hogwarts.school.controller.SchoolProjectTestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    @InjectMocks
    private StudentController studentController;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void init() {
        when(studentRepository.findById(111L)).thenReturn(Optional.of(HARRY));
        when(studentRepository.findById(222L)).thenReturn(Optional.of(HERMIONE));
        when(studentRepository.findById(333L)).thenReturn(Optional.of(RON));
        when(studentRepository.findById(444L)).thenReturn(Optional.of(DRACO));
        when(studentRepository.findById(555L)).thenReturn(Optional.of(CRAB));
        when(studentRepository.findById(666L)).thenReturn(Optional.of(GOYLE));
        when(studentRepository.findById(777L)).thenReturn(Optional.of(CEDRIC));
        when(studentRepository.findById(888L)).thenReturn(Optional.of(SUSAN));
        when(studentRepository.findById(999L)).thenReturn(Optional.of(ZHOU));
        when(studentRepository.findById(1000L)).thenReturn(Optional.of(LUNA));

        when(facultyRepository.findById(111L)).thenReturn(Optional.of(GRYFFINDOR));
        when(facultyRepository.findById(222L)).thenReturn(Optional.of(SLYTHERIN));
        when(facultyRepository.findById(333L)).thenReturn(Optional.of(RAVENCLAW));
        when(facultyRepository.findById(444L)).thenReturn(Optional.of(HUFFLEPUFF));

    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void getAllStudentsTest() {
        when(studentRepository.findAll()).thenReturn(List.of(HARRY, HERMIONE, RON, DRACO,
                CRAB, GOYLE, CEDRIC, SUSAN, ZHOU, LUNA));
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotNull();

        List<Student> students = Collections.emptyList();

        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student", List.class))
                .isNotEqualTo(students);

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(studentRepository, times(3)).findAll();

    }

    @Test
    void getAllStudentsByAgeInTheIntervalTest() {

        List<Student> students = new ArrayList<>(List.of(HARRY,
                HERMIONE, SUSAN, LUNA, RON, DRACO,
                GOYLE));

        int min = 12;
        int max = 13;

        when(studentRepository.findAllByAgeBetweenOrderByAge(min, max)).thenReturn(students);

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/get-by-age-in-iheInterval?min={min}&max={max}", String.class, min, max))
                .isNotNull();

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/get-by-age-in-iheInterval?min={min}&max={max}"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, min, max);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(studentRepository, times(2)).findAllByAgeBetweenOrderByAge(anyInt(), anyInt());

    }

    @Test
    void getStudentTest() {
        long id = RON.getId();
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", String.class, id))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", Student.class, id))
                .isEqualTo(RON);

        id = LUNA.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/{id}", Student.class, id))
                .isEqualTo(LUNA);
        verify(studentRepository, times(3)).findById(anyLong());

    }

    @Test
    void getAllStudentsByAgeTest() {

        List<Student> students = new ArrayList<>(List.of(CRAB,
                ZHOU));

        int ageForAllStudentTest = 14;

        when(studentRepository.findAllByAge(ageForAllStudentTest)).thenReturn(students);

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/get-by-age/{age}", String.class, ageForAllStudentTest))
                .isNotNull();

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/get-by-age/{age}"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, ageForAllStudentTest);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(studentRepository, times(2)).findAllByAge(anyInt());

    }

    @Test
    void getFacultyTest() {
        long id = HERMIONE.getId();
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/get-faculty/{id}", String.class, id))
                .isNotNull();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/get-faculty/{id}", Faculty.class, id))
                .isEqualTo(GRYFFINDOR);

        id = CEDRIC.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/get-faculty/{id}", Faculty.class, id))
                .isEqualTo(HUFFLEPUFF);

        id = DRACO.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/get-faculty/{id}", Faculty.class, id))
                .isEqualTo(SLYTHERIN);

        id = ZHOU.getId();

        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port
                        + "/student/get-faculty/{id}", Faculty.class, id))
                .isEqualTo(RAVENCLAW);

        verify(studentRepository, times(5)).findById(anyLong());

    }

    @Test
    void createStudentTest() {
        Student student = new Student(1111L, "Voldemort", 30, SLYTHERIN);
        when(studentRepository.save(Mockito.any())).thenReturn(student);
        Assertions
                .assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/student",
                        student, String.class))
                .isNotNull();

        when(studentRepository.findById(1111L)).thenReturn(Optional.of(student));

        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/{id}"), HttpMethod.GET, null,
                Student.class, 1111L);

        Student responseStudent = response.getBody();

        Assertions.assertThat(responseStudent.getName()).isEqualTo("Voldemort");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(studentRepository, times(1)).findById(anyLong());
        verify(studentRepository, times(1)).save(Mockito.any());

    }

    @Test
    void editStudentTest() {
        when(studentRepository.save(Mockito.any())).thenReturn(CEDRIC);
        int ageForEditStudentTest = 17;
        CEDRIC.setAge(ageForEditStudentTest);

        HttpEntity<Student> entity = new HttpEntity<>(CEDRIC);

        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student"), HttpMethod.PUT, entity,
                Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId())
                .isEqualTo(CEDRIC.getId());
        Assertions.assertThat(response.getBody().getName()).isEqualTo(CEDRIC.getName());
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(ageForEditStudentTest);

        verify(studentRepository, times(1)).save(Mockito.any());

    }

    @Test
    void deleteStudentTest() {

        Student student = new Student(1111L, "Voldemort", 30, SLYTHERIN);

        when(studentRepository.findById(1111L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(any(Long.class));

        ResponseEntity<Student> response = testRestTemplate.exchange(("http://localhost:" + port
                        + "/student/{id}"), HttpMethod.DELETE, null,
                Student.class, 1111L);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(studentRepository, times(1)).deleteById(1111L);

    }
}