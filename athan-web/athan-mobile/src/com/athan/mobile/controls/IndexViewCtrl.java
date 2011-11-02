package com.athan.mobile.controls;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import com.athan.mobile.init.CookieUtil;
import com.athan.mobile.init.LocalesProvider;

/**
 * 
 * @author BENBOUZID
 */
public class IndexViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// TODO

	}

	public void onClick$english_flag() {
		CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME, LocalesProvider.LOCALE_EN);
		Executions.sendRedirect(""); //reload the same page
	}
	
	public void onClick$french_flag() {
		CookieUtil.setCookie(LocalesProvider.MY_LOCALE_COOKIE_NAME, LocalesProvider.LOCALE_FR);
		Executions.sendRedirect(""); //reload the same page
	}

}
