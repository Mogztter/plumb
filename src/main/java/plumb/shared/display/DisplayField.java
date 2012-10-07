/**
 * 
 */
package plumb.shared.display;

import java.io.Serializable;

import plumb.shared.validation.ValidationType;

/**
 * @author bkhadige
 *
 */
public class DisplayField implements Serializable, Comparable<DisplayField> {

	private static final long serialVersionUID = 1L;
	
	public String name;
	
	public String typeName;
	
	public String key;
	
	public String label;
	
	public int size;
	
	public int order;
	
	public boolean mandatory;
	
	public ValidationType[] validation;
	
	public DisplayField() { }
	
	public DisplayField(String name, String typeName, boolean mandatory) {
		this.name = name;
		this.typeName = typeName;
		this.mandatory = mandatory;
	}

	@Override
	public int compareTo(DisplayField other) {
		return new Integer(order).compareTo(new Integer(other.order));
	}

}
