/* Composer5.java

	Purpose:
		
	Description:
		
Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package com.athan.mobile;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 * This sample is copied from
 * <a href="http://docs.zkoss.org/wiki/ZK_MVC_Made_Easy#Get_Rid_of_Those_.22forward.22_Attributes">here</a>.
 * @author henrichen
 */
public class Composer5 extends GenericForwardComposer {
    
	private static final long serialVersionUID = 1L;
	
	private transient Textbox firstName;
    private transient Textbox lastName;
    private transient Label fullName;
 
    /** onChange event from firstName component
     */
    public void onChange$firstName(Event event) { 
        fullName.setValue(firstName.getValue()+" "+lastName.getValue());
    }
 
    /** onChange event from lastName component
     */
    public void onChange$lastName(Event event) {
        fullName.setValue(firstName.getValue()+" "+lastName.getValue());
    }
}
