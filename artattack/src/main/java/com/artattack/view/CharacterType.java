package com.artattack.view;

public enum CharacterType {
    // Definition: Name, Description, HP Max, Speed, Weapon
    MUSICIAN("Zappa", "Master of rhythm", 20, 5, "Guitar"),
    DIRECTOR("Lynch", "Surreal visionary", 25, 3, "Camera");

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
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getMaxHP() { return maxHP; }
    public int getSpeed() { return speed; }
    public String getWeaponName() { return weaponName; }
}