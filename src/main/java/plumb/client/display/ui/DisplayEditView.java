/**
 * 
 */
package plumb.client.display.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import plumb.client.display.DisplayService;
import plumb.client.display.DisplayServiceAsync;
import plumb.client.widget.form.CustomFormField;
import plumb.client.widget.form.DateFormField;
import plumb.client.widget.form.FormField;
import plumb.client.widget.form.NumberFormField;
import plumb.client.widget.form.SimpleFormField;
import plumb.shared.display.DisplayBean;
import plumb.shared.display.DisplayField;
import plumb.shared.display.DisplayProperty;
import plumb.shared.validation.ValidationType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author bkhadige
 * 
 */
public class DisplayEditView<B extends DisplayBean> extends Composite {

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

	private void populate(B _display) {
		this.display = _display;
		service.resolve(_display,
				new AsyncCallback<List<DisplayField>>() {
					@Override
					public void onSuccess(List<DisplayField> properties) {
						for (Iterator<DisplayField> iterator = properties.iterator(); iterator.hasNext();) {
							DisplayField displayField = iterator.next();
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
		if (! customFields.containsKey(field.name)) {
			Composite w = null;
			if (field.typeName.equals(DisplayEditView.STRING_TYPE)) {
				w = new SimpleFormField(field.mandatory, field.name);
			} else {
				if (field.typeName.equals(DisplayEditView.INT_TYPE)) {
					w = new NumberFormField(field.mandatory, field.name);
				} else if (field.typeName.equals(DisplayEditView.DATE_TYPE)) {
					w = new DateFormField(field.mandatory, field.name);
				} 
			}
			
			ValidationType[] validation = field.validation;
			for (int i = 0; i < validation.length; i++) {
				((FormField) w).addValidationType(validation[i]); // FIXME : refactor
			}
			setCustomProperties(field, w);
			
			// TODO : add other types
			fields.put(field, w);
			container.add(w.asWidget());
		} else {
			CustomFormField w2 = new CustomFormField(field.mandatory, field.name,customFields.get(field.name).w);
			container.add(w2);
			setCustomProperties(field, w2);
            fields.put(field, w2);
		}
	}

	/**
	 * set custom size and label defined in {@link DisplayProperty}
	 * @param field
	 * @param w
	 */
	private void setCustomProperties(DisplayField field, Composite w) {
		if (field.size > 0) ((FormField) w).setWidth(String.valueOf(field.size) + "px");
		if (!"".equals(field.label)) ((FormField) w).setLabel(field.label);
	}

	@Override
	public Widget asWidget() {
		return container.asWidget();
	}

	public B flush() {
        invalidFields.clear();
        final Iterator<Widget> iterator = fields.values().iterator();
        while (iterator.hasNext()) {
            final Widget f = iterator.next();
            FormField formField = (FormField) f;
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
        return ! invalidFields.isEmpty();
    }

}
