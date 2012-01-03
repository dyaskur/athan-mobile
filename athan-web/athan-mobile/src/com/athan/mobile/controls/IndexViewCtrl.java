package com.athan.mobile.controls;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.api.Tabpanel;

import com.athan.mobile.constants.AthanConstants;
import com.athan.mobile.controls.enums.EnumZulTab;
import com.athan.mobile.init.CookieUtil;
import com.athan.mobile.init.LocalesProvider;
import com.athan.mobile.utils.Messages;

/**
 * Manager Controller
 * 
 * @author Saad BENBOUZID
 */
public class IndexViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(IndexViewCtrl.class
			.getName());

	private Component me;

	private Tabpanel tbpHome;
	private Tabpanel tbpDownload;
	private Tabpanel tbpNews;
	private Tabpanel tbpResources;
	private Tabpanel tbpContribute;
	private Tabpanel tbpAbout;

	private Hbox hbxFooter;

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

			Executions.getCurrent().sendRedirect(
					AthanConstants.DOWNLOAD_SERVLET + "?"
							+ AthanConstants.DOWNLOAD_FILETYPE_PARAM + "="
							+ AthanConstants.DOWNLOAD_JAD, null);
			LOG.log(Level.FINE, "Téléchargement du fichier JAD");

		} catch (Exception exc) {
			Messages.error(Labels.getLabel("current.fileDownload.error"));
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAD",
					exc);
		} finally {
			Clients.evalJavaScript(AthanConstants.GA_JAD_DOWNLOAD);
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

		// Addthis content
		Html htmlAddThis = new Html();
		StringBuffer sb = new StringBuffer();
		sb.append("<!-- AddThis Button BEGIN -->");
		sb.append("<div class=\"addthis_toolbox addthis_default_style\" style=\"padding-right: 10px;padding-top: 6px;\">");
		sb.append("<a class=\"addthis_button_facebook_like\" fb:like:layout=\"button_count\" fb:like:action=\"recommend\"></a>");
		sb.append("<a class=\"addthis_button_tweet\"></a>");
		sb.append("<a class=\"addthis_button_google_plusone\" g:plusone:size=\"medium\"></a>");
		sb.append("<a class=\"addthis_counter addthis_pill_style\"></a>");
		sb.append("</div>");
		sb.append("<!-- AddThis Button END -->");
		htmlAddThis.setContent(sb.toString());
		htmlAddThis.setParent(hbxFooter);
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
