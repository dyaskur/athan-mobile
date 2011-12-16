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

	HOME_INTRODUCTION("home_introduction", "/zul/home/introduction.zul"), HOME_FEATURES(
			"home_features", "/zul/home/features.zul"), DOWNLOAD_CURRENT(
			"download_current", "/zul/download/current.zul"), DOWNLOAD_CHANGELOGS(
			"download_changelogs", "/zul/download/changelogs.zul"), NEWS_RELEASES(
			"news_releases", "/zul/news/releases.zul"), NEWS_NEXTFEATURES(
			"news_nextfeatures", "/zul/news/nextfeatures.zul"), RESOURCES_SOUNDFILES(
			"resources_soundfiles", "/zul/resources/soundfiles.zul"), RESOURCES_PROJECT(
			"resources_project", "/zul/resources/project.zul"), CONTRIBUTE_FEEDBACKS(
			"contribute_feedbacks", "/zul/contribute/feedbacks.zul"), ABOUT_CONTRIBUTOR(
			"about_contributor", "/zul/about/contributor.zul"), ABOUT_CREDITS(
			"about_credits", "/zul/about/credits.zul"), ABOUT_ACKNOWLEDGMENT(
			"about_acknowledgment", "/zul/about/acknowledgment.zul");

	private final String id;
	private final String zulFile;

	private EnumZulTab(String id, String zulFile) {
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
