/**
 * 
 */
package com.athan.mobile.enums;

/**
 * Enum for the resources songs
 * 
 * @author Saad BENBOUZID
 */
public enum EnumSong {

	JERUSALEM("soundfiles.alaqsa", "", "Alaqsa.mp3"), BOSNIA(
			"soundfiles.bosnia", "", "Bosnia.mp3"), EGYPT("soundfiles.egypt",
			"", "Egypt.mp3"), LEBANON("soundfiles.lebanon", "", "Lebanon.mp3"), MADINA(
			"soundfiles.madina", "", "Madina.mp3"), MAKKAH1(
			"soundfiles.makkah1", "1", "Makkah1.mp3"), MAKKAH2(
			"soundfiles.makkah2", "2", "Makkah2.mp3"), PAKISTAN(
			"soundfiles.pakistan", "", "Pakistan.mp3"), TURKEY(
			"soundfiles.turkey", "", "Turkey.mp3"), SHEIKH("soundfiles.sheikh",
			"", "Sheikh.mp3");

	/** Key label in labels.properties */
	private String keyLabel;
	/** Label complement (for ex. "(1)", "bis",...) */
	private String labelCmpl;
	/** MP3 file name */
	private String mp3File;

	private EnumSong(String keyLabel, String labelCmpl, String mp3File) {
		this.keyLabel = keyLabel;
		this.labelCmpl = labelCmpl;
		this.mp3File = mp3File;
	}

	public String keyLabel() {
		return keyLabel;
	}

	public String labelCmpl() {
		return labelCmpl;
	}

	public String mp3File() {
		return mp3File;
	}

}
