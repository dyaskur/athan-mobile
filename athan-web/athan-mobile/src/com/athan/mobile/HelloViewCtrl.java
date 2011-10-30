package com.athan.mobile;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;

/**
 * 
 * @author BENBOUZID
 */
public class HelloViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Label lblGreetings;
	private Button btnHi;

	private int i = 0;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		if (lblGreetings != null) {
			lblGreetings.setValue("postValue");
		}

		Html html = new Html();
		html.setContent("<div id=\"saad\">Hello !!</div>");
		html.setParent(comp);

	}

	public void onClick$btnHi() {
		lblGreetings.setValue("" + (i++));
		Clients.evalJavaScript("ecrire(" + i + ")");
	}

}
