package com.datazuul.webapps.cmslight.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;

import com.datazuul.webapps.cmslight.filesystem.FileWrapper;

/**
 * Deleting file/directory after confirmation.
 * 
 * @author Ralf Eichinger
 */
public abstract class DateiLoeschenBestaetigung extends AppBasePage implements
		PageRenderListener {
	private static final Logger LOG = Logger
			.getLogger(DateiLoeschenBestaetigung.class);

	private ArrayList items;

	public abstract FileWrapper getItem();

	public abstract void setItem(FileWrapper item);

	public ArrayList getItems() {
		return this.items;
	}

	public void setItems(ArrayList items) {
		this.items = items;
	}

	public void pageBeginRender(PageEvent event) {
		logEnter(LOG, "DateiLoeschenBestaetigung");
	}

	public void init(IRequestCycle cycle, ArrayList items) {
		setItem(null);
		setItems(items);
		cycle.activate(this);
	}

	public String getQuestion() {
		if (getItem() != null) {
			return format("question.item", getItem().getName());
		} else if (getItems() != null) {
			return getMessage("question.items");
		}
		return null;
	}

	public void formSubmit(IRequestCycle cycle) {
	}

	public void deleteItem(IRequestCycle cycle) {
		if (getItem() != null) {
			File file = getItem().getFile();
			boolean success = deleteFile(file);
			if (success) {
				DateiManager page = (DateiManager) cycle
						.getPage("DateiManager");
				page.setFileList(null);
				cycle.activate(page);
			}
		} else if (getItems() != null) {
			boolean success = true;
			ArrayList items = getItems();
			for (Iterator iter = items.iterator(); iter.hasNext();) {
				FileWrapper element = (FileWrapper) iter.next();
				File file = element.getFile();
				success &= deleteFile(file);
			}
			if (success) {
				DateiManager page = (DateiManager) cycle
						.getPage("DateiManager");
				page.setFileList(null);
				page.setSelectedItems(new HashMap());
				cycle.activate(page);
			}
		} else {
			DateiManager page = (DateiManager) cycle.getPage("DateiManager");
			page.addErrorMessage(getMessage("error.app.property.not.set"));
			cycle.activate(page);
			return;
		}
	}

	private boolean deleteFile(File file) {
		boolean rc = true;
		if (file.isFile()) {
			try {
				boolean deleted = file.delete();
				if (!deleted) {
					LOG.error("DateiLoeschenBestaetigung.deleteFile: Datei "
							+ file.getAbsolutePath()
							+ " konnte nicht geloescht werden");
					addErrorMessage(format("error.file.delete", file.getName()));
					rc = false;
				} else {
					LOG.info("DateiLoeschenBestaetigung.deleteFile: Datei "
							+ file.getAbsolutePath() + " geloescht");
				}
			} catch (SecurityException e) {
				LOG.error("DateiLoeschenBestaetigung.deleteFile: Datei "
						+ file.getAbsolutePath()
						+ " konnte nicht geloescht werden");
				addErrorMessage(format("error.file.delete", file.getName()));
				rc = false;
			}
		} else {
			rc = deleteDirectory(file);
			if (rc) {
				LOG.info("DateiLoeschenBestaetigung.deleteFile: Verzeichnis "
						+ file.getAbsolutePath() + " geloescht");
			} else {
				LOG.error("DateiLoeschenBestaetigung.deleteFile: Verzeichnis "
						+ file.getAbsolutePath()
						+ " konnte nicht geloescht werden");
			}
		}
		return rc;
	}

	public void cancel(IRequestCycle cycle) {
		setItem(null);
		cycle.activate("DateiManager");

	}

	public void init(IRequestCycle cycle, FileWrapper file) {
		setItems(null);
		setItem(file);
		cycle.activate(this);
	}

	private boolean deleteDirectory(File directory) {
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDirectory(files[i]);
			} else {
				files[i].delete();
			}
		}
		return (directory.delete());
	}
}