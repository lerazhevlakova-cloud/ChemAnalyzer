package Substance;


import Element.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Substance{

    public String formula;
    private double molecularMass;
    private HashMap<String, Double> elementsAndIndexes = new HashMap<>();

    public double massFraction;
    public double mole;
    public Substance(String formula, double massFraction){
        this.formula = formula;
        this.massFraction = massFraction;
    }
    public void calculateMolecularMass()  {
        Pattern pattern = Pattern.compile("([A-Z][a-z]?)(\\d+(\\.\\d{1,4})?)?");
        Matcher matcher = pattern.matcher(formula);

        while (matcher.find()) {

            String symbol = matcher.group(1);
            String countStr = matcher.group(2);
            double count = (countStr == null || countStr.isEmpty()) ? 1.0 : Double.parseDouble(countStr);



            switch (symbol) {
                case "C" :{
                    Carbon carbon = new Carbon(count);
                    carbon.calculateMolarMass();

                    molecularMass += carbon.molarMass;
                    elementsAndIndexes.put("C", count);
                    break;

                }
                case "H" : {
                    Hydrogen hydrogen = new Hydrogen(count);
                    hydrogen.calculateMolarMass();
                    molecularMass += hydrogen.molarMass;
                    elementsAndIndexes.put("H", count);
                    break;

                }
                case "O" :{
                    Oxygen oxygen = new Oxygen(count);
                    oxygen.calculateMolarMass();
                    molecularMass += oxygen.molarMass;
                    elementsAndIndexes.put("O", count);
                    break;

                }
                case "N" : {
                    Nitrogen nitrogen = new Nitrogen(count);
                    nitrogen.calculateMolarMass();
                    molecularMass += nitrogen.molarMass;
                    elementsAndIndexes.put("N", count);
                    break;

                }
                case "Ca" : {
                    Calcium calcium = new Calcium(count, symbol);
                    calcium.calculateMolarMass();
                    molecularMass += calcium.molarMass;
                    elementsAndIndexes.put("Ca", count);
                    break;
                }
                case "Al" : {
                    Calcium calcium = new Calcium(count, symbol);
                    calcium.calculateMolarMass();
                    molecularMass += calcium.molarMass;
                    elementsAndIndexes.put("Al", count);
                    break;
                } case "Na" : {
                    Calcium calcium = new Calcium(count, symbol);
                    calcium.calculateMolarMass();
                    molecularMass += calcium.molarMass;
                    elementsAndIndexes.put("Na", count);
                    break;
                }

                default :
                    throw new IllegalArgumentException("Unsupported element: " + symbol);
            }

        }

    }

    public void calculateMole() throws ParseException {
        calculateMolecularMass();
        if (molecularMass == 0) {
            mole =0;
        }else{
            mole = (1000*(massFraction/100))/molecularMass;
            BigDecimal bd = new BigDecimal(mole);
            bd = bd.setScale(4, RoundingMode.HALF_DOWN);
            mole = bd.doubleValue();
        }


    }
    public double getMole(){
        return mole;
    }
    public double getIndexOfElement(String element){

        double result=0;
        if(elementsAndIndexes.get(element) == null){
            return 0;
        }else{
            result = elementsAndIndexes.get(element);
        }


        return result;
    }



}
