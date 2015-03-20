package com.datazuul.webapps.cmslight.datasource;

import org.apache.tapestry.IRequestCycle;

import com.datazuul.webapps.cmslight.beans.IUser;
import com.datazuul.webapps.cmslight.beans.IWebsite;

/**
 * Interface for a service which provides {@link IUser} instances, and each
 * users {@link IWebsite} instances.
 * 
 * @author Ralf Eichinger
 */
public interface IUserSource {
	/**
	 * Name ("<code>pixocmslight.user-source</code>") of an application
	 * extension that implements {@link IUserSource}.
	 */
	public static final String USER_SOURCE_EXTENSION_NAME = "pixocmslight.user-source";

	/**
	 * Invoked to ensure the source is initialized. This must always be invoked
	 * before any other methods.
	 */
	public void initialize(IRequestCycle cycle);

	/**
	 * Create and return a new {@link IUser}defined in this user database.
	 * 
	 * @param username
	 *            Username of the new user
	 * 
	 * @exception IllegalArgumentExceptionif
	 *                the specified username is not unique
	 */
	public IUser createUser(String username);

	/**
	 * Finalize access to the underlying persistence layer.
	 * 
	 * @exception Exception
	 *                if a database access error occurs
	 */
	public void close() throws Exception;

	/**
	 * Return the existing {@link IUser}with the specified username, if any;
	 * otherwise return <code>null</code>.
	 * 
	 * @param username
	 *            Username of the user to retrieve
	 */
	public IUser findUser(String username);

	/**
	 * Return the set of {@link IUser}s defined in this user database.
	 */
	public IUser[] findUsers();

	/**
	 * Initiate access to the underlying persistence layer.
	 * 
	 * @exception Exception
	 *                if a database access error occurs
	 */
	public void open() throws Exception;

	/**
	 * Remove the specified {@link IUser}from this database.
	 * 
	 * @param user
	 *            IUser to be removed
	 * 
	 * @exception IllegalArgumentException
	 *                if the specified user is not associated with this database
	 */
	public void removeUser(IUser user);

	/**
	 * Save any pending changes to the underlying persistence layer.
	 * 
	 * @exception Exception
	 *                if a database access error occurs
	 */
	public void save() throws Exception;
}