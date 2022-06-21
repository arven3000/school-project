package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.Optional;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Optional<Faculty> findFaculty(long id);

    Faculty updateFaculty(Faculty faculty);

    void removeFaculty(long id);

    Collection<Faculty> getAllFaculties();

    Collection<Faculty> getFacultiesByColor(String color);

    Collection<Faculty> getFacultiesByColorOrName(String example);
}
