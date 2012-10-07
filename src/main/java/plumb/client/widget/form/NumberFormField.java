/**
 * 
 */
package plumb.client.widget.form;

import plumb.shared.validation.ValidationType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author bkhadige
 *
 */
public class NumberFormField extends FormField  {

	private static NumberFormFieldUiBinder uiBinder = GWT
			.create(NumberFormFieldUiBinder.class);

	interface NumberFormFieldUiBinder extends UiBinder<Widget, NumberFormField> {
	}

	public NumberFormField() {
		initWidget(uiBinder.createAndBindUi(this));
		addValidationType(ValidationType.NUMBER);
	}
	
	public NumberFormField(boolean mandatory, String labelText) {
		super(mandatory, labelText);
		initWidget(uiBinder.createAndBindUi(this));
		addValidationType(ValidationType.NUMBER);
	}
	
	@UiField
	TextBox field;
	
	@UiHandler("field")
	void onBlur(BlurEvent e) {
		validate();
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Widget getField() {
		return field;
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
		field.setValue((String) value, fireEvents);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HandlerRegistration addValueChangeHandler(@SuppressWarnings("rawtypes") ValueChangeHandler handler) {
		return field.addValueChangeHandler(handler);
	}

}
