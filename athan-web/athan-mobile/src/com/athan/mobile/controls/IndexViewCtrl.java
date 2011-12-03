package com.athan.mobile.controls;

import java.io.FileNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.api.Tabpanel;

import com.athan.mobile.controls.enums.EnumZulTab;
import com.athan.mobile.init.CookieUtil;
import com.athan.mobile.init.LocalesProvider;

/**
 * Manager controler
 * 
 * @author BENBOUZID
 */
public class IndexViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Component me;

	private Tabpanel tbpHome;
	private Tabpanel tbpDownload;
	private Tabpanel tbpNews;
	private Tabpanel tbpResources;
	private Tabpanel tbpContribute;
	private Tabpanel tbpAbout;
	
	private EnumZulTab currentTab = null;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		me = comp;
		
		// Shows the home tab
		init();
	}
	
	public void onClick$btnJadFileMain() {
		try {
			Filedownload.save("/jar/nogps/Athan.jad", "text/vnd.sun.j2me.app-descriptor");
		} catch (FileNotFoundException exc) {
			Clients.alert(Labels.getLabel("current.JadFile.error"), StringUtils.EMPTY, Messagebox.ERROR);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	

	public void loadZulChild(EnumZulTab zulTab, Component container) {

		if (currentTab != null && currentTab.equals(zulTab)) {
			// Exits if it's current tab
			return;
		}
		
		// Removes previous tab content
		for (EnumZulTab en : EnumZulTab.values()) {
			if (container.hasFellow(en.id())) {
				container.removeChild(container.getFellow(en.id()));
			}
		}

		Component zulContent = Executions.createComponents(zulTab.zulFile(),
				container, null);
		zulContent.setId(zulTab.id());

		// Adds created tab
		if (zulContent != null) {
			zulContent.setParent(container);
			currentTab = zulTab;
		}
	}

	public void onClick$homeIntroduction() {
		loadZulChild(EnumZulTab.HOME_INTRODUCTION, tbpHome);
	}

	public void onClick$homeFeatures() {
		loadZulChild(EnumZulTab.HOME_FEATURES, tbpHome);
	}

	public void onClick$downloadCurrent() {
		loadZulChild(EnumZulTab.DOWNLOAD_CURRENT, tbpDownload);
	}

	public void onClick$downloadChangelogs() {
		loadZulChild(EnumZulTab.DOWNLOAD_CHANGELOGS, tbpDownload);
	}

	public void onClick$newsReleases() {
		loadZulChild(EnumZulTab.NEWS_RELEASES, tbpNews);
	}

	public void onClick$newsNextfeatures() {
		loadZulChild(EnumZulTab.NEWS_NEXTFEATURES, tbpNews);
	}

	public void onClick$resourcesSoundfiles() {
		loadZulChild(EnumZulTab.RESOURCES_SOUNDFILES, tbpResources);
	}

	public void onClick$resourcesProject() {
		loadZulChild(EnumZulTab.RESOURCES_PROJECT, tbpResources);
	}

	public void onClick$contributeFeedbacks() {
		loadZulChild(EnumZulTab.CONTRIBUTE_FEEDBACKS, tbpContribute);
	}

	public void onClick$aboutContributor() {
		loadZulChild(EnumZulTab.ABOUT_CONTRIBUTOR, tbpAbout);
	}

	public void onClick$aboutCredits() {
		loadZulChild(EnumZulTab.ABOUT_CREDITS, tbpAbout);
	}

	public void onClick$aboutAcknowledgment() {
		loadZulChild(EnumZulTab.ABOUT_ACKNOWLEDGMENT, tbpAbout);
	}

	public void onClick$tabHome() {
		onClick$homeIntroduction();
	}

	public void onClick$tabDownload() {
		onClick$downloadCurrent();
	}

	public void onClick$tabNews() {
		onClick$newsReleases();
	}

	public void onClick$tabResources() {
		onClick$resourcesSoundfiles();
	}

	public void onClick$tabContribute() {
		onClick$contributeFeedbacks();
	}

	public void onClick$tabAbout() {
		onClick$aboutContributor();
	}

	/**
	 * Initialization function
	 */
	private void init() {
		onClick$tabHome();
	}

	public void onClick$english_flag() {
		CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME,
				LocalesProvider.LOCALE_EN);
		Executions.sendRedirect(""); // reload the same page
	}

	public void onClick$french_flag() {
		CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME,
				LocalesProvider.LOCALE_FR);
		Executions.sendRedirect(""); // reload the same page
	}

	public Component getMe() {
		return me;
	}

}
