/**
 *
 */
package plumb.client.widget.form;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/** @author bkhadige */
public class ListFormField extends FormField {

	private static ListFormFieldUiBinder uiBinder = GWT
			.create(ListFormFieldUiBinder.class);

	private boolean mandatory = false;
	private boolean valueChangeHandlerInitialized;

	interface ListFormFieldUiBinder extends UiBinder<Widget, ListFormField> {
	}

	public ListFormField() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public ListFormField(boolean mandatory, String labelText) {
		this.mandatory = mandatory;
		label = new Label(labelText);
		if (mandatory) {
			label.setText(label.getText() + " (*) ");
		}
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected Widget getField() {
		return field;
	}

	@UiField(provided = true)
	Label label = new Label();

	@UiField(provided = true)
	ListBox field = new ListBox(false);

	@UiHandler("field") void onKeyUp(KeyUpEvent e) {
		error.setHTML("");
		if (mandatory) {
			final String itemText = getResult();
			if (itemText == null || !itemText.isEmpty()) {
				final SafeHtml errorMessage = new SafeHtmlBuilder().appendEscaped("Champ obligatoire").toSafeHtml();
				error.setHTML(errorMessage);
			}
		}
	}

	public void setValues(List<String> values) {
		field.clear();
		if (values != null) {
			for (String value : values) {
				field.addItem(value);
			}
		}
	}

	@Override
	public Object getValue() {
		return getResult();
	}

	private String getResult() {
		return field.getItemText(field.getSelectedIndex());
	}

	@Override
	public void setValue(Object value) {
		setResult((String) value);
	}

	@Override
	public void setValue(Object value, boolean fireEvents) {
		setResult((String) value);
	}

	private void setResult(String result) {
		for (int i = 0; i < field.getItemCount(); i++) {
			if (result.equals(field.getItemText(i))) {
				field.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			field.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(ListFormField.this, getValue());
				}
			});
		}
		//return field.addHandler(handler, ValueChangeEvent.getType());
		return null;
	}

}
