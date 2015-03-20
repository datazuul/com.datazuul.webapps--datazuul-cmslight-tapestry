package com.datazuul.webapps.cmslight.pages;

import org.apache.log4j.Logger;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;

/**
 * Dummy page shown after language has changed (required by tapestry
 * framework... :-( ).
 * 
 * @author Ralf Eichinger
 */
public class SpracheGeaendert extends AppBasePage implements PageRenderListener {
	private static final Logger LOG = Logger.getLogger(SpracheGeaendert.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.tapestry.event.PageRenderListener#pageBeginRender(org.apache
	 * .tapestry.event.PageEvent)
	 */
	public void pageBeginRender(PageEvent event) {
		logEnter(LOG, "SpracheGeaendert");
	}

}
