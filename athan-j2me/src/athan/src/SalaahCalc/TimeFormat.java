/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.SalaahCalc;

/**
 * @author BENBOUZID
 */
public class TimeFormat {

    public static final String TIME_SEPARATOR = ":";

    private final int value;

    protected static final int Time24 = 0;      // 24-hour format
    protected static final int Time12 = 1;      // 12-hour format

    /** L'attribut qui contient la valeur associ� � l'enum */
    public static final TimeFormat H24 = new TimeFormat(Time24);
    public static final TimeFormat H12 = new TimeFormat(Time12);

    /** Le constructeur qui associe une valeur � l'enum */
    private TimeFormat(int value) {
        this.value = value;
    }

    /** La m�thode accesseur qui renvoit la valeur de l'enum */
    public int getValue() {
        return this.value;
    }
}
