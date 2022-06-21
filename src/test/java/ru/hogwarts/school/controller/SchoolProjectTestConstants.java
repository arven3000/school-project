package ru.hogwarts.school.controller;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

public class SchoolProjectTestConstants {
    public static final Faculty GRYFFINDOR = new Faculty(111L, "Gryffindor", "red");
    public static final Faculty SLYTHERIN = new Faculty(222L, "Slytherin", "green");
    public static final Faculty RAVENCLAW = new Faculty(333L, "Ravenclaw", "blue");
    public static final Faculty HUFFLEPUFF = new Faculty(444L, "Hufflepuff", "yellow");
    public static final Student HARRY = new Student(111L, "Harry", 12, GRYFFINDOR);
    public static final Student HERMIONE = new Student(222L, "Hermione", 12, GRYFFINDOR);
    public static final Student RON = new Student(333L, "Ron", 13, GRYFFINDOR);
    public static final Student DRACO = new Student(444L, "Draco", 13, SLYTHERIN);
    public static final Student CRAB = new Student(555L, "Crab", 14, SLYTHERIN);
    public static final Student GOYLE = new Student(666L, "Goyle", 13, SLYTHERIN);
    public static final Student CEDRIC = new Student(777L, "Cedric", 16, HUFFLEPUFF);
    public static final Student SUSAN = new Student(888L, "Susan", 12, HUFFLEPUFF);
    public static final Student ZHOU = new Student(999L, "Zhou", 14, RAVENCLAW);
    public static final Student LUNA = new Student(1000L, "Luna", 12, RAVENCLAW);

}
