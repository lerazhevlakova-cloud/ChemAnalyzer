package Element;

public class Nitrogen extends Element{
    final private double atomicMass = 14;
    final String symbol = "N";
    private double index;
    public void calculateMolarMass(){
        molarMass = index*atomicMass;
    }

    public Nitrogen(double index) {
        this.index = index;
    }
}
