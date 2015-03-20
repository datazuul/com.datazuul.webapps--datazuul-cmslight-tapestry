package com.datazuul.webapps.cmslight.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * List of all templates (lying under ".vorlagen" directory).
 * 
 * @author Ralf Eichinger
 */
public class TemplateSelectionModel implements IPropertySelectionModel {
	private List templates = new ArrayList();

	public TemplateSelectionModel(List templates) {
		super();
		this.templates.add(0, null);
		this.templates.addAll(templates);
	}

	public int getOptionCount() {
		return templates.size();
	}

	public Object getOption(int index) {
		return templates.get(index);
	}

	public String getLabel(int index) {
		if (index == 0) {
			return "";
		}
		File f = (File) templates.get(index);
		return f.getName();
	}

	public String getValue(int index) {
		return Integer.toString(index);
	}

	public Object translateValue(String value) {
		int index = Integer.parseInt(value);
		return getOption(index);
	}
}
