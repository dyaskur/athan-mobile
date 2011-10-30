/**
 * 
 */
package com.athan.mobile.init;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.sys.DesktopCache;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.util.DesktopInit;

/**
 * @author Saad BENBOUZID
 *
 */
public class MyDesktopInit implements DesktopInit {
	  public void init(Desktop desktop, Object req) throws Exception {    
	    HttpServletRequest request = (HttpServletRequest) req;     
	    //Remove old Desktop   
	    String oldDesktopId = (String) request.getSession().getAttribute("currentDesktopId");   
	    WebAppCtrl ctrl = (WebAppCtrl)Executions.getCurrent().getDesktop().getWebApp();   
	    DesktopCache dc = ctrl.getDesktopCache(desktop.getSession());   
	    dc.removeDesktop(dc.getDesktop(oldDesktopId));       
	    //Add new Desktop
	    request.getSession().setAttribute("currentDesktopId", desktop.getId()); 
	  }
	}
