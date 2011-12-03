/**
 * 
 */
package com.athan.mobile.controls.download;

import java.io.FileNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;

/**
 * Current Controler
 * 
 * @author Saad BENBOUZID
 */
public class CurrentViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// TODO Auto-generated method stub
	}
	
	public void onClick$btnJadFile() {
		try {
			Filedownload.save("/jar/nogps/Athan.jad", "text/vnd.sun.j2me.app-descriptor");
		} catch (FileNotFoundException exc) {
			Clients.alert(Labels.getLabel("current.JadFile.error"), StringUtils.EMPTY, Messagebox.ERROR);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
