package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }


    @GetMapping("/get-by-age-in-iheInterval")
    public Collection<Student> getAllStudentsByAgeInTheInterval(@RequestParam int min,
                                                                @RequestParam int max) {
        return studentService.getAllByAgeBetweenOrderByAge(min, max);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        return studentService.findStudent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get-by-age/{age}")
    public ResponseEntity<Collection<Student>> getAllStudentsByAge(@PathVariable int age) {
        Collection<Student> students = studentService.getAllStudentsByAge(age);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/get-faculty/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        return studentService.findStudent(id)
                .map(s -> ResponseEntity.ok(s.getFaculty()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number-of-all-students")
    public ResponseEntity<Integer> getNumberOfAllStudents() {
        return ResponseEntity.ok(studentService.getCountOfAllStudent());
    }

    @GetMapping("/avg-age-students")
    public ResponseEntity<Double> getAvgAgeStudents() {
        return ResponseEntity.ok(studentService.getAvgAgeStudents());
    }

    @GetMapping("/the-last-five-students")
    public ResponseEntity<Collection<Student>> getTheLastFiveStudents() {
        return ResponseEntity.ok(studentService.getTheLastFiveStudents());
    }


    @GetMapping("/all-students-whose-name-begins-with-a")
    public ResponseEntity<Collection<String>> getAllStudentNameStartWithA() {
        return ResponseEntity.ok(studentService.getAllStudentNameStartWithA());
    }

    @GetMapping("/avg-age-students-by-stream-api")
    public ResponseEntity<Double> getAvgAgeStudentsByStreamApi() {
        return ResponseEntity.ok(studentService.getAvgAgeStudentsByStreamApi());
    }

    @GetMapping("/integer")
    public ResponseEntity<Integer> getInteger() {
        return ResponseEntity.ok(studentService.getInteger());

    @GetMapping("/students-threads")
    public Collection<Student> getAllStudentsThreads() {
        return studentService.getAllStudentsThreads();
    }

    @GetMapping("/students-threads-synchronized")
    public Collection<Student> getAllStudentsSynchronizedThreads() {
        return studentService.getAllStudentsSynchronizedThreads();

    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student updateStudent = studentService.updateStudent(student);
        if (updateStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        return studentService.findStudent(id)
                .map(f -> {
                    studentService.removeStudent(id);
                    return ResponseEntity.ok(f);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
