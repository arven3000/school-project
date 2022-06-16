package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FacultyControllerTest {

    private final long id = 1L;
    private final String name = "Example";
    private final String color = "purple";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvatarController avatarController;

    @MockBean
    private StudentController studentController;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void getAllFacultiesTest() throws Exception {
        when(facultyRepository.findAll()).thenReturn(List.of(SchoolProjectTestConstants.SLYTHERIN,
                SchoolProjectTestConstants.GRYFFINDOR, SchoolProjectTestConstants.RAVENCLAW,
                SchoolProjectTestConstants.HUFFLEPUFF));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(SchoolProjectTestConstants.SLYTHERIN.getId()))
                .andExpect(jsonPath("$[1].name").value(SchoolProjectTestConstants.GRYFFINDOR.getName()))
                .andExpect(jsonPath("$[2].color").value(SchoolProjectTestConstants.RAVENCLAW.getColor()))
                .andExpect(jsonPath("$[3].color").value(SchoolProjectTestConstants.HUFFLEPUFF.getColor()));

    }

    @Test
    void getAllFacultiesByColorOrNameTest() throws Exception {
        when(facultyRepository.findAllByColorOrNameIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(SchoolProjectTestConstants.GRYFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty//getAllFacultiesByColorOrName?example={example}",
                                SchoolProjectTestConstants.GRYFFINDOR.getColor())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(SchoolProjectTestConstants.GRYFFINDOR.getId()))
                .andExpect(jsonPath("$[0].name").value(SchoolProjectTestConstants.GRYFFINDOR.getName()))
                .andExpect(jsonPath("$[0].color").value(SchoolProjectTestConstants.GRYFFINDOR.getColor()));

        when(facultyRepository.findAllByColorOrNameIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(SchoolProjectTestConstants.RAVENCLAW));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/getAllFacultiesByColorOrName?example={example}",
                                SchoolProjectTestConstants.RAVENCLAW.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(SchoolProjectTestConstants.RAVENCLAW.getId()))
                .andExpect(jsonPath("$[0].name").value(SchoolProjectTestConstants.RAVENCLAW.getName()))
                .andExpect(jsonPath("$[0].color").value(SchoolProjectTestConstants.RAVENCLAW.getColor()));
    }

    @Test
    void getFacultyTest() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(SchoolProjectTestConstants.GRYFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", SchoolProjectTestConstants.GRYFFINDOR.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SchoolProjectTestConstants.GRYFFINDOR.getId()))
                .andExpect(jsonPath("$.name").value(SchoolProjectTestConstants.GRYFFINDOR.getName()))
                .andExpect(jsonPath("$.color").value(SchoolProjectTestConstants.GRYFFINDOR.getColor()));
    }

    @Test
    void getAllFacultiesByColorTest() throws Exception {
        when(facultyRepository.findAllByColor("blue"))
                .thenReturn(List.of(SchoolProjectTestConstants.RAVENCLAW));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findByColor/{color}", SchoolProjectTestConstants.RAVENCLAW.getColor())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(SchoolProjectTestConstants.RAVENCLAW.getId()))
                .andExpect(jsonPath("$[0].name").value(SchoolProjectTestConstants.RAVENCLAW.getName()))
                .andExpect(jsonPath("$[0].color").value(SchoolProjectTestConstants.RAVENCLAW.getColor()));

        when(facultyRepository.findAllByColor("yellow"))
                .thenReturn(List.of(SchoolProjectTestConstants.HUFFLEPUFF));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findByColor/{color}", SchoolProjectTestConstants.HUFFLEPUFF.getColor())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(SchoolProjectTestConstants.HUFFLEPUFF.getId()))
                .andExpect(jsonPath("$[0].name").value(SchoolProjectTestConstants.HUFFLEPUFF.getName()))
                .andExpect(jsonPath("$[0].color").value(SchoolProjectTestConstants.HUFFLEPUFF.getColor()));
    }

    @Test
    void getStudentsTest() throws Exception {

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(SchoolProjectTestConstants.HUFFLEPUFF));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/getStudents/{id}", SchoolProjectTestConstants.HUFFLEPUFF.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createFacultyTest() throws Exception {

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

    }

    @Test
    void editFacultyTest() throws Exception {

        String editName = "SLYTHERIN2";
        String editColor = "BLACK";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", SchoolProjectTestConstants.SLYTHERIN.getId());
        facultyObject.put("name", editName);
        facultyObject.put("color", editColor);

        Faculty faculty = new Faculty();
        faculty.setId(SchoolProjectTestConstants.SLYTHERIN.getId());
        faculty.setName(editName);
        faculty.setColor(editColor);

        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SchoolProjectTestConstants.SLYTHERIN.getId()))
                .andExpect(jsonPath("$.name").value(editName))
                .andExpect(jsonPath("$.color").value(editColor));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        doNothing().when(facultyRepository).deleteById(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}