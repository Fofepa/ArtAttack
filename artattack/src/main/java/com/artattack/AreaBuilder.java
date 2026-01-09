package com.artattack;

import java.util.ArrayList;
import java.util.List;

public class AreaBuilder {
    private List<Coordinates> area;

    public AreaBuilder() {
        this.area = new ArrayList<>();
    }

    public void reset() {
        this.area = new ArrayList<>();
    }

    public void addShape(String shape) {
        switch (shape) {
            case "4" -> this.area.addAll(
                List.of(new Coordinates(-1, 0),
                        new Coordinates(0, -1),
                        new Coordinates(1, 0),
                        new Coordinates(0, 1)));
            case "x" -> this.area.addAll(
                List.of(new Coordinates(-1, -1),
                        new Coordinates(-1, 1),
                        new Coordinates(1, -1),
                        new Coordinates(1, 1)));
            case "8" -> {
                addShape("4");
                addShape("x");
            }
        }
    }

    public List<Coordinates> getResult() {
        List<Coordinates> product = this.area;
        reset();
        return product;
    }
}
