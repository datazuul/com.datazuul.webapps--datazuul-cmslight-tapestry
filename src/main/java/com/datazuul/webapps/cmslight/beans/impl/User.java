package com.datazuul.webapps.cmslight.beans.impl;

import java.util.ArrayList;

import com.datazuul.webapps.cmslight.beans.IUser;
import com.datazuul.webapps.cmslight.beans.IWebsite;

/**
 * A CMS light user.
 * 
 * @author Ralf Eichinger
 */
public class User implements IUser {
	private String _fromAddress;
	private String _fullName;
	private String _id;
	private String _password;
	private String _replyToAddress;
	private String _username;
	private ArrayList _websites;

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getFromAddress() {
		return _fromAddress;
	}

	public void setFromAddress(String address) {
		_fromAddress = address;
	}

	public String getFullName() {
		return _fullName;
	}

	public void setFullName(String name) {
		_fullName = name;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String _password) {
		this._password = _password;
	}

	public String getReplyToAddress() {
		return _replyToAddress;
	}

	public void setReplyToAddress(String toAddress) {
		_replyToAddress = toAddress;
	}

	public String getUsername() {
		return _username;
	}

	public void setUsername(String _username) {
		this._username = _username;
	}

	public ArrayList getWebsites() {
		return _websites;
	}

	public void setWebsites(ArrayList _websites) {
		this._websites = _websites;
	}

	public IWebsite createWebsite(String serverName, String documentRoot) {
		// TODO Auto-generated method stub
		return null;
	}

	public IWebsite findWebsite(String serverName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeWebsite(IWebsite website) {
		// TODO Auto-generated method stub

	}

	public IWebsite getFirstWebsite() {
		if (_websites != null && !_websites.isEmpty()) {
			return (IWebsite) _websites.get(0);
		}
		return null;
	}

	public void addWebsite(IWebsite website) {
		if (_websites == null) {
			_websites = new ArrayList();
		}
		_websites.add(website);
	}
}
