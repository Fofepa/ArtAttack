package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;

public class AskFactory implements InteractionFactory {
    private String question;
    private List<String> options;
    private List<List<String>> answers;
    private List<Item> items;

    public AskFactory(String question, List<String> options, List<List<String>> answers, List<Item> items){
        if(question == null || question.isEmpty() || options == null)
            throw new IllegalArgumentException();
        this.question = question;
        this.options = options;
        this.answers = answers;
        this.items = items;
    }

    @Override
    public Interaction createInteraction(){
        return new Ask(null, this.question, this.options, this.answers, this.items);
    }
}