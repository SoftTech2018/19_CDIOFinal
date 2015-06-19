package cdio.client.contents;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.FieldVerifier;
import cdio.shared.TokenException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SletRaavare extends Composite{
	
	private VerticalPanel vPane;
	private Label sletRaavarer;
	private FlexTable ft;
	private TextBox id;
	private Label desc;
	private Button btn;
	
	public SletRaavare(){
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}
	
	private void run(){
		
		vPane.clear();
		sletRaavarer = new Label("Slet råvare");
		sletRaavarer.setStyleName("FlexTable-Header");
		vPane.add(sletRaavarer);
		
		desc = new Label("Indtast ID for Råvare der ønskes slettet");
		id = new TextBox();
		id.setStyleName("TextBox-Opret");
		id.addKeyUpHandler(new idCheck());

		btn = new Button("Slet Råvare");
		btn.setEnabled(false);

		ft = new FlexTable();

		ft.setWidget(0, 0, desc);
		ft.setWidget(1, 0, id);
		ft.setWidget(2, 0, btn);

		vPane.add(ft);
		
		btn.addClickHandler(new ClickHandler(){
			
			
			
			@Override
			public void onClick(ClickEvent event) {
				id.setEnabled(false);
				btn.setText("Loading");
				btn.setEnabled(false);
				Controller.service.deleteRaavare(Controller.token, Integer.parseInt(id.getText()), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						if (caught instanceof TokenException){
							final PopupLogin pop = new PopupLogin();
							pop.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
								public void setPosition(int offsetWidth, int offsetHeight) {
									int left = (Window.getClientWidth() - offsetWidth) / 3;
									int top = (Window.getClientHeight() - offsetHeight) / 3;
									pop.setPopupPosition(left, top);
								}
							});
							id.setText("");
							btn.setText("Slet Råvare");
						} else {
							Window.alert(caught.getMessage());
							id.setText("");
							id.setEnabled(true);
							btn.setText("Slet Råvare");
						}
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Råvare ID " + id.getText() + " blev slettet");
						id.setText("");
						id.setEnabled(true);
						btn.setText("Slet Råvare");
						Controller.refreshToken();
						btn.setEnabled(true);
					}
					
				});
				
				
			}
			
		});
		
	}
	
	public class idCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox id = (TextBox) event.getSource();
			if(!FieldVerifier.isValidUserId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				btn.setEnabled(false);
			} else {
				id.setStyleName("TextBox-Opret");
				btn.setEnabled(true);
			}
		}
	}
	
}
