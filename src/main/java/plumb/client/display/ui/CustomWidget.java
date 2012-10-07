/**
 * 
 */
package plumb.client.display.ui;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author bkhadige
 *
 */
public class CustomWidget {
	
	public String property;
	
	public Widget w;

	public CustomWidget(String property, Widget w) {
		super();
		this.property = property;
		this.w = w;
	}

}
