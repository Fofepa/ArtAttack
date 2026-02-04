package com.artattack.saving;

import java.util.List;

import com.artattack.level.Coordinates;

public class MANODEData extends NodeData {
    private List<Coordinates> shape;

    public List<Coordinates> getShape() {
        return shape;
    }

    public void setShape(List<Coordinates> shape) {
        this.shape = shape;
    }

    
}
