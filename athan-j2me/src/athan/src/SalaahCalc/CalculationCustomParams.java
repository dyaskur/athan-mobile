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
 * Conteneur des paramètres personnalisés pour un calcul de prières.
 *
 * @author Saad BENBOUZID
 */
public class CalculationCustomParams {

    private double mFajrAngle;
    public int mImsakSelector;
    private int mMaghrebSelector;
    private int mIshaaSelector;
    public double mImsakValue;
    private double mMaghrebValue;
    private double mIshaaValue;

    public CalculationCustomParams() {
    }

    /**
     * @return the mFajrAngle
     */
    public double getFajrAngle() {
        return mFajrAngle;
    }

    /**
     * @param mFajrAngle the mFajrAngle to set
     */
    public void setFajrAngle(double mFajrAngle) {
        this.mFajrAngle = mFajrAngle;
    }

    /**
     * @return the mImsakSelector
     */
    public int getImsakSelector() {
        return mImsakSelector;
    }

    /**
     * @param mImsakSelector the mImsakSelector to set
     */
    public void setImsakSelector(int mImsakSelector) {
        this.mImsakSelector = mImsakSelector;
    }

    /**
     * @return the mMaghrebSelector
     */
    public int getMaghrebSelector() {
        return mMaghrebSelector;
    }

    /**
     * @param mMaghrebSelector the mMaghrebSelector to set
     */
    public void setMaghrebSelector(int mMaghrebSelector) {
        this.mMaghrebSelector = mMaghrebSelector;
    }

    /**
     * @return the mIshaaSelector
     */
    public int getIshaaSelector() {
        return mIshaaSelector;
    }

    /**
     * @param mIshaaSelector the mIshaaSelector to set
     */
    public void setIshaaSelector(int mIshaaSelector) {
        this.mIshaaSelector = mIshaaSelector;
    }

    /**
     * @return the mImsakValue
     */
    public double getImsakValue() {
        return mImsakValue;
    }

    /**
     * @param mImsakValue the mImsakValue to set
     */
    public void setImsakValue(double mImsakValue) {
        this.mImsakValue = mImsakValue;
    }

    /**
     * @return the mMaghrebValue
     */
    public double getMaghrebValue() {
        return mMaghrebValue;
    }

    /**
     * @param mMaghrebValue the mMaghrebValue to set
     */
    public void setMaghrebValue(double mMaghrebValue) {
        this.mMaghrebValue = mMaghrebValue;
    }

    /**
     * @return the mIshaaValue
     */
    public double getIshaaValue() {
        return mIshaaValue;
    }

    /**
     * @param mIshaaValue the mIshaaValue to set
     */
    public void setIshaaValue(double mIshaaValue) {
        this.mIshaaValue = mIshaaValue;
    }
}
