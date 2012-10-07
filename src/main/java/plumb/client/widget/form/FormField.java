/**
 * 
 */
package plumb.client.widget.form;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import plumb.client.validator.Validator;
import plumb.shared.validation.ValidationType;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author bkhadige
 * 
 */
public abstract class FormField extends Composite implements HasValue {

	static final String ERR_VALEUR_NUMERIQUE_ATTENDUE = "valeur numerique attendue";

	static final String ERR_CHAMP_OBLIGATOIRE = "champ obligatoire";

	static final String ERR_CHAMP_EMAIL = "type mail attendu";

	boolean mandatory = false;

	Set<ValidationType> validation = new HashSet<ValidationType>();

	@UiField(provided = true)
	Label label = new Label();

	@UiField
	HTML error;

	public FormField() {
		
	}
	
	void validate() {
		error.setHTML("");
		HasValue<?> field = (HasValue<?>) getField();
		boolean empty = field.getValue() == null || "".equals(field.getValue());
		if (mandatory && empty) {
			final SafeHtml errorMessage = new SafeHtmlBuilder().appendEscaped(
					ERR_CHAMP_OBLIGATOIRE).toSafeHtml();
			error.setHTML(errorMessage);
		} else {
			if (! empty) {
				runValidators(field);
			}
		}
	}

	/**
	 * @param field
	 */
	private void runValidators(HasValue<?> field) {
		if (validation != null && validation.size() > 0) {
			for (Iterator<ValidationType> iterator = validation.iterator(); iterator.hasNext();) {
				ValidationType v = iterator.next();
				if (v.equals(ValidationType.EMAIL)) {
					if (!Validator.EMAIL_VALIDATOR
							.validate((String) field.getValue())) {
						final SafeHtml errorMessage = new SafeHtmlBuilder()
								.appendEscaped(ERR_CHAMP_EMAIL)
								.toSafeHtml();
						error.setHTML(errorMessage);
					}
				}
				if (v.equals(ValidationType.NUMBER)) {
					if (!Validator.NUMBER_VALIDATOR.validate((String) field.getValue())) {
						final SafeHtml errorMessage = new SafeHtmlBuilder()
								.appendEscaped(
										ERR_VALEUR_NUMERIQUE_ATTENDUE)
								.toSafeHtml();
						error.setHTML(errorMessage);
					}
				}
			}
		}
	}

	public void addValidationType(ValidationType t) {
		this.validation.add(t);
	}


	public FormField(boolean mandatory, String labelText) {
		this.mandatory = mandatory;
		label = new Label(labelText);
		setLabel(labelText);
	}

	public void setLabel(String _label) {
		if (mandatory) {
			this.label.setText(_label + " * ");
		} else {
			this.label.setText(_label);
		}
	}

	/**
	 * @param width
	 * 
	 */
	public void setWidth(String width) {
		getField().setWidth(width);
	}

	protected abstract Widget getField();

	public boolean hasError() {
		return ! "".equals(error.getText()); // FIXME add property
	}

}
