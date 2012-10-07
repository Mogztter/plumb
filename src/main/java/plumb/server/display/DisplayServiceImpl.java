package plumb.server.display;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import plumb.client.display.DisplayService;
import plumb.shared.display.DisplayBean;
import plumb.shared.display.DisplayField;
import plumb.shared.display.DisplayProperty;
import plumb.shared.validation.ValidationType;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DisplayServiceImpl extends RemoteServiceServlet implements
		DisplayService {

	public static void main(String[] args) {
		Pattern p = Pattern.compile("^[a-z A-Z \\d _ . -]*+@+[a-z A-Z]+\\.+[a-z A-Z]{2,4}$");
		Matcher m = p.matcher("2aaB_a@xx.fr");
		boolean b = m.matches();
		System.out.println(b);
	}

	@Override
	public void log(String message) {
		System.out.println(message);
	}

	@Override
	public <D extends DisplayBean> List<DisplayField> resolve(D display) {
		final List<DisplayField> fields = new ArrayList<DisplayField>();
		final Field[] beanFields = getDisplayFields(display);
		for (int i = 0; i < beanFields.length; i++) {
			Field field = beanFields[i];
			DisplayField displayField = defineDisplayField(field);
			fields.add(displayField);
		}
		return fields;
	}

	/**
	 * @param field
	 * @return
	 */
	private DisplayField defineDisplayField(Field field) {
		boolean mandatory = false;
		int size = 0;
		String label = "", key = "";
		ValidationType[] validation = null;
		for (int j = 0; j < field.getAnnotations().length; j++) {
			Annotation annotation = field.getAnnotations()[j];
			if (annotation.annotationType().equals(NotNull.class)) {
				mandatory = true; // check mandatory field
			}
			if (annotation.annotationType().equals(DisplayProperty.class)) {
				DisplayProperty displayProperty = (DisplayProperty) annotation;
				label = displayProperty.label();
				key = displayProperty.key();
				size = displayProperty.size();
				validation = displayProperty.validation();
			}
		}
		DisplayField displayField = new DisplayField(field.getName(), field
				.getType().getCanonicalName(), mandatory);
		displayField.key = key;
		displayField.label = label;
		displayField.size = size;
		displayField.validation = validation;
		return displayField;
	}

	private List<String> technicalFields = Arrays.asList("serialVersionUID");

	/**
	 * @param display
	 * @return
	 */
	private <D> Field[] getDisplayFields(D display) {
		final Field[] fields = display.getClass().getDeclaredFields();
		List<Field> displayFields = new ArrayList<Field>();
		for (Field field : fields) {
			if (displayField(field)) {
				displayFields.add(field);
			}
		}
		return displayFields.toArray(new Field[displayFields.size()]);
	}

	/**
	 * @param field
	 * @return
	 */
	private boolean displayField(Field field) {
		return !technicalFields.contains(field.getName());
	}

}
