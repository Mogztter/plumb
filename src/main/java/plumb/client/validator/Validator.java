/**
 * 
 */
package plumb.client.validator;

import com.google.gwt.regexp.shared.RegExp;

/**
 * @author bkhadige
 * 
 */
public abstract class Validator<V> {

	abstract boolean validate(V value);

	public final static EmailValidator EMAIL_VALIDATOR = new EmailValidator();
	
	public final static NumberValidator NUMBER_VALIDATOR = new NumberValidator();

	/**
	 * @author bkhadige
	 * 
	 */
	public static class EmailValidator extends Validator<String> {
		//RegExp p = RegExp
			//	.compile("^[a-z A-Z \\d _ . -]*+@+[a-z A-Z]+\\.+[a-z A-Z]{2,4}$");

		@Override
		public boolean validate(String value) {
			/* if (value == null) {
				return false;
			}
			return p.test(value);*/ return true;
		}
	}

	/**
	 * @author bkhadige
	 *
	 */
	public static class NumberValidator extends Validator<String> {
		@Override
		public boolean validate(String value) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException nfe) {
				return false;
			}
			return true;
		}
	}

}
