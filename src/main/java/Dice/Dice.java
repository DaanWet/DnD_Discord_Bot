package Dice;

import java.util.Random;

public class Dice {

    private static Random random;
    private String image;

    private int max;

    public Dice(int max){
        random = new Random();
        this.max = max;
        this.image = String.format("https://www.dnddice.com/media/wysiwyg/d%d.jpg", (max != 100) ? max : "10_");
    }

    public int roll(){
        return random.nextInt(max) + 1;
    }

    public String getDescription(){
        return String.format("Rolls a d%d", max);
    }

    public String getName(){
        return String.format("d%d", max);
    }


    public String getImage(){
        return image;
    }
}
