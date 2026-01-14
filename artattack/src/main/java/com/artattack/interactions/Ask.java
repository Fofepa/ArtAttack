package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public class Ask extends Interaction {
	
	private String question;
	private List<String> options;
	private List<List<String>> answers;
	private List<Item> items;
	
	public Ask(MainFrame mainFrame, String question, List<String> options, List<List<String>> answers, List<Item> items){
		super(mainFrame);
		this.question = question;
		this.options = options;
		this.answers = answers;
		this.items = items;
	}
	
	@Override
	public void doInteraction(Player player){
		if (getMainFrame() != null) {
			getMainFrame().showDialogWithChoice(
				this.question,
				this.options,
				choice -> handleChoice(choice, player)
			);
		}
	}
	
	private void handleChoice(int choice, Player player){
		System.out.println("Player chose option " + choice + ": " + options.get(choice));
		
		// Show the answer dialog
		if (getMainFrame() != null) {
			getMainFrame().showDialog(this.answers.get(choice));
		}
		
		// Give the item if there is one
		if(this.items != null && choice < this.items.size() && this.items.get(choice) != null) {
			player.addItem(this.items.get(choice));
			System.out.println("Player received: " + this.items.get(choice).getName());
		}
	}
}