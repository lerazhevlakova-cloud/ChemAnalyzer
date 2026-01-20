import java.text.ParseException;
import Substance.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Solution {
    private double a;
    private double b;
    private double c;
    private double d;
    private double e;
    private double oxygenBalance;
    private double n1; // CO2
    private double n2; // H2O
    private double n3; // N2
    private double n4; // CO
    private double n5; // H2
    private double n6; // NO
    private double n7; //CaO or Al2O3
    private double n8; //O2
    private double n9; //C
    private double QInput;
    private double QProduct;
    private double Q;
    private double moleOfProducts;
    private double volumeOfProducts;
    final private static double enthalpyOfCO2 =395.4;
    final private static double enthalpyOfH2OAsGas = 240.5;
    final private static double enthalpyOfCO = 113.7;
    final private static double enthalpyOfNO = -90.3;
    final private static double enthalpyOfCaO = 630.9;
    private double enthalpyOfNH4NO3 = 0;
    private double enthalpyOfH2OAsWater = 0;
    private double enthalpyOfFuelOil =0;
    final private static double enthalpyOfAl2O3 = 1665.6;
    private double enthalpyOfAnyNitrate =0;
    final private static double enthalpyOfNa2O = 414.2;
    private static double n5Given;
    private static double k1;
    private static double k2;
    private boolean isCalcium;
    private  boolean isAluminium;
    private  boolean isNatrium;
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        JPanel tabPanel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        JPanel resultPanel = new JPanel(new BorderLayout());

        JButton calcButton = new JButton("Порахувати");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        JTextField[][] fields = new JTextField[4][3];
        JTabbedPane tabby = new JTabbedPane();
        for (int i = 0; i < 4; i++) {
            inputPanel.add(new JLabel("Ââåñòè ôîðìóëó êîìïîíåíòà:"));
            fields[i][0] = new JTextField();
            inputPanel.add(fields[i][0]);

            inputPanel.add(new JLabel("Ìàñîâà ÷àñòêà êîìïîíåíòà íà êã:"));
            fields[i][1] = new JTextField();
            inputPanel.add(fields[i][1]);

            inputPanel.add(new JLabel("Òåïëîòà óòâîðåííÿ ó êÄæ/ìîëü:"));
            fields[i][2] = new JTextField();
            inputPanel.add(fields[i][2]);
        }
        tabPanel.add(inputPanel, BorderLayout.CENTER);
        tabPanel.add(calcButton, BorderLayout.SOUTH);
        calcButton.setPreferredSize(new Dimension(1000, 300));
        tabby.add(tabPanel, "Ââåä³òü ñâî¿ äàí³");

        resultPanel.add(resultArea, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.SOUTH);
        tabby.add("Ðåçóëüòàò", resultPanel);

        frame.add(tabby);

        frame.setVisible(true);
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                Substance[] substances = new Substance[4];
                Solution solution = new Solution();
                for(int i =0; i < 4; i++){
                    String formula = fields[i][0].getText();
                    double massFraction = Double.parseDouble(fields[i][1].getText());
                    substances[i] = new Substance(formula, massFraction);
                    double enthalpy = Double.parseDouble(fields[i][2].getText());
                    solution.enthalpyOfNH4NO3 = i == 0 ? enthalpy : solution.enthalpyOfNH4NO3;
                    solution.enthalpyOfH2OAsWater = i==1 ? enthalpy : solution.enthalpyOfH2OAsWater;
                    solution.enthalpyOfFuelOil = i==2 ? enthalpy : solution.enthalpyOfFuelOil;
                    solution.enthalpyOfAnyNitrate = i == 3 ? enthalpy :solution.enthalpyOfAnyNitrate;
                    try {
                        substances[i].calculateMole();
                    } catch (ParseException ex) {
                        sb.append("Âèíèêëà ïîìèëêà. Ïåðåâ³ðòå äàí³. Ìîæëèâî, Âè äåñü íàïèñàëè êîìó, çàì³ñòü êðàïêè.");
                        throw new RuntimeException(ex);
                    }
                    sb.append(formula + ": ").append(substances[i].mole).append("\n");
                }

                AmmoniumNitrate ammoniumNitrate = new AmmoniumNitrate(substances[0].formula, substances[0].massFraction);
                Water water = new Water(substances[1].formula, substances[1].massFraction);
                FuelOil fuelOil = new FuelOil(substances[2].formula, substances[2].massFraction);
                AnyNitrate anyNitrate = new AnyNitrate(substances[3].formula, substances[3].massFraction);
                try {
                    ammoniumNitrate.calculateMole();
                    water.calculateMole();
                    fuelOil.calculateMole();
                    anyNitrate.calculateMole();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                solution.isCalcium = anyNitrate.formula.contains("Ca");
                solution.isAluminium = anyNitrate.formula.contains("Al");
                solution.isNatrium = anyNitrate.formula.contains("Na");

                sb.append("a: " + solution.calculateA(fuelOil, ammoniumNitrate, anyNitrate, water) + "\n")
                        .append("b: " +  solution.calculateB(fuelOil, ammoniumNitrate, anyNitrate, water) + "\n").append("c: " + solution.calculateC(fuelOil, ammoniumNitrate, anyNitrate, water) +"\n")
                                .append("d: " + solution.calculateD(fuelOil, ammoniumNitrate, anyNitrate, water) +"\n")
                                        .append("e: " + solution.calculateE(anyNitrate) +"\n");

                sb.append("Îêñèãåííèé áàëàíñ: " + solution.calculateOxygenBalance() +"%\n");
                sb.append(solution.calculateProductsOfReaction());
                sb.append(solution.calculateHeatOfDetonation(ammoniumNitrate, water, fuelOil, anyNitrate));
                resultArea.setText(sb.toString());
            }
        });

    }
    public double calculateA(FuelOil fuelOil, AmmoniumNitrate aN, AnyNitrate cN, Water water){
        a = fuelOil.getIndexOfElement("C") * fuelOil.mole + aN.getIndexOfElement("C") * aN.mole + cN.getIndexOfElement("C") * cN.mole + water.getIndexOfElement("C")*water.mole;
        return a;
    }
    public double calculateB(FuelOil fuelOil, AmmoniumNitrate aN, AnyNitrate cN, Water water){
        b = fuelOil.getIndexOfElement("H") * fuelOil.mole + aN.getIndexOfElement("H") * aN.mole + cN.getIndexOfElement("H") * cN.mole + water.getIndexOfElement("H")* water.mole;
        return b;
    }
    public double calculateC(FuelOil fuelOil, AmmoniumNitrate aN, AnyNitrate cN, Water water){
        c = fuelOil.getIndexOfElement("O") * fuelOil.mole + aN.getIndexOfElement("O") * aN.mole + cN.getIndexOfElement("O") * cN.mole + water.getIndexOfElement("O")* water.mole;
        return c;
    }
    public double calculateD(FuelOil fuelOil, AmmoniumNitrate aN, AnyNitrate cN, Water water){
        d = fuelOil.getIndexOfElement("N") * fuelOil.mole + aN.getIndexOfElement("N") * aN.mole + cN.getIndexOfElement("N") * cN.mole + water.getIndexOfElement("N")*water.mole;
        return d;
    }
    public double calculateE(AnyNitrate cN){
        e =  isCalcium ? cN.getIndexOfElement("Ca") * cN.mole : (isAluminium ? cN.getIndexOfElement("Al")*cN.getMole() : cN.getIndexOfElement("Na")*cN.getMole());
        return e;
    }
    public double calculateOxygenBalance(){
        double M = a*12 + b + 16*c + 14* d + (isCalcium ?  40*e : (isAluminium ? 27*e :(isNatrium ? 23*e : 0)));
        oxygenBalance = isAluminium ? ((c-(2*a + b/2 +1.5*e))/M)*16*100 : (isCalcium ? ((c-(2*a + b/2 + e))/M)*16*100 : (isNatrium ? ((c-(2*a + b/2 + e/2))/M)*16*100 : ((c-(2*a + b/2))/M)*16*100));
       return oxygenBalance;
    }
    public StringBuilder calculateProductsOfReaction()  {
        StringBuilder sb = new StringBuilder();
        if(oxygenBalance >= 0 || (Math.abs(Math.round(oxygenBalance)) < 3 && Math.abs(Math.round(oxygenBalance)) >=0)){
            if(e != 0){
                if(isCalcium){
                    n2 = b / 2; //H2O
                    n3 = d/2; //N2
                    n7 = e; //CaO
                    n1 = a; //CO2
                    if(c - n2 - n7 - 2*n1 <0 || (c - n2 - n7 - 2*n1) <0 && Math.abs(Math.round(c - n2 - n7 - 2*n1)) > 0.1){
                        n1 =  (c - n2 - n7)/2;//CO2
                        n9 = a - n1;//C
                    }
                    else{
                        n8 =  c - n2 - n7 - 2*n1;//O2
                    }
                    moleOfProducts = n1 + n2 + n8+n3;

                }else if(isAluminium){
                    n2 = b / 2; //H2O
                    n3 = d/2; //N2
                    n7 = e/2; //Al2O3

                    n1 = a; //CO2
                    if(c - n2 - 3*n7 - 2*n1 <0 || (c - n2 - 3*n7 - 2*n1) <0 && Math.abs(Math.round(c - n2 - 3*n7 - 2*n1)) > 0.1){
                        n1 =  (c - n2 - 3*n7)/2;//CO2
                        n9 = a - n1;//C
                    }else{
                        n8 =  c - n2 - 3*n7 - 2*n1;//O2
                    }
                    moleOfProducts = n1 + n2 + n3+ n8;

                } else if (isNatrium) {
                    n2 = b / 2; //H2O
                    n3 = d/2; //N2
                    n7 = e/2; //Na2O
                    n1 = a; //CO2
                     if((c - n2 - n7 - 2*n1) <0 || (c - n2 - n7 - 2*n1) <0 && Math.abs(Math.round(c - n2 - n7 - 2*n1)) > 0.1){
                        n1 =  (c - n2 - n7)/2;//CO2
                        n9 = a - n1;//C
                    }else{
                        n8 =  c - n2 - n7 - 2*n1;//O2
                    }
                     moleOfProducts = n1 + n2 + n3+ n8;
                }
            }else{
                n2 = b / 2; //H2O
                n3 = d/2; //N2
                n1 = a;//CO2
                n8 = c - n2 - 2*n1;//O2
                moleOfProducts = n1 + n2 + n3 + n8;
            }
        }else {
            if (e != 0.0) {
                if (isCalcium) {
                    if (c >= a + b / 2 + e) {
                        n2 = b / 2; //H2O
                        n3 = d / 2; //N2
                        n7 = e; //CaO
                        double z = c - n2 - n7 - a;
                        if(z - a > 0) {
                            n1 = z - a; //CO2
                            n4 = 2*a-z;//CO
                        }else{
                            n1=0;
                            n4 = c - n2 - n7;//CO
                            n9 = a-z;//C
                        }
                        moleOfProducts = n1 + n2 + n3 +n4;
                    } else {
                        n2 = b / 2; //H2O
                        n3 = d / 2; //N2
                        n7 = e; //CaO
                        double z = c - n2 - n7;
                        n4 = z; //CO
                        n9 = a - z; //C
                        moleOfProducts = n2 + n3;
                    }
                } else if (isAluminium) {
                    if(c >= a+b/2 + 1.5*e) {

                        n2 = b / 2; //H2O
                        n3 = d / 2; //N2
                        n7 = e / 2; //Al2O3

                        double z = c - n2 - n7 - a;
                        if(z - a > 0) {
                            n1 = z - a; //CO2
                            n4 = 2*a-z;//CO
                        }else{
                            n1=0;
                            n4 = c - n2 - 1.5*n7;//CO
                            n9 = a-z;//C
                        }
                        moleOfProducts = n1 + n2 + n3 +n4;
                    }else{
                        n2 = b / 2; //H2O
                        n3 = d / 2; //N2
                        n7 = e / 2; //Al2O3

                        double z = c - n2 - 1.5 * n7;
                        n4 = z; //CO
                        n9 = a - z; //C
                        moleOfProducts =  n2 + n3;
                    }
            } else if (isNatrium) {
                    if(c >= a+b/2 + e/2) {
                        n2 = b / 2; //H2O
                        n3 = d / 2; //N2
                        n7 = e/2; //Na2O

                        double z = c - n2 - n7 - a;
                        if(z - a > 0) {
                            n1 = z - a; //CO2
                            n4 = 2*a-z;//CO
                        }else{
                            n1=0;
                            n4 = c - n2 - n7;//CO
                            n9 = a-z;//C
                        }
                        moleOfProducts = n1 + n2 + n3+n4;
                    }else{
                        n2 = b / 2; //H2O
                        n3 = d / 2; //N2
                        n7 = e/2 ; //Na2O
                        double z = c - n2 - n7;
                        n4 = z; //CO
                        n9 = a - z; //C
                        moleOfProducts =n2 + n3 +n4;
                    }
                }
            } else {
                double A = (c/(2*a +b/2)) * 100;
                double K = 0.32* Math.pow(A, 0.24);
                if(A>100){
                    n1 = (1.4*K -0.4)*a; //CO2
                    n4 = a - n1; //CO
                    n2 = K*b/2; //H2O
                    n8 = (c - 2*n1 - n4 - n2)/2; //O2
                    n5 =(1-K)*b/2; //H2
                    n3 = d/2;//N2
                    n9=0;//C
                    moleOfProducts = n1+n4+n8+n2+n3+n5;
                }else{
                    if(c >(a+b/2)){
                        n1 = 0.7*(c-b/2)*K - 0.4*a;//CO2
                        n4 = 1.4*a -0.7*(c - b/2)*K;//CO
                        n3 = d/2;//N2
                        n2 = K*b/2;//H2O
                        n5 = (1-K)*b/2;//H2
                        n8 = (c - 2*n1 - n4 - n2)/2; //O2
                        moleOfProducts = n1+n2+n4+n3+n8+n5;
                    }else{
                        n2 = K*b/2;//H2O
                        n1 = 1.16*c*(K - 0.568) - 0.5*n2;//CO2
                        n4 = c-(2*n1+n2);//CO
                        n5 = (1-K)*b/2;//H2
                        n8 =0;//O2
                        n3 = d/2; //N2
                        n9 = a -(n1+n4);//C
                        moleOfProducts = n2 +n1+n4+n5+n9;
                    }
                }
            }
        }
        volumeOfProducts = moleOfProducts*22.4;
        sb.append(n1+"CO2 + "+n2+"H2O + "+n3+"N2 + "+n7+"CaO/Al2O3/Na2O + "+n4+"CO + "+n9+"C + "+ Math.round(n8) +"O2 + "+n5 +"H2"+"\n");
        sb.append("Ìîë³ ïðîäóêò³â ðåàö³¿: "+moleOfProducts + "\n");
        sb.append("Îá'ºì ãàç³â: "+volumeOfProducts + "\n");
        return sb;
    }
    public StringBuilder calculateHeatOfDetonation(AmmoniumNitrate aN, Water h2O, FuelOil fO, AnyNitrate cN){
        StringBuilder sb = new StringBuilder();
        QInput =  aN.getMole()*enthalpyOfNH4NO3 + h2O.getMole()*enthalpyOfH2OAsWater + fO.getMole()*enthalpyOfFuelOil + cN.getMole()*enthalpyOfAnyNitrate ;
        QProduct =  n1*enthalpyOfCO2 + n2*enthalpyOfH2OAsGas + n4*enthalpyOfCO;
        if(n7 !=0){
            QProduct+= isCalcium ? n7*enthalpyOfCaO : (isAluminium ? n7*enthalpyOfAl2O3 :(isNatrium ? n7*enthalpyOfNa2O: 0));
        }
        Q = QProduct-QInput;
        sb.append("Òåïëîòà âõ³äíèõ ïðîäóêò³â: "+ QInput + "." + " Òåïëîòà ïðîäóêò³â âèáóõó: " + QProduct + "\n");
        sb.append("Òåïëîòà âèáóõó â êÄæ/êã: "+Q);
        return sb;
    }
}

