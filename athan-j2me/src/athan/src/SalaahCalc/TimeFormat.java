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
 * Enumération des types d'horaire pour l'affichage des horaires des prières.
 *
 * @author BENBOUZID
 */
public class TimeFormat {

    public static final String TIME_SEPARATOR = ":";

    private final int value;

    protected static final int Time24 = 0;      // 24-hour format
    protected static final int Time12 = 1;      // 12-hour format

    /** L'attribut qui contient la valeur associé à l'enum */
    public static final TimeFormat H24 = new TimeFormat(Time24);
    public static final TimeFormat H12 = new TimeFormat(Time12);

    /** Le constructeur qui associe une valeur à l'enum */
    private TimeFormat(int value) {
        this.value = value;
    }

    /** La méthode accesseur qui renvoit la valeur de l'enum */
    public int getValue() {
        return this.value;
    }
}
