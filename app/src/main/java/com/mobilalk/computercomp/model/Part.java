package com.mobilalk.computercomp.model;

public class Part {
    private String name;
    private String description;
    private String webpage;

    public Part() {
    }

    public Part(String name, String description, String webpage) {
        this.name = name;
        this.description = description;
        this.webpage = webpage;
    }

    public String getDescription() {
        return description;
    }

    public String getWebpage() {
        return webpage;
    }

    public String getName() {
        return name;
    }
}
