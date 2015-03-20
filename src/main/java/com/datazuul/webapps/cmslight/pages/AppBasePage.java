package com.datazuul.webapps.cmslight.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.RenderString;

import com.datazuul.webapps.cmslight.validation.ValidationDelegate;

/**
 * Basic page for all application pages. Makes it possible to show all
 * validation errors (not only the first) and categorizing messages.
 * 
 * @author Ralf Eichinger
 */
public abstract class AppBasePage extends BasePage {
	private String error;

	private ArrayList errorMessages;

	private ArrayList warningMessages;

	private ArrayList infoMessages;

	public AppBasePage() {
		super();
	}

	protected void initialize() {
		this.errorMessages = new ArrayList();
		this.warningMessages = new ArrayList();
		this.infoMessages = new ArrayList();
	}

	public String getError() {
		return this.error;
	}

	public void setError(String errorMessage) {
		this.error = errorMessage;
	}

	public void addErrorMessage(String message) {
		this.errorMessages.add(message);
	}

	public void addWarningMessage(String message) {
		this.warningMessages.add(message);
	}

	public void addInfoMessage(String message) {
		this.infoMessages.add(message);
	}

	public void deleteMessages() {
		this.errorMessages.clear();
		this.warningMessages.clear();
		this.infoMessages.clear();
	}

	public boolean hasMessages() {
		try {
			ValidationDelegate delegate = (ValidationDelegate) getBeans()
					.getBean("delegate");
			if (delegate.getHasErrors()) {
				return true;
			}
		} catch (ApplicationRuntimeException e) {
			// Component does not define a bean name delegate.
		}
		if (this.errorMessages.size() > 0 || this.warningMessages.size() > 0
				|| this.infoMessages.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getHasMessages() {
		return hasMessages();
	}

	public ArrayList getErrorMessages() {
		try {
			ValidationDelegate delegate = (ValidationDelegate) getBeans()
					.getBean("delegate");
			if (delegate.getHasErrors()) {
				List trackings = delegate.getAssociatedTrackings();
				if (trackings.size() != 0) {
					Iterator i = trackings.iterator();

					while (i.hasNext()) {
						IFieldTracking tracking = (IFieldTracking) i.next();

						if (tracking.isInError()) {
							RenderString render = (RenderString) tracking
									.getErrorRenderer();
							addErrorMessage(render.getString());
						}
					}
				}
			}
		} catch (ApplicationRuntimeException e) {
			// Component does not define a bean name delegate.
		}
		return this.errorMessages;
	}

	public ArrayList getWarningMessages() {
		return this.warningMessages;
	}

	public ArrayList getInfoMessages() {
		return this.infoMessages;
	}

	public void logEnter(Logger log, String className) {
		if (getRequestCycle() != null && !getRequestCycle().isRewinding()) {
			log.info("ENTERED: " + className);
		}
	}
}
