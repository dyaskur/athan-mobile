/**
 * 
 */
package com.athan.mobile.controls.enums;

/**
 * Lists all Zul tabs
 * 
 * @author Saad BENBOUZID
 */
public enum EnumZulTab {

	HOME("home", "introduction"), DOWNLOAD("download", "current"), NEWS("news",
			"releases"), RESOURCES("resources", "soundfiles"), CONTRIBUTE(
			"contribute", "feedbacks"), ABOUT("about", "contributor");

	private final String id;

	private final String defaultSubTab;

	private EnumZulTab(String id, String defaultSubTab) {
		this.id = id;
		this.defaultSubTab = defaultSubTab;
	}

	public String id() {
		return this.id;
	}

	public String defaultSubTab() {
		return this.defaultSubTab;
	}

}
