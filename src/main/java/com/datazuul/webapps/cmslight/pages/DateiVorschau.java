package com.datazuul.webapps.cmslight.pages;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;

import com.datazuul.webapps.cmslight.Visit;
import com.datazuul.webapps.cmslight.filesystem.Folder;

/**
 * Page for previewing a HTML-page (webserver must be running).
 * 
 * @author Ralf Eichinger
 */
public abstract class DateiVorschau extends AppBasePage implements
		PageRenderListener {
	private static final Logger LOG = Logger.getLogger(DateiVorschau.class);

	public abstract String getFilepath();

	public abstract void setFilepath(String filepath);

	private String filename;

	private String filecontent;

	public void pageBeginRender(PageEvent event) {
		logEnter(LOG, "DateiVorschau");
	}

	public String getFilecontent() {
		return this.filecontent;
	}

	public void setFilecontent(String content) {
		this.filecontent = content;
	}

	public void setFilename(String fileName) {
		this.filename = fileName;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getAbsoluteUrl() {
		Visit visit = (Visit) getVisit();
		String websiteUrl = visit.getUser().getFirstWebsite().getUrl();
		return websiteUrl + getFilepath();
	}

	public void formSubmit(IRequestCycle cycle) {
	}

	public void saveItem(IRequestCycle cycle) {
		Visit visit = (Visit) getVisit();
		File previewFile = visit.getPreviewFile();
		File originalFile = visit.getLastFile().getFile();

		String formFilename = getFilename();
		String originalFilename = originalFile.getName();
		boolean deleted;
		boolean renamed;
		if (!originalFilename.equals(formFilename)) {
			Folder parent = (Folder) visit.getLastFolder();
			File parentFile = parent.getFile();
			File newFile = new File(parentFile, formFilename);
			deleted = newFile.delete();
			renamed = previewFile.renameTo(newFile);
			deleted = originalFile.delete();
		} else {
			deleted = originalFile.delete();
			renamed = previewFile.renameTo(originalFile);
		}
		LOG.info("DateiVorschau.saveItem: Datei " + originalFilename
				+ " gespeichert");
		cycle.activate("DateiManager");

	}

	public void cancel(IRequestCycle cycle) {
		Visit visit = (Visit) getVisit();
		File previewFile = visit.getPreviewFile();
		boolean deleted = previewFile.delete();
		DateiBearbeiten next = (DateiBearbeiten) cycle
				.getPage("DateiBearbeiten");
		next.setFileContent(getFilecontent());
		cycle.activate(next);
	}
}
