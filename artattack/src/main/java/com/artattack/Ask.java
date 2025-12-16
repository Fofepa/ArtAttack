package com.artattack;

import java.util.List;

import com.artattack.view.InteractionPanel;

public class Ask implements Interaction {

    private InteractionPanel dialogPanel;
	private String question;
	private List<String> options;
	private List<List<String>> answers;
	private List<Item> items;
	
	public Ask(InteractionPanel dialogPanel, String question, List<String> options, List<List<String>> answers, List<Item> items){
		this.dialogPanel = dialogPanel;
		this.question = question;
		this.options = options;
		this.answers = answers;
		this.items = items;
	}
	
	@Override
	public void doInteraction(Player player){
		this.dialogPanel.showDialogWithChoice(
			this.question,
			this.options,
			choice -> handleChoice(choice, player)
		);
	}
	
	private void handleChoice(int choice, Player player){
		this.dialogPanel.showDialog(this.answers.get(choice));
		if(this.items != null)
			player.addItem(this.items.get(choice));
	}
}
