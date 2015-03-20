package com.datazuul.webapps.cmslight.components;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineServiceView;

import com.datazuul.webapps.cmslight.Visit;

/**
 * The "frame" around all pages.
 * 
 * @author Ralf Eichinger
 */
public abstract class Border extends BaseComponent {

	public void logout(IRequestCycle cycle) {
		Visit visit = (Visit) cycle.getEngine().getVisit();

		if (visit != null) {
			visit.setLastFile(null);
			visit.setLastFilecontent(null);
			visit.setUser(null);
			visit.setLastFilepath(null);
			visit.setLastFolder(null);
			visit.setPreviewFile(null);
			visit.setTemplateDir(null);
		}

		// TODO logout process is not clean so...
		// wait for 4.0 to look how it got better..
		try {
			HttpSession session = cycle.getRequestContext().getSession();

			if (session != null) {
				session.invalidate();
				session = null;
			}
			IEngineServiceView engineView = (IEngineServiceView) cycle
					.getEngine();
			engineView.restart(cycle);
			cycle.activate("Home");
		} catch (IllegalStateException ex) {
			// Ignore.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}