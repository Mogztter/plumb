package plumb.client.display.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import plumb.client.display.DisplayService;
import plumb.client.display.DisplayServiceAsync;
import plumb.client.widget.form.CustomFormField;
import plumb.client.widget.form.DateFormField;
import plumb.client.widget.form.FormField;
import plumb.client.widget.form.ListFormField;
import plumb.client.widget.form.NumberFormField;
import plumb.client.widget.form.SimpleFormField;
import plumb.client.widget.form.SuggestFormField;
import plumb.shared.display.DisplayBean;
import plumb.shared.display.DisplayField;
import plumb.shared.validation.ValidationType;

/**
 * @author bkhadige
 */
public class DisplayEditView<B extends DisplayBean> extends Composite {

	public static final String SUGGESTION_TYPE = "suggestion";
	public static final String LIST_TYPE = "list";

	private static final String STRING_TYPE = "java.lang.String";
	private static final String INT_TYPE = "int";
	private static final String DATE_TYPE = "java.util.Date";

	private B display;

	// fields having invalid values on flush()
	private Set<String> invalidFields = new HashSet<String>();

	private DriverDelegate<B> delegate = GWT.create(DriverDelegate.class);

	DisplayServiceAsync service = GWT.create(DisplayService.class);

	FlowPanel container = new FlowPanel();

	Map<DisplayField, Widget> fields = new HashMap<DisplayField, Widget>();

	Map<String, CustomWidget> customFields = new HashMap<String, CustomWidget>();

	Map<String, KeyPressHandler> customKeyPressHandler = new HashMap<String, KeyPressHandler>();
	Map<String, KeyDownHandler> customKeyDownHandler = new HashMap<String, KeyDownHandler>();
	Map<String, KeyUpHandler> customKeyUpHandler = new HashMap<String, KeyUpHandler>();

	public DisplayEditView() {
		initWidget(container);
	}

	public DisplayEditView(B display, Set<CustomWidget> customWidgets) {
		this();
		this.display = display;
		if (customWidgets != null) {
			for (CustomWidget customWidget : customWidgets) {
				customFields.put(customWidget.property, customWidget);
			}
		}
		populate(display);
	}

	public DisplayEditView(B display) {
		this(display, null);
	}

	public void addKeyPressHandler(final String fieldName, final KeyPressHandler keyPressHandler) {
		customKeyPressHandler.put(fieldName, keyPressHandler);
	}

	public void addKeyDownHandler(final String fieldName, final KeyDownHandler keyDownHandler) {
		customKeyDownHandler.put(fieldName, keyDownHandler);
	}

	public void addKeyUpHandler(final String fieldName, final KeyUpHandler keyUpHandler) {
		customKeyUpHandler.put(fieldName, keyUpHandler);
	}

	private void populate(B _display) {
		this.display = _display;
		service.resolve(_display,
				new AsyncCallback<List<DisplayField>>() {
					@Override
					public void onSuccess(List<DisplayField> properties) {
						for (DisplayField displayField : properties) {
							bindFieldToWidget(displayField);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
				});
	}

	/**
	 * @param field
	 */
	public void bindFieldToWidget(DisplayField field) {
		final Composite composite;
		final String fieldName = field.name;
		if (!customFields.containsKey(fieldName)) {
			final String fieldTypeName = field.typeName;
			if (DisplayEditView.STRING_TYPE.equals(fieldTypeName)) {
				composite = new SimpleFormField(field.mandatory, fieldName);
			} else if (DisplayEditView.INT_TYPE.equals(fieldTypeName)) {
				composite = new NumberFormField(field.mandatory, fieldName);
			} else if (DisplayEditView.DATE_TYPE.equals(fieldTypeName)) {
				composite = new DateFormField(field.mandatory, fieldName);
			} else if (DisplayEditView.SUGGESTION_TYPE.equals(fieldTypeName)) {
				final SuggestFormField suggestFormField = new SuggestFormField(field.mandatory, fieldName);
				suggestFormField.setSuggestion(field.suggestions);
				composite = suggestFormField;
			} else if (DisplayEditView.LIST_TYPE.equals(fieldTypeName)) {
				final ListFormField listFormField = new ListFormField(field.mandatory, fieldName);
				listFormField.setValues(Arrays.asList(field.list));
				composite = listFormField;
			}
			else {
				// TODO : add other types
				final ExceptionMessages exceptionMessages = GWT.create(ExceptionMessages.class);
				throw new UnsupportedOperationException(exceptionMessages.typeNotYetSupported(fieldTypeName));
			}

			ValidationType[] validation = field.validation;
			for (ValidationType aValidation : validation) {
				((FormField) composite).addValidationType(aValidation); // FIXME : refactor
			}
		} else {
			composite = new CustomFormField(field.mandatory, fieldName, customFields.get(fieldName).w);
		}
		setCustomProperties(field, composite);
		final Widget widget = composite.asWidget();
		bindFieldToEvent(fieldName, widget);
		container.add(widget);
		fields.put(field, widget);
	}

	private void bindFieldToEvent(String fieldName, Widget widget) {
		final KeyUpHandler keyUpHandler = customKeyUpHandler.get(fieldName);
		final KeyDownHandler keyDownHandler = customKeyDownHandler.get(fieldName);
		final KeyPressHandler keyPressHandler = customKeyPressHandler.get(fieldName);
		if (keyUpHandler != null) {

			widget.addDomHandler(keyUpHandler, KeyUpEvent.getType());
		}
		if (keyDownHandler != null) {
			widget.addDomHandler(keyDownHandler, KeyDownEvent.getType());
		}
		if (keyPressHandler != null) {
			widget.addDomHandler(keyPressHandler, KeyPressEvent.getType());
		}
	}

	/**
	 * Set custom size and label defined in {@link plumb.shared.display.DisplayProperty}
	 *
	 * @param field
	 * @param composite
	 */
	private void setCustomProperties(DisplayField field, Composite composite) {
		if (field.size > 0) {
			composite.setWidth(String.valueOf(field.size) + "px");
		}
		if (!"".equals(field.label)) {
			((FormField) composite).setLabel(field.label);
		}
	}

	@Override
	public Widget asWidget() {
		return container.asWidget();
	}

	public B flush() {
		invalidFields.clear();
		for (Widget widget : fields.values()) {
			FormField formField = (FormField) widget;
			if (formField.hasError()) {
				invalidFields.add(formField.toString()); // FIXME add propertyName to FormField
			}
		}
		if (invalidFields.isEmpty()) {
			display = delegate.flush(display, fields);
		} else {
			GWT.log("invalid fields for display " + display.getClass().getName());
		}
		return display;
	}

	public boolean hasErrors() {
		return !invalidFields.isEmpty();
	}

	public interface ExceptionMessages extends Messages {
		/**
		 * @param fieldTyeName the field type name
		 * @return a message specifying that the field type is not yet supported
		 */
		@DefaultMessage("Type {0} is not supported yet !")
		String typeNotYetSupported(String fieldTyeName);
	}
}
