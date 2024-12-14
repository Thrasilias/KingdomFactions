package nl.dusdavidgames.kingdomfactions.modules.monster;

public enum GuardType {

    SKELETON(20, 5),  // Skeleton with 20 health and 5 damage
    ZOMBIE(30, 4);    // Zombie with 30 health and 4 damage

    private final int health;
    private final int damage;

    // Constructor to initialize health and damage values
    GuardType(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    // Getters for health and damage
    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }
}
