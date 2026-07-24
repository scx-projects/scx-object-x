package dev.scx.object.x.test;

import java.util.concurrent.atomic.AtomicInteger;

public class Student {

    @NewName("student_name")
    public String name;

    public AtomicInteger age = new AtomicInteger(22);

    public AtomicInteger ageNull = null;

    public byte[] byteData = "This is BYTE DATA !!!".getBytes();

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

}
