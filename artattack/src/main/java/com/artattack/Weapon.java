package com.artattack;

public class Weapon{
    //Attributes
    //private List<Move> moves;
    private final String name;
    private final String description;
    private Player characterClass;


    //Constructor
    public Weapon(String name, String description, /*List<Move> moves,*/ Player characterClass) {
        this.name = name;
        this.description = description;
        //this.moves = moves;
        this.characterClass = characterClass;
    }

    //Getters
   /*  public List<Move> getMoves() {
        return moves;
    } */

    public Player getCharacterClass() {
        return characterClass;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

   /*  //Methods
    @Override
    public int use() {
        return 0;
    }
} */
}
