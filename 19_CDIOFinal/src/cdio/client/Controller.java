package cdio.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Controller extends Composite {

	private String token;
	private ServiceAsync service;
	private Label errorMsg;
	private VerticalPanel vPane;

	public Controller(String token, final ServiceAsync service) {
		this.token = token;
		this.service = service;
		vPane = new VerticalPanel();
		initWidget(vPane);
		
		errorMsg = new Label();
		errorMsg.setStyleName("ErrorMsg");

		service.getRole(token, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				errorMsg.setText(caught.getMessage());
				vPane.add(errorMsg);
			}

			@Override
			public void onSuccess(String result) {
				errorMsg.setText(result);
				vPane.add(errorMsg);
				switch (result.toUpperCase()){
				case "ADMIN":
					// TO DO
					break;
				case "FARMACEUT":
					// TO DO
					break;
				case "VAERKFOERER":
					// TO DO
					break;
				default:
					errorMsg.setText("Der er sket en fejl. Din rolle kunne ikke genkendes");
					vPane.add(errorMsg);
					Button ok = new Button("Ok");
					ok.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event) {
							new Login(service);	
						}
					});
				}
			}

		});
	}

}
