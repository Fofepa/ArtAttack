package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.mapelements.Player;
import com.artattack.view.InteractionPanel;
import com.artattack.view.MainFrame;

public class Ask extends Interaction {

   /*  private InteractionPanel dialogPanel; */
	private String question;
	private List<String> options;
	private List<List<String>> answers;
	private List<Item> items;
	
	public Ask(MainFrame mainFrame ,String question, List<String> options, List<List<String>> answers, List<Item> items){
		super(mainFrame);
		this.question = question;
		this.options = options;
		this.answers = answers;
		this.items = items;
	}
	
	@Override
	public void doInteraction(Player player){
		this.getMainFrame().showDialogWithChoice(
			this.question,
			this.options,
			choice -> handleChoice(choice, player)
		);
	}
	
	private void handleChoice(int choice, Player player){
		this.getMainFrame().showDialog(this.answers.get(choice));
		if(this.items != null)
			player.addItem(this.items.get(choice));
	}
}
