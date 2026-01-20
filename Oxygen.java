package Element;

public class Oxygen extends Element{
    final private double atomicMass = 16;
    final String symbol = "O";
    private double index;
    public void calculateMolarMass(){
        molarMass = index*atomicMass;
    }
    public Oxygen(double index) {
        this.index = index;
    }

}
