package com.datazuul.webapps.cmslight.pages;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.valid.IValidationDelegate;

import com.datazuul.webapps.cmslight.Global;
import com.datazuul.webapps.cmslight.Visit;
import com.datazuul.webapps.cmslight.beans.IUser;
import com.datazuul.webapps.cmslight.datasource.IUserSource;

/**
 * First page (login, language selection).
 * 
 * @author Ralf Eichinger
 */
public abstract class Home extends AppBasePage implements PageRenderListener {
	private static final Logger LOG = Logger.getLogger(Home.class);

	public abstract String getUserName();

	public abstract String getPassword();

	public void pageBeginRender(PageEvent event) {
		logEnter(LOG, "Home");
	}

	public void login(IRequestCycle cycle) {
		// TODO: gibt es einen besseren ort dafür als hier
		// oder eine bessere art Global zu initialisieren?
		Global global = (Global) getGlobal();
		global.initialize(cycle);
		if (isValidLogin(cycle, getUserName(), getPassword())) {
			LOG.info("User " + getUserName() + " logged in.");
			cycle.activate("DateiManager");
			return;
		}
	}

	/**
	 * @param cycle
	 * @param userName
	 * @param password
	 * @return
	 */
	private boolean isValidLogin(IRequestCycle cycle, String userName,
			String password) {
		IValidationDelegate delegate = (IValidationDelegate) getBeans()
				.getBean("delegate");
		if (delegate.getHasErrors()) {
			return false;
		}
		IUser user = getUser(cycle, userName);
		if (user != null && user.getPassword().equals(password)) {
			Visit visit = (Visit) getVisit();
			visit.setUser(user);
			return true;
		} else if (user == null) {
			LOG.info("Home.isValidLogin: unbekannter Benutzername '" + userName
					+ "'");
			addErrorMessage(getMessage("invalidLogin"));
		} else if (!user.getPassword().equals(password)) {
			addErrorMessage(getMessage("invalidLogin"));
			LOG.info("Home.isValidLogin: falsches Passwort");
		}
		return false;
	}

	private IUser getUser(IRequestCycle cycle, String userName) {
		IEngine engine = cycle.getEngine();
		IApplicationSpecification specification = engine.getSpecification();
		IUserSource source = (IUserSource) specification.getExtension(
				IUserSource.USER_SOURCE_EXTENSION_NAME, IUserSource.class);
		source.initialize(cycle);
		IUser result = source.findUser(userName);
		return result;
	}

	public void setEnglish(IRequestCycle cycle) {
		getEngine().setLocale(Locale.ENGLISH);
		LOG.info("Home.setEnglish: Sprache nach 'Englisch' geaendert");
		cycle.activate("SpracheGeaendert");
	}

	public void setGerman(IRequestCycle cycle) {
		getEngine().setLocale(Locale.GERMAN);
		LOG.info("Home.setGerman: Sprache nach 'Deutsch' geaendert");
		cycle.activate("SpracheGeaendert");
	}
}
