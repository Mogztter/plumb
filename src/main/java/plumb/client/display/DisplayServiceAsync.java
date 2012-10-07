package plumb.client.display;

import java.util.List;

import plumb.shared.display.DisplayBean;
import plumb.shared.display.DisplayField;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DisplayServiceAsync {

	void resolve(DisplayBean display,
			AsyncCallback<List<DisplayField>> callback);

	void log(String message, AsyncCallback<Void> callback);
	
}
