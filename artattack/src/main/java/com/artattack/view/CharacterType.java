package com.artattack.view;

import com.artattack.mapelements.PlayerType;

public enum CharacterType {
    MUSICIAN("Zappa", "Master of rhythm", 20, 5, "Guitar"),
    DIRECTOR("Lynch", "Surreal visionary", 25, 3, "Camera");
    
    private PlayerType playerType;

    private final String name;
    private final String description;
    private final int maxHP;
    private final int speed;
    private final String weaponName;
    

    CharacterType(String name, String description, int maxHP, int speed, String weaponName) {
        this.name = name;
        this.description = description;
        this.maxHP = maxHP;
        this.speed = speed;
        this.weaponName = weaponName;
        this.playerType = name.equals("Zappa") ? PlayerType.MUSICIAN : PlayerType.MOVIE_DIRECTOR;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getMaxHP() { return maxHP; }
    public int getSpeed() { return speed; }
    public String getWeaponName() { return weaponName; }
    public PlayerType getPlayerType() {return playerType; }
}