package com.datazuul.webapps.cmslight.validation;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidator;

/**
 * Validation delegate designing validated fields.
 * 
 * @author Ralf Eichinger
 */
public class ValidationDelegate extends
		org.apache.tapestry.valid.ValidationDelegate {
	public void writeLabelPrefix(IFormComponent component,
			IMarkupWriter writer, IRequestCycle cycle) {
		if (isInError(component)) {
			writer.begin("span");
			writer.attribute("class", "label-error");
		}
	}

	public void writeLabelSuffix(IFormComponent component,
			IMarkupWriter writer, IRequestCycle cycle) {
		if (isInError(component)) {
			writer.end(); // span
		}
	}

	public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle,
			IFormComponent component, IValidator validator) {
		if (isInError())
			writer.attribute("class", "field-error");
	}

	public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle,
			IFormComponent component, IValidator validator) {
		// Generally, validator won't be null, because this method
		// is invoked by a ValidField, which requires a non-null validator.
		// In chapter 8, the CreditCardField component breaks this rule,
		// as it hooks into the validation subsystem without actually
		// having a validator.

		if (validator != null && validator.isRequired()) {
			writer.printRaw("&nbsp;");
			writer.begin("span");
			writer.attribute("class", "required-marker");
			writer.print("*");
			writer.end();
		}

		if (isInError()) {
			writer.printRaw("&nbsp;");
			writer.beginEmpty("img");
			writer.attribute("src", "images/field-error.png");
			writer.attribute("width", 16);
			writer.attribute("height", 16);
		}
	}

}
