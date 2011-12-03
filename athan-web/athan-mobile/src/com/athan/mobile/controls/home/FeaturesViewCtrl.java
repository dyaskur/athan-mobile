/**
 * 
 */
package com.athan.mobile.controls.home;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;

/**
 * Features Controler
 * 
 * @author Saad BENBOUZID
 */
public class FeaturesViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

	}
	
	public void onSelect$tabMainFt() {
		allowZoomerJS();
	}
	
	public void onSelect$tabLocation() {
		allowZoomerJS();
	}
	
	public void onSelect$tabAlerts() {
		allowZoomerJS();
	}

	public void onSelect$tabCalculation() {
		allowZoomerJS();
	}
	
	public void onSelect$tabCompass() {
		allowZoomerJS();
	}
	
	public void onSelect$tabLanguage() {
		allowZoomerJS();
	}
	
	public void onSelect$tabLocaltime() {
		allowZoomerJS();
	}
	
	public void onSelect$tabPrayers() {
		allowZoomerJS();
	}

	private void allowZoomerJS() {
		Clients.evalJavaScript("allowZoomer();");
	}

}
