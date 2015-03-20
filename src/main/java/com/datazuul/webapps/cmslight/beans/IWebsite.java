package com.datazuul.webapps.cmslight.beans;

import com.datazuul.webapps.cmslight.datasource.IUserSource;

/**
 * <p>
 * A <strong>IWebsite</strong> which is stored, along with the associated
 * {@link IUser}, in a {@link IUserSource}.
 * </p>
 * 
 * @author Ralf Eichinger
 */
public interface IWebsite {
	/**
	 * Return the document root.
	 */
	public String getDocumentRoot();

	/**
	 * Set the document root of the website
	 * 
	 * @param documentRoot
	 */
	public void setDocumentRoot(String documentRoot);

	/**
	 * Return the url to the homepage of the website.
	 */
	public String getUrl();

	/**
	 * Set the url to the homepage of the website
	 * 
	 * @param url
	 */
	public void setUrl(String url);
}