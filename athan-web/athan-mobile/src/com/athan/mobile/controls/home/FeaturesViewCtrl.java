/**
 * 
 */
package com.athan.mobile.controls.home;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.api.Tabbox;

/**
 * Features Controler
 * 
 * @author Saad BENBOUZID
 */
public class FeaturesViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	
	private Tabbox tabFeatures;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		tabFeatures.setSelectedIndex(0);
		
		// Call the first tab panel handler
		onSelect$tabLocation();

	}

	public void onSelect$tabLocation() {
		allowZoomerJS();
	}

	public void onSelect$tabAlerts() {
		allowZoomerJS();
	}

	private void allowZoomerJS() {
		Clients.evalJavaScript("allowZoomer();");
	}

}
