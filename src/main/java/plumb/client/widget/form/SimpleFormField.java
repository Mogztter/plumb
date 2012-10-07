/**
 * 
 */
package plumb.client.widget.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
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
public class SimpleFormField extends FormField implements HasBlurHandlers {

	private static SimpleFormFieldUiBinder uiBinder = GWT
			.create(SimpleFormFieldUiBinder.class);

	interface SimpleFormFieldUiBinder extends UiBinder<Widget, SimpleFormField> {
	}

	public SimpleFormField() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public SimpleFormField(boolean mandatory, String labelText) {
		super(mandatory, labelText);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	TextBox field;

	@UiHandler("field")
	void onBlur(BlurEvent e) {
		validate();
	}

	public void setText(String text) {
		if (text != null) {
			field.setText(text); 
		} else {
			throw new RuntimeException("null value is not supported");
		}
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return field.addBlurHandler(handler);
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
