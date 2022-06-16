package ru.hogwarts.school.controller;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

public class SchoolProjectTestConstants {

    public static final Faculty GRYFFINDOR = new Faculty(26L, "Gryffindor", "red");

    public static final Faculty SLYTHERIN = new Faculty(27L, "Slytherin", "green");

    public static final Faculty RAVENCLAW = new Faculty(28L, "Ravenclaw", "blue");

    public static final Faculty HUFFLEPUFF = new Faculty(29L, "Hufflepuff", "yellow");

//    public static final Faculty FACULTY = new Faculty();

    public static final Student HARRY = new Student(30L, "Harry", 12, GRYFFINDOR);

    public static final Student HERMIONE = new Student(31L, "Hermione", 12, GRYFFINDOR);

    public static final Student RON = new Student(32L, "Ron", 13, GRYFFINDOR);

    public static final Student DRACO = new Student(33L, "Draco", 13, SLYTHERIN);

    public static final Student CRAB = new Student(34L, "Crab", 14, SLYTHERIN);

    public static final Student GOYLE = new Student(35L, "Goyle", 13, SLYTHERIN);

    public static final Student CEDRIC = new Student(36L, "Cedric", 16, HUFFLEPUFF);

    public static final Student SUSAN = new Student(37L, "Susan", 12, HUFFLEPUFF);

    public static final Student ZHOU = new Student(38L, "Zhou", 14, RAVENCLAW);

    public static final Student LUNA = new Student(39L, "Luna", 12, RAVENCLAW);

}
