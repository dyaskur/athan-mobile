/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.SalaahCalc;

/**
 * @author BENBOUZID
 */
public class CalculationMethods {

    private final int value;

    private static final int Jafari_VAL = 0;
    private static final int Karachi_VAL = 1;
    private static final int ISNA_VAL = 2;
    private static final int MWL_VAL = 3;
    private static final int Makkah_VAL = 4;
    private static final int Egypt_VAL = 5;
    private static final int Custom_VAL = 6;

    /** L'attribut qui contient la valeur associé à l'enum */
    public static final CalculationMethods Jafari = new CalculationMethods(Jafari_VAL);
    public static final CalculationMethods Karachi = new CalculationMethods(Karachi_VAL);
    public static final CalculationMethods ISNA = new CalculationMethods(ISNA_VAL);
    public static final CalculationMethods MWL = new CalculationMethods(MWL_VAL);
    public static final CalculationMethods Makkah = new CalculationMethods(Makkah_VAL);
    public static final CalculationMethods Egypt = new CalculationMethods(Egypt_VAL);
    public static final CalculationMethods Custom = new CalculationMethods(Custom_VAL);

    /** Le constructeur qui associe une valeur à l'enum */
    private CalculationMethods(int value) {
        this.value = value;
    }

    /** La méthode accesseur qui renvoit la valeur de l'enum */
    public int getValue() {
        return this.value;
    }
}
