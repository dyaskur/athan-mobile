/**
 * 
 */
package com.athan.mobile.controls.enums;

/**
 * Lists all Zul sub-tabs
 * 
 * @author Saad BENBOUZID
 */
public enum EnumZulPage {

	INTRODUCTION("introduction", "/zul/home/introduction.zul"), FEATURES(
			"features", "/zul/home/features.zul"), CURRENT("current",
			"/zul/download/current.zul"), CHANGELOGS("changelogs",
			"/zul/download/changelogs.zul"), RELEASES("releases",
			"/zul/news/releases.zul"), NEXTFEATURES("nextfeatures",
			"/zul/news/nextfeatures.zul"), SOUNDFILES("soundfiles",
			"/zul/resources/soundfiles.zul"), PROJECT("project",
			"/zul/resources/project.zul"), FEEDBACKS("feedbacks",
			"/zul/contribute/feedbacks.zul"), CONTRIBUTOR("contributor",
			"/zul/about/contributor.zul"), CREDITS("credits",
			"/zul/about/credits.zul"), ACKNOWLEDGMENT("acknowledgment",
			"/zul/about/acknowledgment.zul");

	private final String id;
	private final String zulFile;

	private EnumZulPage(String id, String zulFile) {
		this.id = id;
		this.zulFile = zulFile;
	}

	public String id() {
		return this.id;
	}

	public String zulFile() {
		return this.zulFile;
	}
}
