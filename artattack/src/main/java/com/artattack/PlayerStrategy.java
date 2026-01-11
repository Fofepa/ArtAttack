package com.artattack;

import java.util.*;

public interface PlayerStrategy {
    public void execute(int dx, int dy);
    public int getType();
     
}