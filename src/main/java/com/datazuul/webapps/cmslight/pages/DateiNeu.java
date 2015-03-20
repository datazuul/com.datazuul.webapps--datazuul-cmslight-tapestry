package com.datazuul.webapps.cmslight.pages;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.request.IUploadFile;

import com.datazuul.webapps.cmslight.Constants;
import com.datazuul.webapps.cmslight.Visit;
import com.datazuul.webapps.cmslight.filesystem.Folder;
import com.datazuul.webapps.cmslight.filesystem.HRef;
import com.datazuul.webapps.cmslight.models.TemplateSelectionModel;

/**
 * Page for creating/uploading a new file/directory.
 * 
 * @author Ralf Eichinger
 */
public abstract class DateiNeu extends AppBasePage implements
		PageRenderListener {
	private static final Logger LOG = Logger.getLogger(DateiNeu.class);

	/* Navigationslink */
	public abstract int getParentIndex();

	public abstract void setParentIndex(int index);

	public abstract HRef getParent();

	public abstract void setParent(HRef parent);

	public abstract String getFilepath();

	public abstract void setFilepath(String filepath);

	public abstract IUploadFile getFile();

	public abstract String getFileName();

	public abstract String getFileType();

	public abstract void setFileType(String filetype);

	public abstract File getTemplate();

	public abstract void setTemplate(File o);

	private static final String TYPE_DIRECTORY = "directory";

	private static final String TYPE_FILE = "file";

	private TemplateSelectionModel templatesModel;

	public void pageBeginRender(PageEvent event) {
		logEnter(LOG, "DateiNeu");

		setFileType(TYPE_FILE);
	}

	public void formSubmit(IRequestCycle cycle) {

	}

	public IPropertySelectionModel getTemplatesModel() {
		if (this.templatesModel == null)
			this.templatesModel = buildTemplatesModel();
		return (IPropertySelectionModel) this.templatesModel;
	}

	private TemplateSelectionModel buildTemplatesModel() {
		ArrayList templates = new ArrayList();
		Visit visit = (Visit) getVisit();
		File templateDir = visit.getTemplateDir();
		File[] children = templateDir.listFiles();
		for (int i = 0; i < children.length; i++) {
			File child = children[i];
			if (!child.isDirectory()) {
				templates.add(child);
			}
		}
		TemplateSelectionModel model = new TemplateSelectionModel(templates);
		return model;
	}

	/**
	 * Listener invoked to change directory.
	 */
	public void changeDirectory(IRequestCycle cycle) {
		Object[] parameters = cycle.getServiceParameters();
		String navigationTarget = (String) parameters[0];
		DateiManager page = (DateiManager) cycle.getPage("DateiManager");
		page.init(cycle, navigationTarget);
	}

	public void saveItem(IRequestCycle cycle) {
		Visit visit = (Visit) getVisit();
		String filename = getFileName();
		String filetype = getFileType();
		File template = getTemplate();
		IUploadFile uploadFile = getFile();

		LOG.info("DateiNeu.saveItem: Dateiname: " + getFileName());
		LOG.info("DateiNeu.saveItem: Dateityp: " + getFileType());
		if (getTemplate() != null) {
			LOG.info("DateiNeu.saveItem: Vorlage: " + getTemplate().getName());
		}

		if (getFile() != null) {
			LOG.info("DateiNeu.saveItem: Content-Type: "
					+ getFile().getContentType());
		}

		if ((filename == null || filename.equals(""))
				&& Constants.FILE.equals(filetype)) {
			// prio 1
			if (uploadFile != null && uploadFile.getSize() > 0) {
				filename = uploadFile.getFileName();
			} else {
				if (template != null) {
					// prio 2
					filename = template.getName();
				}
			}
		}

		Folder parent = visit.getLastFolder();
		File parentFile = parent.getFile();
		File newFile = new File(parentFile, filename);
		if (!newFile.getParentFile().getAbsolutePath()
				.equals(parentFile.getAbsolutePath())) {
			addErrorMessage(getMessage("error.file.invalid.name"));
			return;
		}
		boolean created = false;
		try {
			if (filetype.equals(Constants.FILE)) {
				// fill file with content
				if (uploadFile.getFileName() != null
						&& !"".equals(uploadFile.getFileName())) {// prio 1
					// create file
					created = newFile.createNewFile();
					if (created) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						InputStream stream = uploadFile.getStream();
						// write the file to the file specified
						OutputStream bos = new FileOutputStream(newFile);
						int bytesRead = 0;
						byte[] buffer = new byte[8192];
						while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
							bos.write(buffer, 0, bytesRead);
						}
						bos.close();
						// close the stream
						stream.close();
					}
				} else if (template != null) {
					if (!newFile.exists()) {
						FileUtils.copyFile(template, newFile);
						created = true;
					}
				} else {
					created = newFile.createNewFile();
				}
			} else if (filetype.equals(Constants.DIRECTORY)) {
				created = newFile.mkdir();
			}
			if (created == false) {
				addErrorMessage(format("error.file.exists", newFile.getName()));
				return;
			}
		} catch (IOException e) {
			addErrorMessage(format("error.file.write", newFile.getName()));
			return;
		} catch (SecurityException se) {
			addErrorMessage(format("error.file.write", newFile.getName()));
			return;
		}
		LOG.info("DateiNeu.saveItem: Datei " + newFile.getAbsolutePath()
				+ " angelegt");
		cycle.activate("DateiManager");
	}

	public void init(IRequestCycle cycle, String filepath) {
		setFilepath(filepath);
		cycle.activate(this);
	}
}
