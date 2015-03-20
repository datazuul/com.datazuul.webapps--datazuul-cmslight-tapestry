package com.datazuul.webapps.cmslight.models;

import org.apache.commons.lang.enum.Enum;

/**
 * Enumeration of possible file actions.
 * 
 * @author Ralf Eichinger
 */
public class DateiManagerActionEnumeration extends Enum {
	private static final long serialVersionUID = 3689918361714570037L;

	public static final DateiManagerActionEnumeration ACTION_DELETE = new DateiManagerActionEnumeration(
			"DELETE");
	public static final DateiManagerActionEnumeration ACTION_DOWNLOAD = new DateiManagerActionEnumeration(
			"DOWNLOAD");
	public static final DateiManagerActionEnumeration ACTION_NONE = new DateiManagerActionEnumeration(
			"NONE");

	public static final DateiManagerActionEnumeration[] ALL_VALUES = {
			ACTION_DELETE, ACTION_DOWNLOAD, ACTION_NONE };

	private DateiManagerActionEnumeration(String name) {
		super(name);
	}
}