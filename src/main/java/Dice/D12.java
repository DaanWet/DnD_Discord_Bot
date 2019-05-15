package Dice;

public class D12 extends Dice {

    public D12(){
        super();
        image = "https://www.dnddice.com/media/wysiwyg/d12.jpg";
    }

    @Override
    public int roll() {
        return super.random.nextInt(12) + 1;
    }

    @Override
    public String getDescription() {
        return "!d12: Roll a d12.";
    }
}
