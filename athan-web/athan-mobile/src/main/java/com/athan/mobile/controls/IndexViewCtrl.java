package com.athan.mobile.controls;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import com.athan.mobile.constants.AthanConstants;
import com.athan.mobile.controls.enums.EnumZulPage;
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

	private Tabbox tab;

	private Tabpanel tbpHome;
	private Tabpanel tbpDownload;
	private Tabpanel tbpNews;
	private Tabpanel tbpResources;
	private Tabpanel tbpContribute;
	private Tabpanel tbpAbout;

	private Hbox hbxFooter;

	private EnumZulPage currentTab = null;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		me = comp;

		// Get request parameters
		try {
			String language = Executions.getCurrent().getParameter("lng");
			String tab = Executions.getCurrent().getParameter("tab");
			String page = Executions.getCurrent().getParameter("page");
			if (!StringUtils.isEmpty(language)) {
				// Set language
				boolean redirectLng = true;
				if (language.equals("en")) {
					Executions
							.getCurrent()
							.getSession()
							.setAttribute(
									Attributes.PREFERRED_LOCALE,
									Locales.getLocale(LocalesProvider.LOCALE_EN));
					// CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME,
					// LocalesProvider.LOCALE_EN);
				} else if (language.equals("fr")) {
					Executions
							.getCurrent()
							.getSession()
							.setAttribute(
									Attributes.PREFERRED_LOCALE,
									Locales.getLocale(LocalesProvider.LOCALE_FR));
					// CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME,
					// LocalesProvider.LOCALE_FR);
				} else {
					redirectLng = false;
				}

				if (redirectLng) {
					// Redirect to specified URI or root URI
					if (!StringUtils.isEmpty(tab) && !StringUtils.isEmpty(page)) {
						Executions.sendRedirect("/" + tab + "/" + page + "/");
					} else {
						Executions.sendRedirect("/");
					}
				}
			}

			// Proceeds tabs
			if (!StringUtils.isEmpty(tab)) {
				if (EnumZulTab.HOME.id().equals(tab)) {
					this.tab.setSelectedPanel(tbpHome);
					proceedPage(page, EnumZulTab.HOME, tbpHome);
				} else if (EnumZulTab.DOWNLOAD.id().equals(tab)) {
					this.tab.setSelectedPanel(tbpDownload);
					proceedPage(page, EnumZulTab.DOWNLOAD, tbpDownload);
				} else if (EnumZulTab.NEWS.id().equals(tab)) {
					this.tab.setSelectedPanel(tbpNews);
					proceedPage(page, EnumZulTab.NEWS, tbpNews);
				} else if (EnumZulTab.RESOURCES.id().equals(tab)) {
					this.tab.setSelectedPanel(tbpResources);
					proceedPage(page, EnumZulTab.RESOURCES, tbpResources);
				} else if (EnumZulTab.CONTRIBUTE.id().equals(tab)) {
					this.tab.setSelectedPanel(tbpContribute);
					proceedPage(page, EnumZulTab.CONTRIBUTE, tbpContribute);
				} else if (EnumZulTab.ABOUT.id().equals(tab)) {
					this.tab.setSelectedPanel(tbpAbout);
					proceedPage(page, EnumZulTab.ABOUT, tbpAbout);
				} else {
					// Shows the home tab
					selectDefaultPage();
				}
			}

			// Default page selection if no parameters were specified
			if (StringUtils.isEmpty(tab)) {
				// Shows the home tab
				selectDefaultPage();
			}

			// Clients.alert(page);
		} catch (Exception exc) {
			// Shows the home tab
			selectDefaultPage();
		}
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

	public void loadZulChild(EnumZulPage zulPage, Component container) {

		if (currentTab != null && currentTab.equals(zulPage)) {
			// Exits if it's current tab
			return;
		}

		// Removes previous tab content
		for (EnumZulPage en : EnumZulPage.values()) {
			if (container.hasFellow(en.id())) {
				container.removeChild(container.getFellow(en.id()));
			}
		}

		Component zulContent = Executions.createComponents(zulPage.zulFile(),
				container, null);
		zulContent.setId(zulPage.id());

		try {
			// Changes web application title
			me.getPage().setTitle(
					Labels.getLabel("application.title") + " - "
							+ Labels.getLabel("title." + zulPage.id()));
		} catch (Exception exc) {
		}

		// Adds created tab
		if (zulContent != null) {
			zulContent.setParent(container);
			currentTab = zulPage;
		}
	}

	/**
	 * Parses and proceeds "page" parameter from URI
	 */
	private void proceedPage(String page, EnumZulTab enuTab, Tabpanel tbp) {
		if (!StringUtils.isEmpty(page)) {
			boolean pageLoaded = false;
			EnumZulPage enuPage = valueOfId(page);
			if (enuPage != null) {
				loadZulChild(enuPage, tbp);
				pageLoaded = true;
			}
			if (!pageLoaded) {
				// Loads default page
				loadZulChild(valueOfId(enuTab.defaultSubTab()), tbp);
			}
		} else {
			// Loads default page
			loadZulChild(valueOfId(enuTab.defaultSubTab()), tbp);
		}
	}

	/**
	 * Get Enum from page id's name
	 * 
	 * @param id
	 * @return
	 */
	private EnumZulPage valueOfId(String id) {
		for (EnumZulPage enuPage : EnumZulPage.values()) {
			if (enuPage.id().equals(id)) {
				return enuPage;
			}
		}
		return null;
	}

	public void onClick$homeIntroduction() {
		loadZulChild(EnumZulPage.INTRODUCTION, tbpHome);
	}

	public void onClick$homeFeatures() {
		loadZulChild(EnumZulPage.FEATURES, tbpHome);
	}

	public void onClick$downloadCurrent() {
		loadZulChild(EnumZulPage.CURRENT, tbpDownload);
	}

	public void onClick$downloadChangelogs() {
		loadZulChild(EnumZulPage.CHANGELOGS, tbpDownload);
	}

	public void onClick$newsReleases() {
		loadZulChild(EnumZulPage.RELEASES, tbpNews);
	}

	public void onClick$newsNextfeatures() {
		loadZulChild(EnumZulPage.NEXTFEATURES, tbpNews);
	}

	public void onClick$resourcesSoundfiles() {
		loadZulChild(EnumZulPage.SOUNDFILES, tbpResources);
	}

	public void onClick$resourcesProject() {
		loadZulChild(EnumZulPage.PROJECT, tbpResources);
	}

	public void onClick$contributeFeedbacks() {
		loadZulChild(EnumZulPage.FEEDBACKS, tbpContribute);
	}

	public void onClick$aboutContributor() {
		loadZulChild(EnumZulPage.CONTRIBUTOR, tbpAbout);
	}

	public void onClick$aboutCredits() {
		loadZulChild(EnumZulPage.CREDITS, tbpAbout);
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
	 * Sets default landing page (in case no parameters wee sent to URI)s
	 */
	private void selectDefaultPage() {
		tab.setSelectedPanel(tbpHome);
		onClick$tabHome();
	}

	public void onClick$english_flag() {
		// Executions
		// .getCurrent()
		// .getSession()
		// .setAttribute(Attributes.PREFERRED_LOCALE,
		// Locales.getLocale(LocalesProvider.LOCALE_EN));
		CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME,
				LocalesProvider.LOCALE_EN);
		Executions.sendRedirect("/"); // reload the same page
	}

	public void onClick$french_flag() {
		// Executions
		// .getCurrent()
		// .getSession()
		// .setAttribute(Attributes.PREFERRED_LOCALE,
		// Locales.getLocale(LocalesProvider.LOCALE_FR));
		CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME,
				LocalesProvider.LOCALE_FR);
		Executions.sendRedirect("/"); // reload the same page
	}

	public Component getMe() {
		return me;
	}

}
