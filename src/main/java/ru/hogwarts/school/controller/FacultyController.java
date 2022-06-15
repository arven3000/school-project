package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/getAllFacultiesByColorOrName")
    public ResponseEntity<Collection<Faculty>> getAllFacultiesByColorOrName(@RequestParam String example) {
        Collection<Faculty> faculties = facultyService.getFacultiesByColorOrName(example);
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByColor/{color}")
    public ResponseEntity<Collection<Faculty>> getAllFacultiesByColor(@PathVariable String color) {
        Collection<Faculty> faculties = facultyService.getFacultiesByColor(color);
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/getStudents/{id}")
    public ResponseEntity<Set<Student>> getStudents(@PathVariable long id) {
        return facultyService.findFaculty(id).map(f -> ResponseEntity.ok(f.getStudents()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty updateFaculty = facultyService.updateFaculty(faculty);
        if (updateFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id)
                .map(f -> {
                    facultyService.removeFaculty(id);
                    return ResponseEntity.ok(f);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
