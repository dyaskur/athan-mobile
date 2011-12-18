/**
 * 
 */
package com.athan.mobile.controls.resources;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;

/**
 * Project Controller
 * 
 * @author Saad BENBOUZID
 */
public class ProjectViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	public void onClick$divProjectHosting() {
		Executions.getCurrent().sendRedirect(Labels.getLabel("project.hosting.url"), "_blank");
	}

}
