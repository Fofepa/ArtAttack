package com.artattack;

import java.util.List;

public class AskFactory implements InteractionFactory{
    private InteractionaPanel dialogPanel;
    private String question;
    private List<String> options;
    private List<String> answers;
    private List<Item> items;

    public AskFactory(InteractionPanel dialogPanel, String question, List<String> options, List<String> answers, List<Item> items){
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
