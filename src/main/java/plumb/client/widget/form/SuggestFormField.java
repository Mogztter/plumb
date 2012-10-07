/**
 * 
 */
package plumb.client.widget.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author bkhadige
 *
 */
public class SuggestFormField extends Composite implements HasText, HasKeyUpHandlers, HasValueChangeHandlers<String> {

	private static SuggestFormFieldUiBinder uiBinder = GWT
			.create(SuggestFormFieldUiBinder.class);

	interface SuggestFormFieldUiBinder extends UiBinder<Widget, SuggestFormField> {
	}

	public SuggestFormField() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public SuggestFormField(boolean mandatory, String labelText) {
		this.mandatory = mandatory;
		label = new Label(labelText);
		if (mandatory) {
			label.setText(label.getText() + " (*) ");
		}
		initWidget(uiBinder.createAndBindUi(this));
		field.addStyleName("suggestformField");
//		field.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//			@Override
//			public void onSelection(SelectionEvent<Suggestion> event) {
//				final String suggestion = event.getSelectedItem().getReplacementString();
//				if (suggestionsMap.containsKey(suggestion)) {
//					description.setValue(suggestionsMap.get(suggestion).getLabel());
//				} else {
//					Window.alert("invalid suggestion");
//				}
//			}
//		});
	}
	
	boolean mandatory = false;

	@UiField(provided=true)
	Label label = new Label();
	
	private final MultiWordSuggestOracle suggestions = new MultiWordSuggestOracle();

	// TODO
//	Map<String, SuggestDisplay> suggestionsMap = new HashMap<String, SuggestDisplay>();
	
	@UiField(provided = true)
	SuggestBox field = new SuggestBox(suggestions);
	
	@UiField
	TextBox description;
	
	@UiField
	HTML error;

	@UiHandler("field")
	void onKeyUp(KeyUpEvent e) {
		error.setHTML("");
		if (mandatory) {
			if (field.getText() == null	|| "".equals(field.getText())) {
				final SafeHtml errorMessage = new SafeHtmlBuilder().appendEscaped("Champ obligatoire").toSafeHtml();
				error.setHTML(errorMessage);
			}
		}
	}

	public void setText(String text) {
		if (text != null) {
			field.setText(text); 
		} else {
			throw new RuntimeException("null value is not supported");
		}
	}

	public String getText() {
		return field.getText();
	}

	public void setResult(String result) {
		this.description.setText(result);
	}

	public void setInvalidResult() {
		error.setText("recherche invalide");
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return field.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return field.addValueChangeHandler(handler);
	}

//	public void addSuggestion(SuggestDisplay d) {
//		suggestionsMap.put(d.getCode(), d);
//		suggestions.add(d.getCode());
//	}
//
//	public void clearSuggestions() {
//		suggestions.clear();
//		suggestionsMap.clear();
//	}
	
	public void showSuggestionList() {
		field.showSuggestionList();
	}

}
