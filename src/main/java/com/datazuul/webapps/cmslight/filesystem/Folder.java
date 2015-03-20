package com.datazuul.webapps.cmslight.filesystem;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper around a {@link java.io.File} (directory).
 * 
 * @author Ralf Eichinger
 */
public class Folder implements Serializable {
	private static final long serialVersionUID = 4049361919892665910L;
	private File file;
	private File[] children;
	private FileWrapper[] files;
	private List parents;
	private Map nameToFile;
	private String docroot;

	public Folder(File file, String docroot) {
		this.file = file;
		this.docroot = docroot;
	}

	public void load() {
		this.children = this.file.listFiles();
		this.files = new FileWrapper[this.children.length];
		this.nameToFile = new HashMap(this.children.length);
		for (int i = 0; i < this.children.length; i++) {
			String name = this.children[i].getName();
			String relativePath = this.children[i].getAbsolutePath().substring(
					this.docroot.length() + 1);
			this.files[i] = new FileWrapper(this, this.children[i],
					relativePath);
			this.nameToFile.put(name, this.children[i]);
		}
		String path = this.file.getAbsolutePath().substring(
				this.docroot.length());
		String[] pp = path.split("/");
		if ("/".equals(path)) {
			pp = new String[1];
		}
		pp[0] = "/";

		HRef[] parentLinks = new HRef[pp.length];
		String s;
		int p = 0;
		for (int i = 0; i < pp.length - 1; i++) {
			s = path.substring(0, 1 + path.indexOf("/", p));
			p = s.length();
			parentLinks[i] = new HRef(pp[i], s);
		}
		parentLinks[pp.length - 1] = new HRef(pp[pp.length - 1], null);
		this.parents = Arrays.asList(parentLinks);
	}

	public FileWrapper[] getFiles() {
		return this.files;
	}

	public List getParents() {
		return this.parents;
	}

	public int getNumberOfParents() {
		return this.parents.size();
	}

	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return this.file;
	}

	public String getRelativePath() {
		if (getFile() != null) {
			String relativePath = getFile().getAbsolutePath();
			relativePath = relativePath.substring(this.docroot.length());
			return relativePath;
		}
		return null;
	}
}