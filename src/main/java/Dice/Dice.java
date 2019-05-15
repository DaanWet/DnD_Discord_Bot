package Dice;

import java.util.Random;

public abstract class Dice {

    protected Random random;
    protected String image;

    public Dice(){
        random = new Random();
    }

    public abstract int roll();

    public abstract String getDescription();

    public String getImage(){
        return image;
    }
}
