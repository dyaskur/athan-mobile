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
 * Enumération des méthodes juridiques pour le calcul des prières.
 *
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
