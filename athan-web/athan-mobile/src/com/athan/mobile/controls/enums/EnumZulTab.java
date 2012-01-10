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

	HOME("home", "home_introduction"), DOWNLOAD("download", "download_current"), NEWS(
			"news", "news_releases"), RESOURCES("resources",
			"resources_soundfiles"), CONTRIBUTE("contribute",
			"contribute_feedbacks"), ABOUT("about", "about_contributor");

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
