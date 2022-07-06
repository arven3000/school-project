package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Request to create a new student");
        student.setId(null);
        studentRepository.save(student);
        logger.info("Create a new student completed successfully");
        return student;
    }

    @Override
    public Optional<Student> findStudent(long id) {
        logger.info("Student search request by id {}", id);
        Optional<Student> student = studentRepository.findById(id);
        logger.info("Student search request by id {} completed successfully", id);
        return student;
    }

    @Override
    public Student updateStudent(Student student) {
        logger.info("Request to update a student with id {}", student.getId());
        studentRepository.save(student);
        logger.info("Update a student with id {} completed successfully", student.getId());
        return student;
    }

    @Override
    public void removeStudent(long id) {
        logger.info("Request to delete a student with id {}", id);
        studentRepository.deleteById(id);
        logger.info("Delete a student with id {} completed successfully", id);
    }

    @Override
    public Collection<Student> getAllStudents() {
        logger.info("Request to get all students");
        Collection<Student> students = studentRepository.findAll();
        logger.info("Get all students completed successfully");
        return students;
    }

    @Override
    public Collection<Student> getAllStudentsByAge(int age) {
        logger.info("Request to get all students by age");
        Collection<Student> students = studentRepository.findAllByAge(age);
        logger.info("Get all students by age completed successfully");
        return students;
    }

    @Override
    public Collection<Student> getAllByAgeBetweenOrderByAge(int min, int max) {
        logger.info("Request to get all students by age between min {} and max {}", min, max);
        Collection<Student> students = studentRepository.findAllByAgeBetweenOrderByAge(min, max);
        logger.info("Get all students by age between min {} and max {} completed successfully", min, max);
        return students;
    }

    @Override
    public Integer getCountOfAllStudent() {
        logger.info("Request to get count of all students");
        Integer count = studentRepository.getCountOfStudents();
        logger.info("Get count of all students completed successfully");
        return count;
    }

    @Override
    public Double getAvgAgeStudents() {
        logger.info("Request to get average grade of all students");
        Double avgAge = studentRepository.getAvgAgeStudents();
        logger.info("Get average grade of all students completed successfully");
        return avgAge;
    }

    @Override
    public Collection<Student> getTheLastFiveStudents() {
        logger.info("Request to get the last five students");
        Collection<Student> students = studentRepository.getTheLastFiveStudents();
        logger.info("Get the last five students completed successfully");
        return students;
    }
}
