package com.datazuul.webapps.cmslight;

import java.io.File;

import com.datazuul.webapps.cmslight.beans.IUser;
import com.datazuul.webapps.cmslight.filesystem.FileWrapper;
import com.datazuul.webapps.cmslight.filesystem.Folder;

/**
 * Page for editing a textfile.
 * 
 * @author Ralf Eichinger
 */
public class Visit {
	private IUser user;
	private File templateDir;
	private FileWrapper lastFile;
	private String lastFilecontent;
	private String lastFilepath;
	private Folder lastFolder;
	private File previewFile;

	public IUser getUser() {
		return this.user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	public File getTemplateDir() {
		return this.templateDir;
	}

	public void setTemplateDir(File templateDir) {
		this.templateDir = templateDir;
	}

	public FileWrapper getLastFile() {
		return this.lastFile;
	}

	public void setLastFile(FileWrapper file) {
		this.lastFile = file;
	}

	public String getLastFilecontent() {
		return this.lastFilecontent;
	}

	public void setLastFilecontent(String filecontent) {
		this.lastFilecontent = filecontent;
	}

	public String getLastFilepath() {
		return this.lastFilepath;
	}

	public void setLastFilepath(String filepath) {
		this.lastFilepath = filepath;
	}

	public Folder getLastFolder() {
		return this.lastFolder;
	}

	public void setLastFolder(Folder folder) {
		this.lastFolder = folder;
	}

	public File getPreviewFile() {
		return this.previewFile;
	}

	public void setPreviewFile(File file) {
		this.previewFile = file;
	}

}