package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;
import com.artattack.view.MainFrame;

public class Ask extends Interaction {
	
	private String question;
	private List<String> options;
	private List<List<String>> answers;
	private List<Item> items;
	private GameContext gameContext;
	
	public Ask(MainFrame mainFrame, String question, List<String> options, List<List<String>> answers, List<Item> items){
		super(InteractionType.ASK);
		this.question = question;
		this.options = options;
		this.answers = answers;
		this.items = items;
		this.gameContext = null;
	}
	
	@Override
	public void doInteraction(GameContext gameContext, Player player, String spritePath){
		this.gameContext = gameContext;
		if (gameContext.getUiManager() != null) {
			gameContext.getUiManager().showDialogWithChoice(
				this.question,
				this.options,
				choice -> handleChoice(choice, player, spritePath),
				spritePath
			);
		}
	}
	
	private void handleChoice(int choice, Player player, String spritePath){
		System.out.println("Player chose option " + choice + ": " + options.get(choice));
		
		// Show the answer dialog
		if (this.gameContext.getMapManager() != null) {
			this.gameContext.getUiManager().showDialog(this.answers.get(choice), spritePath);
		}
		
		// Give the item if there is one
		if(this.items != null && choice < this.items.size() && this.items.get(choice) != null) {
			player.addItems(List.of(this.items.get(choice)));
			System.out.println("Player received: " + this.items.get(choice).getName());
		}
	}
}