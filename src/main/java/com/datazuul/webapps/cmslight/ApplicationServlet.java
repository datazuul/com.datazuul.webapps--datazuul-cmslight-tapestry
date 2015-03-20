package com.datazuul.webapps.cmslight;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Basic servlet for this application.
 * 
 * @author Ralf Eichinger
 */
public class ApplicationServlet extends org.apache.tapestry.ApplicationServlet {
	private static final long serialVersionUID = 3258128038125318194L;
	private static final Log LOG = LogFactory.getLog(ApplicationServlet.class);

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Enumeration en = config.getServletContext().getAttributeNames();
	}
}