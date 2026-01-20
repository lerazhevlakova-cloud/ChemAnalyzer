package Element;

public class Element {
    private double index;
    private String symbol;
    private double atomicMass;
    public double molarMass;
    public Element(double index){
        this.index = index;
    }

    public Element() {
    }

    public void calculateMolarMass(){
        molarMass = index*atomicMass;
    }





}
