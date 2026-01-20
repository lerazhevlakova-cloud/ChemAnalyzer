package Element;

public class Carbon extends Element {
    final private double atomicMass = 12;
    private double index;


    public Carbon(double index) {
        this.index = index;
    }
    public void calculateMolarMass(){
        molarMass = index*atomicMass;
    }
}
