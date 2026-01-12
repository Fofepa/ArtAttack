package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.view.InteractionPanel;

public class AskFactory implements InteractionFactory{
    private InteractionPanel dialogPanel;
    private String question;
    private List<String> options;
    private List<List<String>> answers;
    private List<Item> items;

    public AskFactory(InteractionPanel dialogPanel, String question, List<String> options, List<List<String>> answers, List<Item> items){
        if(dialogPanel == null || question == null ||  question.isEmpty() || options == null)
            throw new IllegalArgumentException();
        this.dialogPanel = dialogPanel;
        this.question = question;
        this.answers = answers;
        this.items = items;
    }

    @Override
    public Interaction createInteraction(){
        return new Ask(this.dialogPanel, this.question, this.options, this.answers, this.items);
    }
}
