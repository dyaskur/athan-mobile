//    Athan Mobile - Prayer Times Software
//    Copyright (C) 2011 - Saad BENBOUZID
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
package athan.src.SalaahCalc;

/**
 * Enumération des méthodes de calculs pour le calcul de prières.
 *
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
    public CalculationMethods(int value) {
        this.value = value;
    }

    /** La méthode accesseur qui renvoit la valeur de l'enum */
    public int getValue() {
        return this.value;
    }
}
