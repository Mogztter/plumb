/**
 *
 */
package plumb.client.widget.form;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author bkhadige
 */
public class SuggestFormField extends FormField {

	private static SuggestFormFieldUiBinder uiBinder = GWT
			.create(SuggestFormFieldUiBinder.class);

	private boolean mandatory = false;
	private final MultiWordSuggestOracle suggestOracle = new MultiWordSuggestOracle();

	interface SuggestFormFieldUiBinder extends UiBinder<Widget, SuggestFormField> {
	}

	public SuggestFormField() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SuggestFormField(boolean mandatory, String labelText) {
		this.mandatory = mandatory;
		label = new Label(labelText);
		if (mandatory) {
			label.setText(label.getText() + " (*) ");
		}
		initWidget(uiBinder.createAndBindUi(this));
		field.addStyleName("suggestformField");
	}

	@Override
	protected Widget getField() {
		return field;
	}

	@UiField(provided = true)
	Label label = new Label();


	@UiField(provided = true)
	SuggestBox field = new SuggestBox(suggestOracle);

	@UiField
	TextBox description;

	@UiField
	HTML error;

	@UiHandler("field")
	void onKeyUp(KeyUpEvent e) {
		error.setHTML("");
		if (mandatory) {
			if (field.getText() == null || "".equals(field.getText())) {
				final SafeHtml errorMessage = new SafeHtmlBuilder().appendEscaped("Champ obligatoire").toSafeHtml();
				error.setHTML(errorMessage);
			}
		}
	}

	public void setText(String text) {
		if (text != null) {
			field.setText(text);
		} else {
			throw new RuntimeException("null value is not supported");
		}
	}

	public String getText() {
		return field.getText();
	}

	public void setResult(String result) {
		this.description.setText(result);
	}

	public void setInvalidResult() {
		error.setText("recherche invalide");
	}

	public void showSuggestionList() {
		field.showSuggestionList();
	}

	public void setSuggestion(String[] suggestions) {
		suggestOracle.clear();
		suggestOracle.setDefaultSuggestionsFromText(Arrays.asList(suggestions));
	}

	@Override
	public Object getValue() {
		return field.getValue();
	}

	@Override
	public void setValue(Object value) {
		field.setValue((String) value);
	}

	@Override
	public void setValue(Object value, boolean fireEvents) {
		field.setValue((String) value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		// TODO
		return null;
	}
}
