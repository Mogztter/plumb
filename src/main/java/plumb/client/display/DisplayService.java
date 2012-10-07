package plumb.client.display;

import java.util.List;

import plumb.shared.display.DisplayBean;
import plumb.shared.display.DisplayField;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("display")
public interface DisplayService extends RemoteService {
	
	<D extends DisplayBean> List<DisplayField> resolve(D display);
	
	void log(String message);

}
