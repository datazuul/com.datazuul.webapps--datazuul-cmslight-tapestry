package com.datazuul.webapps.cmslight.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.EnumPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.datazuul.webapps.cmslight.Constants;
import com.datazuul.webapps.cmslight.Visit;
import com.datazuul.webapps.cmslight.beans.IUser;
import com.datazuul.webapps.cmslight.filesystem.FileWrapper;
import com.datazuul.webapps.cmslight.filesystem.Folder;
import com.datazuul.webapps.cmslight.filesystem.HRef;
import com.datazuul.webapps.cmslight.models.DateiManagerActionEnumeration;

/**
 * Page providing list of all files in a directory and all associated actions.
 * 
 * @author Ralf Eichinger
 */
public abstract class DateiManager extends AppBasePage implements
		PageRenderListener {
	private static final Logger LOG = Logger.getLogger(DateiManager.class);

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyyMMdd");

	public abstract String getFilepath();

	public abstract void setFilepath(String filepath);

	public abstract HRef getParent();

	public abstract void setParent(HRef parent);

	public abstract int getParentIndex();

	public abstract void setParentIndex(int index);

	public abstract ArrayList getFileList();

	public abstract void setFileList(ArrayList fileList);

	public abstract FileWrapper getDeleteItem();

	public abstract FileWrapper getEditItem();

	public abstract FileWrapper getItem();

	public abstract void setItem(FileWrapper item);

	public abstract HashMap getSelectedItems();

	public abstract void setSelectedItems(HashMap selectedItems);

	private DateiManagerActionEnumeration action = DateiManagerActionEnumeration.ACTION_NONE;

	private IPropertySelectionModel actionModel;

	public IPropertySelectionModel getActionModel() {
		if (actionModel == null)
			actionModel = buildActionModel();
		return actionModel;
	}

	private IPropertySelectionModel buildActionModel() {
		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.datazuul.webapps.cmslight.FileActionStrings", getLocale());
		return new EnumPropertySelectionModel(
				DateiManagerActionEnumeration.ALL_VALUES, bundle);
	}

	public void pageBeginRender(PageEvent event) {
		logEnter(LOG, "DateiManager");

		ArrayList list = getFileList();

		if (list == null || getFilepath() != null) {
			FileWrapper.setNextUid(0);
			Visit visit = (Visit) getVisit();
			IUser user = visit.getUser();
			String docroot = user.getFirstWebsite().getDocumentRoot();
			if (visit.getTemplateDir() == null) {
				visit.setTemplateDir(getTemplateDir(docroot));
			}

			String filepath = getFilepath();
			File file = null;
			Folder folder = null;

			if (filepath == null || filepath.equals("")
					|| filepath.indexOf("\\") >= 0
					|| filepath.indexOf("..") >= 0) {
				Folder lastFolder = (Folder) visit.getLastFolder();
				if (lastFolder == null) {
					file = new File(docroot);
				} else {
					// use last folder
					lastFolder.load(); // refresh it
					folder = lastFolder;
				}
			} else {
				if (filepath.startsWith("/")) {
					filepath = filepath.substring(1);
				}
				file = new File(docroot, filepath);
			}

			if (folder == null && file.exists()) {
				if (file.isDirectory()) {
					folder = new Folder(file, docroot);
					folder.load();
				} else {
					folder = new Folder(file.getParentFile(), docroot);
					folder.load();
				}
				visit.setLastFolder(folder);
			}
			if (folder != null) {
				FileWrapper[] files = folder.getFiles();
				list = new ArrayList();
				for (int i = 0; i < files.length; i++) {
					list.add(files[i]);
				}
				setFileList(list);
				if (!event.getRequestCycle().isRewinding()) {
					LOG.info("DateiManager.pageBeginRender: Verzeichnis "
							+ folder.getFile().getPath());
				}
				setFilepath(folder.getRelativePath());
			} else {
				Home page = (Home) event.getRequestCycle().getPage("Home");
				page.addErrorMessage(getMessage("error.folder.does.not.exist"));
				event.getRequestCycle().activate(page);
				throw new PageRedirectException(page);
			}
		}
	}

	public boolean getCheckboxSelected() {
		return getSelectedItems().containsKey(getItem().getRelativePath());
	}

	public void setCheckboxSelected(boolean bSelected) {
		FileWrapper item = getItem();
		HashMap selectedItems = getSelectedItems();

		if (bSelected)
			selectedItems.put(item.getRelativePath(), item);
		else
			selectedItems.remove(item.getRelativePath());

		// persist value
		setSelectedItems(selectedItems);
	}

	public DateiManagerActionEnumeration getAction() {
		return action;
	}

	public String getItemUrl() {
		Visit visit = (Visit) getVisit();
		String path = getFilepath();
		if (path == null) {
			path = "";
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.length() > 0) {
			path += "/";
		}
		return visit.getUser().getFirstWebsite().getUrl() + path
				+ getItem().getName();
	}

	public void setAction(DateiManagerActionEnumeration action) {
		this.action = action;
	}

	public void formSubmit(IRequestCycle cycle) {
		FileWrapper deleteItem = getDeleteItem();
		FileWrapper editItem = getEditItem();

		ArrayList items = getFileList();
		for (int i = 0; i < items.size(); i++) {
			FileWrapper file = (FileWrapper) items.get(i);

			// delete item
			if (deleteItem != null
					&& file.getFile().getAbsolutePath()
							.equals(deleteItem.getFile().getAbsolutePath())) {
				DateiLoeschenBestaetigung page = (DateiLoeschenBestaetigung) cycle
						.getPage("DateiLoeschenBestaetigung");
				page.init(cycle, file);
				break;
			}

			// edit item
			if (editItem != null
					&& file.getFile().getAbsolutePath()
							.equals(editItem.getFile().getAbsolutePath())) {
				Visit visit = (Visit) getVisit();
				visit.setLastFile(file);
				cycle.activate("DateiBearbeiten");
				break;
			}
		}
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

	public void init(IRequestCycle cycle, String navigationTarget) {
		try {
			setFilepath(URLDecoder.decode(navigationTarget, "ISO8859-15"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		cycle.activate(this);
	}

	/**
	 * Listener invoked to allow a user to edit a file.
	 */
	public void editFile(IRequestCycle cycle) {
		Object[] parameters = cycle.getServiceParameters();
		Integer itemId = (Integer) parameters[0];
		FileWrapper item = (FileWrapper) getFileList().get(itemId.intValue());
		if (item.getIsDirectory()) {
			DateiManager page = (DateiManager) cycle.getPage("DateiManager");
			page.init(cycle, item.getRelativePath());
		} else {
			Visit visit = (Visit) getVisit();
			visit.setLastFile(item);
			cycle.activate("DateiBearbeiten");
		}
	}

	public void addNewItem(IRequestCycle cycle) {
		DateiNeu page = (DateiNeu) cycle.getPage("DateiNeu");
		page.init(cycle, getFilepath());
	}

	public void doAction(IRequestCycle cycle) {
		ArrayList selectedItems = new ArrayList();
		selectedItems.addAll(getSelectedItems().values());
		if (selectedItems.size() == 0) {
			DateiManager page = (DateiManager) cycle.getPage("DateiManager");
			page.addErrorMessage(getMessage("error.no.item.selected"));
			cycle.activate(page);
			return;
		}
		if (action.equals(DateiManagerActionEnumeration.ACTION_DELETE)) {
			DateiLoeschenBestaetigung page = (DateiLoeschenBestaetigung) cycle
					.getPage("DateiLoeschenBestaetigung");
			page.init(cycle, selectedItems);
			cycle.activate(page);
			return;
		} else if (action.equals(DateiManagerActionEnumeration.ACTION_DOWNLOAD)) {
			try {
				String filename = "backup-" + formatter.format(new Date())
						+ ".zip";
				HttpServletResponse response = cycle.getRequestContext()
						.getResponse();
				response.setHeader("Content-disposition", "inline; filename="
						+ filename);
				response.setContentType("application/zip");

				ZipOutputStream out = new ZipOutputStream(
						response.getOutputStream());
				for (int i = 0; i < selectedItems.size(); ++i) {
					FileWrapper fw = (FileWrapper) selectedItems.get(i);
					File file = fw.getFile();
					LOG.info("DateiManager.doAction: add '" + file.getPath()
							+ "' to zip");

					if (!file.isDirectory()) {
						zipFile(file, out);
					} else {
						zipDir(file, out);
					}
				}
				response.flushBuffer();
				out.close();
			} catch (IOException e) {
				System.err.println(e.toString());
			}
		} else {
			cycle.activate("DateiManager");
		}
	}

	/**
	 * @param docroot
	 */
	private File getTemplateDir(String docroot) {
		File templateDir = new File(docroot + File.separator
				+ Constants.DIRNAME_TEMPLATES);
		if (!templateDir.exists()) {
			if (!templateDir.mkdir()) {
				throw new RuntimeException("Could not create directory '"
						+ templateDir.getAbsolutePath() + "'");
			}
		}
		return templateDir;
	}

	private void zipDir(File zipDir, ZipOutputStream zos) {
		try {
			// get a listing of the directory content
			String[] dirList = zipDir.list();
			byte[] readBuffer = new byte[4096];
			int bytesIn = 0;
			// loop through dirList, and zip the files
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(zipDir, dirList[i]);
				if (f.isDirectory()) {
					// if the File object is a directory, call this
					// function again to add its content recursively
					zipDir(f, zos);
				} else {
					// if we reached here, the File object f was not a directory
					// create a FileInputStream on top of f
					zipFile(f, zos);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param file
	 * @param out
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void zipFile(File file, ZipOutputStream out)
			throws FileNotFoundException, IOException {
		byte[] buf = new byte[4096];
		FileInputStream in = new FileInputStream(file);

		Visit visit = (Visit) getVisit();
		Folder parent = visit.getLastFolder();
		int offset = parent.getFile().getPath().length();

		String name = file.getPath().substring(offset);
		ZipEntry entry = new ZipEntry(name);
		entry.setSize(file.length());
		entry.setTime(file.lastModified());
		out.putNextEntry(entry);
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
	}
}
