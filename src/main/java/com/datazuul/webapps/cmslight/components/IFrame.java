package com.datazuul.webapps.cmslight.components;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * IFrame-Component: Implements an &lt;iframe&gt; within a html page.
 * 
 * @author Ralf Eichinger
 */
public abstract class IFrame extends AbstractComponent {
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		if (cycle.isRewinding())
			return;

		writer.beginEmpty("iframe");
		String targetPage = getTargetPage();
		if (targetPage != null) {
			IEngine engine = cycle.getEngine();
			IEngineService pageService = engine
					.getService(Tapestry.PAGE_SERVICE);
			String[] parameters = { targetPage };
			ILink link = pageService.getLink(cycle, this, parameters);
			writer.attribute("src", link.getURL());
		}
		renderInformalParameters(writer, cycle);

		writer.closeTag();
	}

	public abstract String getTargetPage();

	public abstract void setTargetPage(String targetPage);
}
