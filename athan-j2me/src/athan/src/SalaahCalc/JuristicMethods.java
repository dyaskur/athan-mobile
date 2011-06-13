/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.SalaahCalc;

/**
 * @author BENBOUZID
 */
public class JuristicMethods {

    private final int value;

    private static final int Shafii_VAL = 0;
    private static final int Hanafi_VAL = 1;

    /** L'attribut qui contient la valeur associé à l'enum */
    public static final JuristicMethods Shafii = new JuristicMethods(Shafii_VAL);
    public static final JuristicMethods Hanafi = new JuristicMethods(Hanafi_VAL);

    /** Le constructeur qui associe une valeur à l'enum */
    public JuristicMethods(int value) {
        this.value = value;
    }

    /** La méthode accesseur qui renvoit la valeur de l'enum */
    public int getValue() {
        return this.value;
    }
}
