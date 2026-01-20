package Element;

public class Hydrogen extends Element{
    final private double atomicMass = 1;
    final String symbol = "H";
    private double index;
    public void calculateMolarMass(){
        molarMass = index*atomicMass;
    }
    public Hydrogen(double index) {
        this.index = index;
    }
}
