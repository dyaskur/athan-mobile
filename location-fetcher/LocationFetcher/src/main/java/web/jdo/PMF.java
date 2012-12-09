package athan.web.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * PersistenceManager singleton
 * 
 * @author Saad BENBOUZID
 */
public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private PMF() {}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}
