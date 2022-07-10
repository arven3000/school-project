package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Optional;

public interface StudentService {
    Student createStudent(Student student);

    Optional<Student> findStudent(long id);

    Student updateStudent(Student student);

    void removeStudent(long id);

    Collection<Student> getAllStudents();

    Collection<Student> getAllStudentsByAge(int age);

    Collection<Student> getAllByAgeBetweenOrderByAge(int min, int max);

    Integer getCountOfAllStudent();

    Double getAvgAgeStudents();

    Collection<Student> getTheLastFiveStudents();

    Collection<String> getAllStudentNameStartWithA();

    Double getAvgAgeStudentsByStreamApi();

    Integer getInteger();
}
