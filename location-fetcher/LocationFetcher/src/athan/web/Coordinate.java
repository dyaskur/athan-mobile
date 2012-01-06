/**
 * 
 */
package athan.web;

/**
 * @author Saad BENBOUZID
 */
public class Coordinate {
	private double mLat;
	private double mLng;

	public Coordinate() {
	}

	public Coordinate(double pLat, double pLng) {
		mLat = pLat;
		mLng = pLng;
	}

	public String toString() {
		String retour = "";

		retour += "Lat=[" + mLat + "] - ";
		retour += "Long=[" + mLng + "]";

		return retour;
	}

	public double getLat() {
		return mLat;
	}

	public void setLat(double pLat) {
		mLat = pLat;
	}

	public double getLng() {
		return mLng;
	}

	public void setLng(double pLng) {
		mLng = pLng;
	}

}
