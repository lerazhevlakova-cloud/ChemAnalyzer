package Element;

public class Calcium extends Element{
    final private double atomicMassOfCalcium = 40;
    final private double atomicMassOfAluminium = 27;
    final private double atomicMassOfNatrium = 23;

    public String symbol;
    private double index;
    public void calculateMolarMass(){
        if(symbol.equals("Ca")){
            molarMass = index*atomicMassOfCalcium;
        } else if (symbol.equals("Al")) {
            molarMass = index*atomicMassOfAluminium;
        } else if (symbol.equals("Na")) {
            molarMass = index*atomicMassOfNatrium;
        }

    }

    public Calcium(double index, String symbol) {
        this.index = index;
        this.symbol = symbol;
    }
}
