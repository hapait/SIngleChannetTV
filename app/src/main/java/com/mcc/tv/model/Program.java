package com.mcc.tv.model;

public class Program {
    private int id;
    private String name, time;

    public Program(int id, String name, String time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

}