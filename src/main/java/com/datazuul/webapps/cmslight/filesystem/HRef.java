package com.datazuul.webapps.cmslight.filesystem;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A hyperlink.
 * 
 * @author Ralf Eichinger
 */
public class HRef implements Serializable {
	private static final long serialVersionUID = 3257003241907433776L;

	private String display;

	private String link;

	public HRef(String display, String link) {
		this.display = display;
		if ("/".equals(this.display)) {
			this.display = "TOP";
		}
		if (link == null || link.equals("")) {
			this.link = "/";
		} else {
			try {
				this.link = URLEncoder.encode(link, "ISO8859-15");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean getIsActive() {
		return link != null;
	}

	public String getDisplay() {
		return display;
	}

	public String getLink() {
		return link;
	}
}
