package com.datazuul.webapps.cmslight.beans.impl;

import com.datazuul.webapps.cmslight.beans.IWebsite;

/**
 * A hosted website on this server.
 * 
 * @author Ralf Eichinger
 */
public class Website implements IWebsite {
	private String _documentRoot;
	private String _url;

	public String getDocumentRoot() {
		return _documentRoot;
	}

	public void setDocumentRoot(String documentRoot) {
		this._documentRoot = documentRoot;
	}

	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		this._url = url;
	}
}
