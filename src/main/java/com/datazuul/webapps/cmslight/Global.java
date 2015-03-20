package com.datazuul.webapps.cmslight;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.request.RequestContext;

/**
 * Global object for the CMS light application. This class has the following
 * responsibilities:
 * <ul>
 * <li>Read init-parameter from web.xml and provide them as Java-Objects</li>
 * </ul>
 * 
 * @author Ralf Eichinger
 */
public class Global implements Serializable {
	private static final long serialVersionUID = 3617296735459162165L;

	private boolean init = false;

	public static String editables[];
	public static String previewables[];

	public void initialize(IRequestCycle cycle) {
		if (!init) {
			RequestContext requestContext = cycle.getRequestContext();
			ApplicationServlet servlet = requestContext.getServlet();
			ServletContext context = servlet.getServletContext();
			IPropertySource propertySource = cycle.getEngine()
					.getPropertySource();

			String editablesString = propertySource
					.getPropertyValue("editables");
			editables = editablesString.split(",");

			String previewablesString = propertySource
					.getPropertyValue("previewables");
			previewables = previewablesString.split(",");

			init = true;
		}
	}
}
