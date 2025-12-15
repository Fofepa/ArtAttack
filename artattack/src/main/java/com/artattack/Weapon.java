package com.artattack;

public class Weapon{
    //Attributes
    //private List<Move> moves;
    private final String name;
    private final String description;
    private int compatibility;


    //Constructor
    public Weapon(String name, String description, /*List<Move> moves,*/ int compatibility) {
        this.name = name;
        this.description = description;
        //this.moves = moves;
        this.compatibility = compatibility;
    }

    //Getters
   /*  public List<Move> getMoves() {
        return moves;
    } */

    public int getCharacterClass() {
        return this.compatibility;
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
