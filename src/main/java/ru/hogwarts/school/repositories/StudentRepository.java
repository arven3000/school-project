package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findAllByAge(int age);

    Collection<Student> findAllByAgeBetweenOrderByAge(int min, int max);

    @Query(value = "SELECT COUNT(*) from students", nativeQuery = true)
    Integer getCountOfStudents();

    @Query(value = "SELECT AVG(age) from students", nativeQuery = true)
    Double getAvgAgeStudents();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> getTheLastFiveStudents();
}
