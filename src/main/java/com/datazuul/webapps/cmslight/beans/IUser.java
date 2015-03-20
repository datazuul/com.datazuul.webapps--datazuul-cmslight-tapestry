package com.datazuul.webapps.cmslight.beans;

import java.util.ArrayList;

import com.datazuul.webapps.cmslight.datasource.IUserSource;

/**
 * <p>
 * A <strong>IUser</strong> which is stored, along with his or her associated
 * {@link IWebsite}s, in a {@link IUserSource}.
 * </p>
 * 
 * @author Ralf Eichinger
 */
public interface IUser {
	/**
	 * @return the dataset id
	 */
	public String getId();

	/**
	 * @param id
	 *            the dataset id
	 */
	public void setId(String id);

	/**
	 * Return the from address.
	 */
	public String getFromAddress();

	/**
	 * Set the from address.
	 * 
	 * @param fromAddress
	 *            The new from address
	 */
	public void setFromAddress(String fromAddress);

	/**
	 * Return the full name.
	 */
	public String getFullName();

	/**
	 * Set the full name.
	 * 
	 * @param fullName
	 *            The new full name
	 */
	public void setFullName(String fullName);

	/**
	 * Return the password.
	 */
	public String getPassword();

	/**
	 * Set the password.
	 * 
	 * @param password
	 *            The new password
	 */
	public void setPassword(String password);

	/**
	 * Return the reply-to address.
	 */
	public String getReplyToAddress();

	/**
	 * Set the reply-to address.
	 * 
	 * @param replyToAddress
	 *            The new reply-to address
	 */
	public void setReplyToAddress(String replyToAddress);

	/**
	 * Add a new website to list of websites of this user.
	 * 
	 * @param website
	 *            the new website
	 */
	public void addWebsite(IWebsite website);

	/**
	 * Find and return all {@link IWebsite}s associated with this user. If there
	 * are none, a zero-length array is returned.
	 */
	public ArrayList getWebsites();

	/**
	 * Return the username.
	 */
	public String getUsername();

	/**
	 * Create and return a new {@link IWebsite} associated with this IUser, for
	 * the specified server name.
	 * 
	 * @param serverName
	 *            server name for which to create a website
	 * @param documentRoot
	 *            document root of the new website
	 * 
	 * @exception IllegalArgumentException
	 *                if the server name is not unique for this user
	 */
	public IWebsite createWebsite(String serverName, String documentRoot);

	/**
	 * Find and return the {@link IWebsite} associated with the specified server
	 * name. If none is found, return <code>null</code>.
	 * 
	 * @param serverName
	 *            server name to look up
	 */
	public IWebsite findWebsite(String serverName);

	/**
	 * Remove the specified {@link IWebsite} from being associated with this
	 * IUser.
	 * 
	 * @param website
	 *            IWebsite to be removed
	 * 
	 * @exception IllegalArgumentException
	 *                if the specified website is not associated with this IUser
	 */
	public void removeWebsite(IWebsite website);

	/**
	 * @return first website
	 */
	public IWebsite getFirstWebsite();
}