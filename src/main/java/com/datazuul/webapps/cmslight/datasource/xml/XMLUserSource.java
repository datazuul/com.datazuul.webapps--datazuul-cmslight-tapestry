package com.datazuul.webapps.cmslight.datasource.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.digester.Digester;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.resource.ContextResourceLocation;

import com.datazuul.webapps.cmslight.beans.IUser;
import com.datazuul.webapps.cmslight.beans.impl.User;
import com.datazuul.webapps.cmslight.beans.impl.Website;
import com.datazuul.webapps.cmslight.datasource.IUserSource;

/**
 * Implementation of {@link IUserSource} that reads user information from an XML
 * file. Only reading yet, no storing in XML file.
 * 
 * @author Ralf Eichinger
 */
public class XMLUserSource implements IUserSource {
	private String _usersFile = "database.xml";

	/**
	 * Map of {@link User} keyed on (String) user id.
	 */
	private Map _users;

	private List _userIds;

	public IUser getUser(String userId) {
		return (IUser) _users.get(userId);
	}

	/**
	 * Initializes the list of users from the a file stored in WEB-INF.
	 */
	public synchronized void initialize(IRequestCycle cycle) {
		if (_users == null) {
			ServletContext servletContext = cycle.getRequestContext()
					.getServlet().getServletContext();
			IResourceLocation location = new ContextResourceLocation(
					servletContext, "/WEB-INF/" + _usersFile);

			User[] users = read(location);

			_users = new HashMap();
			_userIds = new ArrayList();

			for (int i = 0; i < users.length; i++) {
				String id = users[i].getId();
				_userIds.add(id);
				_users.put(id, users[i]);
			}
		}
	}

	public String getUsersFile() {
		return _usersFile;
	}

	public void setUsersFile(String string) {
		_usersFile = string;
	}

	public User[] read(IResourceLocation location) {
		Digester digester = new Digester();
		digester.setValidating(false);

		digester.addObjectCreate("users", ArrayList.class);

		digester.addObjectCreate("users/user", User.class);
		digester.addSetProperties("users/user");

		digester.addObjectCreate("users/user/website", Website.class);
		digester.addSetProperties("users/user/website");
		// This adds each website instance to the list, by
		// invoking method addWebsite() in User.class
		digester.addSetNext("users/user/website", "addWebsite");
		// This adds each User instance to the list, by
		// invoking method add() on the ArrayList.
		digester.addSetNext("users/user", "add");

		URL fileURL = location.getResourceURL();

		List users = null;
		try {
			InputStream stream = fileURL.openStream();
			users = (List) digester.parse(stream);
			stream.close();
		} catch (Exception ex) {
			throw new ApplicationRuntimeException("Unable to read users file "
					+ location, ex);
		}

		int count = Tapestry.size(users);
		return (User[]) users.toArray(new User[count]);
	}

	public List getUserIds() {
		return new ArrayList(_userIds);
	}

	public IUser createUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public IUser findUser(String username) {
		if (_users != null && _users.size() > 0) {
			for (Iterator iter = _users.values().iterator(); iter.hasNext();) {
				IUser user = (IUser) iter.next();
				if (username.equals(user.getUsername())) {
					return user;
				}
			}
		}
		return null;
	}

	public IUser[] findUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void open() throws Exception {
		// TODO Auto-generated method stub

	}

	public void removeUser(IUser user) {
		// TODO Auto-generated method stub

	}

	public void save() throws Exception {
		// TODO Auto-generated method stub

	}

}
