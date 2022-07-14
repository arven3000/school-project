package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public Collection<String> getAllStudentNameStartWithA() {
        return studentRepository.findAll().stream()
                .map(s -> s.getName().toUpperCase())
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Double getAvgAgeStudentsByStreamApi() {
        return studentRepository.findAll().stream()
                .collect(Collectors.averagingInt(Student::getAge));
    }

    @Override
    public Integer getInteger() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }

    @Override
    public Collection<Student> getAllStudentsThreads() {
        List<Student> students = studentRepository.findAll();

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + students.get(2).getName());
            System.out.println(Thread.currentThread().getName() + ": " + students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + students.get(4).getName());
            System.out.println(Thread.currentThread().getName() + ": " + students.get(5).getName());
        });

        System.out.println(Thread.currentThread().getName() + ": " + students.get(0).getName());
        System.out.println(Thread.currentThread().getName() + ": " + students.get(1).getName());

        thread1.start();
        thread2.start();
        return students;
    }

    @Override
    public Collection<Student> getAllStudentsSynchronizedThreads() {
        List<Student> students = studentRepository.findAll();

        Thread thread1 = new Thread(() -> printName(List.of(students.get(2), students.get(3))));

        Thread thread2 = new Thread(() -> printName(List.of(students.get(4), students.get(5))));

        printName(List.of(students.get(0), students.get(1)));

        thread1.start();
        thread2.start();

        return students;
    }

    private synchronized void printName(List<Student> students) {
        students.forEach(student -> System.out.println(Thread.currentThread().getName() + ": " + student.getName()));
    }
}
