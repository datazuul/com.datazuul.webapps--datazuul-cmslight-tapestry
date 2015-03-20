package com.datazuul.webapps.cmslight.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;

import com.datazuul.webapps.cmslight.Global;
import com.datazuul.webapps.cmslight.Visit;
import com.datazuul.webapps.cmslight.beans.IUser;
import com.datazuul.webapps.cmslight.filesystem.FileWrapper;
import com.datazuul.webapps.cmslight.filesystem.Folder;

/**
 * Page for editing a textfile.
 * 
 * @author Ralf Eichinger
 */
public abstract class DateiBearbeiten extends AppBasePage implements
		PageRenderListener {
	private static final Logger LOG = Logger.getLogger(DateiBearbeiten.class);

	private boolean marked;

	public final static String CMS_ENDE = "<!-- PIXOCMS-ENDE -->";

	public final static String CMS_START = "<!-- PIXOCMS-START -->";

	private static final String TXT_FILE_CONTENT = "txtFileContent";

	public abstract String getFileContent();

	public abstract void setFileContent(String fileContent);

	public abstract String getFileName();

	public abstract void setFileName(String fileName);

	public void pageBeginRender(PageEvent event) {
		logEnter(LOG, "DateiBearbeiten");

		Visit visit = (Visit) getVisit();
		FileWrapper fileWrapper = visit.getLastFile();
		setFileName(fileWrapper.getName());

		LOG.info("DateiBearbeiten.pageBeginRender: Datei " + getFileName());

		File file = fileWrapper.getFile();
		String content = getFileContent();
		if (content == null) {
			try {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis,
						"ISO-8859-15");
				int filesize = (int) file.length();
				char[] data = new char[filesize];
				isr.read(data, 0, filesize);
				isr.close();
				content = new String(data);
			} catch (FileNotFoundException exc) {
				addErrorMessage(format("error.file.not.found", getFileName()));
				return;
			} catch (IOException exc) {
				addErrorMessage(format("error.file.read", getFileName()));
				return;
			}
		}
		if (content.indexOf(CMS_START) >= 0) {
			visit.setLastFilecontent(content);
			int start = content.indexOf(CMS_START);
			int ende = content.indexOf(CMS_ENDE);
			if (ende != -1) {
				content = content.substring(start, ende + CMS_ENDE.length());
			} else {
				content = content.substring(start);
			}
		} else {
			visit.setLastFilecontent(null);
		}
		setFileContent(content);
		if (!getRequestCycle().isRewinding()) {
			if (getFileContent() != null) {
				if (getFileContent().indexOf(CMS_START) != -1) {
					this.marked = true;
				} else {
					this.marked = false;
				}
			}
		}
	}

	public Map getScriptSymbols() {
		HashMap symbols = new HashMap(1);
		symbols.put("txtFileContent", getComponent(TXT_FILE_CONTENT));
		return symbols;
	}

	public void formSubmit(IRequestCycle cycle) {
	}

	public boolean getIsNotMarked() {
		return !getIsMarked();
	}

	public boolean getIsMarked() {
		return marked;
	}

	public boolean getHasPreview() {
		return this.hasPreview();
	}

	public boolean getHasNoPreview() {
		return !this.hasPreview();
	}

	private boolean hasPreview() {
		Visit visit = (Visit) getVisit();
		FileWrapper fileWrapper = visit.getLastFile();
		String filename = fileWrapper.getFile().getName();
		if (filename.lastIndexOf(".") > 0) {
			String filetype = filename.substring(filename.lastIndexOf("."));
			if (filetype != null && !"".equals(filetype)) {
				for (int i = 0; i < Global.previewables.length; i++) {
					if (Global.previewables[i].equals(filetype)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getFilepath() {
		Visit visit = (Visit) getVisit();
		return visit.getLastFile().getRelativePath();
	}

	public String getLink() {
		Visit visit = (Visit) getVisit();
		try {
			return URLEncoder.encode(visit.getLastFolder().getRelativePath(),
					"ISO8859-15");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
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

	public void doMark(IRequestCycle cycle) {
		Visit visit = (Visit) getVisit();
		FileWrapper fileWrapper = visit.getLastFile();
		setFileName(fileWrapper.getName());
		File file = fileWrapper.getFile();

		if (file != null && file.exists() && file.isFile()) {
			try {
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos,
						"ISO-8859-15");
				String text = getFileContent();
				int textsize = text.length();
				osw.write(text, 0, textsize);
				osw.close();
			} catch (IOException exc) {
				addErrorMessage(format("error.file.write", getFileName()));
				return;
			}
			LOG.info("DateiBearbeiten.doMark: Bereich in Datei "
					+ getFileName() + " markiert");
			cycle.activate(this);
			return;
		} else {
			addErrorMessage(format("error.file.not.exists", getFileName()));
			cycle.activate(this);
			return;
		}
	}

	public void doDemark(IRequestCycle cycle) {
		Visit visit = (Visit) getVisit();
		FileWrapper fileWrapper = visit.getLastFile();
		setFileName(fileWrapper.getName());
		File file = fileWrapper.getFile();

		if (file != null && file.exists() && file.isFile()) {
			try {
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos,
						"ISO-8859-15");
				String text = getFileContent();
				if (visit.getLastFilecontent() != null) {
					String originalText = visit.getLastFilecontent();
					if (originalText.indexOf("PIXOCMS-START") >= 0) {
						String beforeText = originalText.substring(0,
								originalText.indexOf("<!-- PIXOCMS-START"));
						String afterText = originalText.substring(originalText
								.indexOf("PIXOCMS-ENDE -->") + 16);
						text = beforeText + text + afterText;
					}
				}
				int textsize = text.length();
				osw.write(text, 0, textsize);
				osw.close();
				setFileContent(text);
			} catch (IOException exc) {
				addErrorMessage(format("error.file.write", getFileName()));
				return;
			}
			LOG.info("DateiBearbeiten.doDemark: Bereich in Datei "
					+ getFileName() + " demarkiert");
			cycle.activate(this);
			return;
		} else {
			addErrorMessage(format("error.file.not.exists", getFileName()));
			cycle.activate(this);
			return;
		}
	}

	public void previewFile(IRequestCycle cycle) {
		Visit visit = (Visit) getVisit();
		IUser user = visit.getUser();
		String docroot = user.getFirstWebsite().getDocumentRoot();
		FileWrapper fileWrapper = visit.getLastFile();
		String content = getFileContent();

		boolean check = checkNewFilename();
		if (check == false) {
			// Neuer Dateiname widerspricht den Regeln.
			cycle.activate(this);
			return;
		}

		// Preview-Datei abspeichern (Festplatte und Visit)
		File file = fileWrapper.getFile();
		String filepath = fileWrapper.getRelativePath();

		if (file != null && file.exists() && file.isFile()) {
			String text = null;
			try {
				String fileEnd = file.getName().substring(
						file.getName().lastIndexOf("."));
				if (filepath.indexOf("/") != -1) {
					filepath = filepath.substring(0,
							filepath.lastIndexOf("/") + 1);
				} else {
					filepath = "";
				}
				filepath = filepath + "pixocms_preview" + fileEnd;
				file = new File(docroot, filepath);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos,
						"ISO-8859-15");
				text = getFileContent();
				if (visit.getLastFilecontent() != null) {
					String originalText = visit.getLastFilecontent();
					if (originalText.indexOf("PIXOCMS-START") >= 0) {
						String beforeText = originalText.substring(0,
								originalText.indexOf("<!-- PIXOCMS-START"));
						String afterText = originalText.substring(originalText
								.indexOf("PIXOCMS-ENDE -->") + 16);
						text = beforeText + text + afterText;
					}
				}
				int textsize = text.length();
				osw.write(text, 0, textsize);
				osw.close();
			} catch (IOException exc) {
				addErrorMessage(format("error.file.write", file.getName()));
				cycle.activate(this);
				return;
			}
			DateiVorschau next = (DateiVorschau) cycle.getPage("DateiVorschau");
			next.setFilepath(filepath);
			next.setFilename(getFileName());
			next.setFilecontent(text);
			visit.setPreviewFile(file);
			cycle.activate(next);
		} else {
			addErrorMessage(format("error.file.not.exists",
					fileWrapper.getName()));
			cycle.activate(this);
			return;
		}
	}

	public void saveFile(IRequestCycle cycle) {
		Visit visit = (Visit) getVisit();
		IUser user = visit.getUser();
		String docroot = user.getFirstWebsite().getDocumentRoot();
		FileWrapper fileWrapper = visit.getLastFile();
		String content = getFileContent();

		boolean check = checkNewFilename();
		if (check == false) {
			// Neuer Dateiname widerspricht den Regeln.
			cycle.activate(this);
			return;
		}

		// Preview-Datei abspeichern (Festplatte und Visit)
		File file = fileWrapper.getFile();
		String filepath = fileWrapper.getRelativePath();

		if (file != null && file.exists() && file.isFile()) {
			String text = null;
			try {
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos,
						"ISO-8859-15");
				text = getFileContent();
				if (visit.getLastFilecontent() != null) {
					String originalText = visit.getLastFilecontent();
					if (originalText.indexOf("PIXOCMS-START") >= 0) {
						String beforeText = originalText.substring(0,
								originalText.indexOf("<!-- PIXOCMS-START"));
						String afterText = originalText.substring(originalText
								.indexOf("PIXOCMS-ENDE -->") + 16);
						text = beforeText + text + afterText;
					}
				}
				int textsize = text.length();
				osw.write(text, 0, textsize);
				osw.close();
			} catch (IOException exc) {
				addErrorMessage(format("error.file.write", file.getName()));
				cycle.activate(this);
				return;
			}
			DateiManager next = (DateiManager) cycle.getPage("DateiManager");
			cycle.activate(next);
		} else {
			addErrorMessage(format("error.file.not.exists",
					fileWrapper.getName()));
			cycle.activate(this);
			return;
		}
	}

	/**
	 * @return
	 */
	private boolean checkNewFilename() {
		Visit visit = (Visit) getVisit();
		IUser user = visit.getUser();
		String docroot = user.getFirstWebsite().getDocumentRoot();
		FileWrapper fileWrapper = visit.getLastFile();
		String formFilename = getFileName();
		String originalFilename = fileWrapper.getName();

		if (!originalFilename.equals(formFilename)) {
			Folder parent = visit.getLastFolder();
			File parentFile = parent.getFile();
			File newFile = new File(parentFile, formFilename);
			if (newFile.exists()) {
				addErrorMessage(format("error.file.already.exists",
						formFilename));
				LOG.info("DateiBearbeiten.checkNewFilename: Datei "
						+ formFilename + " existiert bereits");
				return false;
			}
			if (!newFile.getParentFile().getAbsolutePath()
					.equals(parentFile.getAbsolutePath())) {
				addErrorMessage(format("error.file.invalid.name", formFilename));
				LOG.info("DateiBearbeiten.checkNewFilename: Ungueltiger Dateiname "
						+ formFilename);
				return false;
			}
		}
		return true;
	}

}
