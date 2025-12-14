package com.artattack;

import java.util.*;

public interface InteractionFactory {
    public Interaction createInteraction(InteractionPanel dialogPanel, List<String> dialog, Item item);
    public Interaction createInteraction(InteractionPanel dialogPanel, List<String> dialog);
}
