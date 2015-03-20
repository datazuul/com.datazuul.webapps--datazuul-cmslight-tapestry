package com.datazuul.webapps.cmslight.filesystem;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.datazuul.webapps.cmslight.Global;

/**
 * Wrapper around a {@link java.io.File}.
 * 
 * @author Ralf Eichinger
 */
public class FileWrapper implements Serializable {

	private static final long serialVersionUID = 3545233618126321972L;

	private static int _nextuid = 0;
	private int _uid = _nextuid++;

	private boolean selected;

	private DateFormat dateFormat;

	private File file;

	private Folder folder;

	private String relpath;

	public FileWrapper(File file, String path, String docroot) {
		this.folder = new Folder(file.getParentFile(), docroot);
		this.file = file;
		this.relpath = path;
		this.dateFormat = DateFormat.getDateTimeInstance();
	}

	public FileWrapper(Folder parent, File file, String path) {
		this.folder = parent;
		this.file = file;
		this.relpath = path;
		this.dateFormat = DateFormat.getDateTimeInstance();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public File getFile() {
		return this.file;
	}

	public String getId() {
		String s = null;
		try {
			s = URLEncoder.encode(
					this.file.getName() + "."
							+ Long.toString(this.file.lastModified()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			//
		}
		return s;
	}

	public boolean getIsEditable() {
		if (this.file.isDirectory()) {
			return true;
			// it is not really "editable", but the doEdit action should be
			// called to
			// change directory
		}
		String filename = this.file.getName();
		if (filename.lastIndexOf(".") > 0) {
			String filetype = filename.substring(filename.lastIndexOf("."));
			return this.isEditable(filetype);
		}
		return false;
	}

	private boolean isEditable(String filetype) {
		if (filetype != null && !"".equals(filetype)) {
			for (int i = 0; i < Global.editables.length; i++) {
				if (Global.editables[i].equals(filetype)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getName() {
		return this.file.getName();
	}

	// return the virtual path
	public String getRelativePath() {
		return this.relpath;
	}

	public String toString() {
		return this.file.toString();
	}

	public String getSize() {
		long l;

		l = this.file.length();

		if (this.file.isDirectory()) {
			l = FileUtils.sizeOfDirectory(this.file);
		}
		return FileUtils.byteCountToDisplaySize(l);
	}

	public boolean getIsDirectory() {
		return this.file.isDirectory();
	}

	public String getType() {
		return getIsDirectory() ? "dir" : "file";
	}

	public String getLastModified() {
		long l = this.file.lastModified();
		String s = this.dateFormat.format(new Date(l));
		return s;
	}

	public int getUid() {
		return _uid;
	}

	public static void setNextUid(int uid) {
		_nextuid = uid;
	}
}