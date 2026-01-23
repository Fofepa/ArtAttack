package com.artattack.view;

import java.util.List;
import java.util.function.Consumer;
import com.artattack.level.Maps;

public interface UIManager {
    public void showDialog(List<String> messages);
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback);
    public void loadSprite(String spritePath);
    public void switchMap(Maps map);
}
