/**
 * 
 */
package plumb.client.widget.form;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author bkhadige
 *
 */
public class DateFormField extends FormField {

	private static SimpleFormFieldUiBinder uiBinder = GWT
			.create(SimpleFormFieldUiBinder.class);

	interface SimpleFormFieldUiBinder extends UiBinder<Widget, DateFormField> {
	}

	public DateFormField() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	public DateFormField(boolean mandatory, String labelText) {
		super(mandatory, labelText);
		initWidget(uiBinder.createAndBindUi(this));
		
		// Create a DateBox
	    DateTimeFormat dateFormat = DateTimeFormat.getLongDateFormat();
	    
	    field.setFormat(new DateBox.DefaultFormat(dateFormat));
	}

	@UiField(provided=true)
	DateBox field = new DateBox();;
	
	@UiHandler("field")
	void onBlur(ValueChangeEvent<Date> e) {
		validate();
	}

	public void setText(String text) {

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
		field.setValue((Date) value);
	}

	@Override
	public void setValue(Object value, boolean fireEvents) {
		field.setValue((Date) value, fireEvents);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HandlerRegistration addValueChangeHandler(@SuppressWarnings("rawtypes") ValueChangeHandler handler) {
		return field.addValueChangeHandler(handler);
	}

}
