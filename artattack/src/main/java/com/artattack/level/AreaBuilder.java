package com.artattack.level;

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
            case "8" -> {
                addShape("4");
                addShape("x", 1);
            }
            case "base" -> {
                addShape("8");
                this.area.addAll(
                    List.of(new Coordinates(-2, 0),
                            new Coordinates(0, -2),
                            new Coordinates(2, 0),
                            new Coordinates(0, 2)));
            }
            default -> {}
        }
    }

    public void addShape(String shape, int size) {
        switch (shape) {
            case "+" -> {
                for (int i = size; i > 0; i--) {
                    this.area.addAll(
                            List.of(new Coordinates(-i, 0), //left arm
                                    new Coordinates(0, i), //top arm
                                    new Coordinates(i, 0), //right arm
                                    new Coordinates(0, -i))); //bottom arm
                }
            }
            case "x" -> {
                for (int i = size; i > 0; i--) {
                    this.area.addAll(
                            List.of(new Coordinates(-i, -i), //bottom-left arm
                                    new Coordinates(-i, i), //top-left arm
                                    new Coordinates(i, -i), //bottom-right arm
                                    new Coordinates(i, i))); //top-right arm
                }
            }
            default -> {}
        }
    }

    public void addShape(String shape, int size, boolean fill) {
        switch (shape) {
            case "diamond" -> {
                if (fill && size > 1) {
                    addShape("diamond", size - 1, true);
                }
                for (int i = size; i > 0; i--) {
                    this.area.addAll(
                            List.of(new Coordinates(i - size, -i), //bottom-left side
                                    new Coordinates(-i, size - i), //top-left side
                                    new Coordinates(i, i - size), //bottom-right side
                                    new Coordinates(size - i, i))); //top-right side
                }
            }
            case "square" -> {
                if (fill && size > 1) {
                    addShape("square", size - 1, true);
                }
                for (int i = size; i > -size; i--) {
                    this.area.addAll(
                            List.of(new Coordinates(-size, -i), //left side
                                    new Coordinates(-i, size), //top side
                                    new Coordinates(size, i), //right side
                                    new Coordinates(i, -size))); //bottom side
                }
            }
            case "circle" -> {
                if (fill && size > 1) {
                    addShape("circle", size - 1, true);
                }
                this.area.addAll(
                        List.of(new Coordinates(-size, 0), //left
                                new Coordinates(0, size), //top
                                new Coordinates(size, 0), //right
                                new Coordinates(0, -size))); //bottom
                for (int i = size; i > 0; i--) {
                    this.area.addAll(
                            List.of(new Coordinates(i - size - 1, -i), //bottom-left side
                                    new Coordinates(-i, size + 1 - i), //top-left side
                                    new Coordinates(i, i - size - 1), //bottom-right side
                                    new Coordinates(size + 1 - i, i))); //top-right side
                }
            }
            default -> {}
        }
    }

    public List<Coordinates> getResult() {
        List<Coordinates> newMA = this.area.stream()
                .distinct()
                .toList();
        List<Coordinates> product = newMA;
        reset();
        return product;
    }
}
