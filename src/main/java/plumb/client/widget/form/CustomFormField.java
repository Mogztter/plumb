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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author bkhadige
 *
 */
public class CustomFormField extends FormField implements HasBlurHandlers {

	private static CustomFormFieldUiBinder uiBinder = GWT
			.create(CustomFormFieldUiBinder.class);

	interface CustomFormFieldUiBinder extends UiBinder<Widget, CustomFormField> {
	}

	public CustomFormField() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public CustomFormField(boolean mandatory, String labelText, Widget w) {
		super(mandatory, labelText);
		// FIXME : widget has to be hasText and hasBlurHandlers
		this.field = w;
		initWidget(uiBinder.createAndBindUi(this));
		addDefaultBlurHandler();
	}
	
	@UiField(provided=true)
	Widget field;
	
	public final BlurHandler BLUR_HANDLER = new BlurHandler() {
		@Override
		public void onBlur(BlurEvent event) {
			error.setHTML("");
			error.setHTML("");
			if (mandatory && (((HasText) field).getText() == null || "".equals(((HasText) field).getText()))) {
				final SafeHtml errorMessage = new SafeHtmlBuilder().appendEscaped(ERR_CHAMP_OBLIGATOIRE).toSafeHtml();
				error.setHTML(errorMessage);
			}
		}
	};
	
	private void addDefaultBlurHandler() {
		addBlurHandler(BLUR_HANDLER);
	}

	public void setText(String text) {
		if (text != null) {
			((HasText) field).setText(text); 
		} else {
			throw new RuntimeException("null value is not supported");
		}
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return ((HasBlurHandlers) field).addBlurHandler(handler);
	}
	
	@Override
	protected Widget getField() {
		return field;
	}
	
	@Override
	public Object getValue() {
		return ((HasValue<?>) field).getValue();
	}

	@Override
	public void setValue(Object value) {
		((HasValue) field).setValue(value);
	}

	@Override
	public void setValue(Object value, boolean fireEvents) {
		((HasValue) field).setValue(value, fireEvents);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HandlerRegistration addValueChangeHandler(@SuppressWarnings("rawtypes") ValueChangeHandler handler) {
		return ((HasValue) field).addValueChangeHandler(handler);
	}

}
