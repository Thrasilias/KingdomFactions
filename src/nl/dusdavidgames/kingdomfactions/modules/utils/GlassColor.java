package nl.dusdavidgames.kingdomfactions.modules.utils;

import java.util.Random;

public class GlassColor {

    public static final int WHITE = 0;
    public static final int ORANGE = 1;
    public static final int MAGENTA = 2;
    public static final int LIGHT_BLUE = 3;
    public static final int YELLOW = 4;
    public static final int LIME = 5;
    public static final int PINK = 6;
    public static final int GRAY = 7;
    public static final int LIGHT_GRAY = 8;
    public static final int CYAN = 9;
    public static final int PURPLE = 10;
    public static final int BLUE = 11;
    public static final int BROWN = 12;
    public static final int GREEN = 13;
    public static final int RED = 14;
    public static final int BLACK = 15;

    // Method to generate a random color
    public static int getRandomColor() {
        return new Random().nextInt(16); // Generates a random number from 0 to 15
    }

    // Method to get color name as a string
    public static String getColorName(int color) {
        switch (color) {
            case WHITE: return "White";
            case ORANGE: return "Orange";
            case MAGENTA: return "Magenta";
            case LIGHT_BLUE: return "Light Blue";
            case YELLOW: return "Yellow";
            case LIME: return "Lime";
            case PINK: return "Pink";
            case GRAY: return "Gray";
            case LIGHT_GRAY: return "Light Gray";
            case CYAN: return "Cyan";
            case PURPLE: return "Purple";
            case BLUE: return "Blue";
            case BROWN: return "Brown";
            case GREEN: return "Green";
            case RED: return "Red";
            case BLACK: return "Black";
            default: return "Unknown";
        }
    }
}
