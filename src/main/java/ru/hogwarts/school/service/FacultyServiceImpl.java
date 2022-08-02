package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Request to create a new faculty");
        facultyRepository.save(faculty);
        logger.info("Create a new faculty completed successfully");
        return faculty;
    }

    @Override
    public Optional<Faculty> findFaculty(long id) {
        logger.info("Search request by faculty id {}", id);
        Optional<Faculty> faculty = facultyRepository.findById(id);
        logger.info("Search by faculty id {} completed successfully", id);
        return faculty;
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Request to update the faculty by id {}", faculty.getId());
        facultyRepository.save(faculty);
        logger.info("Update the faculty by id {} completed successfully", faculty.getId());
        return faculty;
    }

    @Override
    public void removeFaculty(long id) {
        logger.info("Request to delete a faculty with id {}", id);
        facultyRepository.deleteById(id);
        logger.info("Delete a faculty with id {} completed successfully", id);
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        logger.info("Request to get all faculties");
        Collection<Faculty> faculties = facultyRepository.findAll();
        logger.info("Get all faculties completed successfully");
        return faculties;
    }

    @Override
    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.info("Request to get all faculties by color {}", color);
        Collection<Faculty> faculties = facultyRepository.findAllByColor(color);
        logger.info("Get all faculties by color {} completed successfully", color);
        return faculties;
    }

    @Override
    public Collection<Faculty> getFacultiesByColorOrName(String example) {
        logger.info("Request to get all faculties by color or name {}", example);
        Collection<Faculty> faculties = facultyRepository
                .findAllByColorOrNameIgnoreCase(example, example);
        logger.info("Get all faculties by color or name {} completed successfully", example);
        return faculties;
    }

    @Override
    public Optional<String> getFacultyWithTheLongestName() {
        return facultyRepository.findAll().stream().map(Faculty::getName)
                .max(Comparator.comparingInt(String::length));
    }
}
