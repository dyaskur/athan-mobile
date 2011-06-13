/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package athan.src.SalaahCalc;

/**
 * Conteneur de paramètres personnalisés pour la méthode de calcul
 *
 * @author Saad BENBOUZID
 */
public class CalculationCustomParams {
    private double mFajrAngle;
    private int mMaghrebSelector;
    private int mIshaaSelector;
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
