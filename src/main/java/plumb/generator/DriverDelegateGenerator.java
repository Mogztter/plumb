package plumb.generator;

import static org.reflections.ReflectionUtils.withModifier;
import static org.reflections.ReflectionUtils.withParametersCount;
import static org.reflections.ReflectionUtils.withPrefix;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Set;

import org.reflections.Reflections;

import plumb.shared.display.DisplayBean;

import com.google.common.base.Predicates;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class DriverDelegateGenerator extends Generator {

	// private static final String WAF_PACKAGE = "com.aprilwaf.wafservice.techadmin.shared";

	private static final String DISPLAY_BEAN_PACKAGE = "plumb.shared";

	GeneratorContext context = null;
	
	String scanPackage = "plumb.shared";

	/**
	 * 
	 */
	public String bindDisplayMethod() {
		Reflections reflections = new Reflections(
				DISPLAY_BEAN_PACKAGE);
		Set<Class<? extends DisplayBean>> subTypes = reflections
				.getSubTypesOf(DisplayBean.class);
		String s = "private void bindDisplay(DisplayBean display, String name, Object value) { ";
		for (Class<? extends DisplayBean> bean : subTypes) {
			@SuppressWarnings("unchecked")
			Set<Method> setters = Reflections.getAllMethods(bean, Predicates
					.and(withModifier(Modifier.PUBLIC), withPrefix("set"),
							withParametersCount(1)));
			s = bindDisplayClass(bean, s, setters);
			s = s.concat("}"); // IF 1
		}
		
		PropertyOracle propertyOracle = context.getPropertyOracle();

		// TODO : add property
		// System.out.println("scan package : " + System.getProperty("plumb.scan.package"));
		try {
			Reflections reflections2 = new Reflections(scanPackage);
			subTypes = reflections2
					.getSubTypesOf(DisplayBean.class);
			for (Class<? extends DisplayBean> bean : subTypes) {
				@SuppressWarnings("unchecked")
				Set<Method> setters = Reflections.getAllMethods(bean, Predicates
						.and(withModifier(Modifier.PUBLIC), withPrefix("set"),
								withParametersCount(1)));
				s = bindDisplayClass(bean, s, setters);
				s = s.concat("}"); // IF 1
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		s = s.concat("}"); // IF 2
		System.out.println("code genere pour la plumb " + s);
		return s;
	}

	/**
	 * @param subTypes
	 * @param s
	 * @param setters
	 * @return
	 */
	public String bindDisplayClass(Class<? extends DisplayBean> subTypes,
			String s, Set<Method> setters) {
		final String simpleName = subTypes.getSimpleName();
		s = s.concat("if (display instanceof " + simpleName + ") { "); // IF 1
		for (Method method : setters) {
			s = s.concat("if ("
					+ "\""
					+ method.getName().toLowerCase().replaceFirst("set", "")
							.concat("\".equalsIgnoreCase(name)) {"));
			if (method.getParameterTypes()[0].equals(String.class)) {
				s = s.concat("((" + simpleName + ")" + "display)."
						+ method.getName() + "((String) value); ");
			} else {
				if (method.getParameterTypes()[0].equals(int.class)) {
					s = s.concat("((" + simpleName + ")" + "display)."
							+ method.getName()
							+ "(new Integer((String) value)); ");
				} else {
					if (method.getParameterTypes()[0].equals(Date.class)) {
						s = s.concat("((" + simpleName + ")" + "display)."
								+ method.getName()
								+ "((java.util.Date) value); ");
					}
				}
			}
			s = s.concat("} \n");
		}
		return s;
	}

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		
		this.context = context;
		
		// Build a new class, that implements a "paintScreen" method
		JClassType classType;

		try {
			classType = context.getTypeOracle().getType(typeName);

			PropertyOracle propertyOracle = context.getPropertyOracle();
			try {
				com.google.gwt.core.ext.SelectionProperty selectionProperty = propertyOracle.getSelectionProperty(logger,"plumb.scan.package");
				scanPackage = selectionProperty.getCurrentValue();
			} catch (BadPropertyValueException e) {
				logger.log(TreeLogger.WARN, "Unable to find value for '"
						+ "plumb.scan.package" + "'", e);
			}

			// Here you would retrieve the metadata based on typeName for this
			// Screen
			SourceWriter src = getSourceWriter(classType, context, logger);

			if (src != null) {
				
				
				
				src.println(bindDisplayMethod());
				src.indent();
				src.println();

				src.println("@Override public B flush(B display"
						+ ", Map<DisplayField, Widget> fields) {");

				src.println("Set<DisplayField> keyset = fields.keySet();");
				src.println("for (DisplayField df : keyset) {");
				src.println("final HasValue hasText = (HasValue) fields.get(df);");
				src.println("final Object value = hasText.getValue();");
				src.println("if (value != null && value != null) {");

				src.println("bindDisplay(display, df.name, value);");

				src.println("} else {");
				src.println("Window.alert(\"field widget missing for  + df.name)\"); ");
				src.println("}");
				src.println("}");
				src.println("return display;");
				src.println("}");
				src.commit(logger);
			}
			System.out.println("Generating for: " + typeName);
			return typeName + "Generated";

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SourceWriter getSourceWriter(JClassType classType,
			GeneratorContext context, TreeLogger logger) {
		String packageName = classType.getPackage().getName();

		// Get the display class

		String simpleName = classType.getSimpleSourceName() + "Generated";

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			
			String className = simpleName.concat("<B extends DisplayBean>");

			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
					packageName, className);

			composer.setSuperclass(classType.getName() + "<B>");
			// composer.addImplementedInterface(classType.getName() + "<B>");

			// Need to add whatever imports your generated class needs.
			composer.addImport("java.util.Map");
			composer.addImport("java.util.Set");

			composer.addImport("plumb.shared.display.DisplayBean");
			composer.addImport("plumb.shared.display.DisplayField");

			Reflections reflections = new Reflections(
					DISPLAY_BEAN_PACKAGE);
			Set<Class<? extends DisplayBean>> subTypes = reflections
					.getSubTypesOf(DisplayBean.class);

			for (Class<? extends DisplayBean> class1 : subTypes) {
				composer.addImport(class1.getCanonicalName());
			}
			
			// TODO
			Reflections reflections2 = new Reflections(scanPackage);
			subTypes = reflections2
					.getSubTypesOf(DisplayBean.class);
			for (Class<? extends DisplayBean> bean : subTypes) {
				composer.addImport(bean.getCanonicalName());
			}


			composer.addImport("com.google.gwt.user.client.Window");
			composer.addImport("com.google.gwt.user.client.ui.HasValue");
			composer.addImport("com.google.gwt.user.client.ui.Widget");

			
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}
}
