package cdio.client;

import cdio.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import cdio.shared.FieldVerifier;

public class Login extends Composite {

	private TextBox userName;
	private PasswordTextBox password;
	private Label errorMsg;
	private VerticalPanel vPane;
	private Button send;
	private boolean passValid, idValid;
	private ServiceAsync service;

	public Login(final ServiceAsync service){
		this.service = service;
		vPane = new VerticalPanel();
		vPane.setStyleName("Content-Login");
		initWidget(vPane);
		run();
	}
		
	public void run(){
		vPane.clear();
		// Opretter de nødvendige elementer
		Label userTxt = new Label("Bruger ID:");
		Label passTxt = new Label("Adgangskode:");
		userName = new TextBox();
		password = new PasswordTextBox();
		send = new Button("Login");
		send.setEnabled(false);
		Label header = new Label("Distribueret Afvejningssystem");
		errorMsg = new Label("");
		Label footer = new Label("Copyright © Gruppe 19");
		
		// Sæt curser i username feltet
		userName.setFocus(true);
		userName.selectAll();
		
		// Formatering/Design via CSS
		userTxt.setStyleName("TextLabel-Login");
		passTxt.setStyleName("TextLabel-Login");
		userName.setStyleName("TextBox");
		password.setStyleName("TextBox");
		errorMsg.setStyleName("TextLabel-ErrorMessage");
		header.setStyleName("Header-Login");
		footer.setStyleName("Footer-Login");
		vPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		// Tilføj elementerne til FlexTable
		FlexTable ft = new FlexTable();
		ft.setStyleName("FlexTable-Login"); // Design i CSS
		ft.setWidget(1, 0, userTxt);
		ft.setWidget(2, 0, userName);
		ft.setWidget(3, 0, passTxt);
		ft.setWidget(4, 0, password);
			FlexTable ft1 = new FlexTable();
			ft1.setWidth("100%");
			ft1.setWidget(0, 0, errorMsg);
			ft1.setWidget(0, 1, send);
			ft1.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		ft.setWidget(5, 0, ft1);
		
		// Tilføj elementerne i korrekt rækkefølge til siden
		vPane.add(header);
		vPane.add(ft);
		vPane.add(footer);
		
		send.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				UserDTO user = new UserDTO(userName.getText(), password.getText());
				password.setText("");
				password.setFocus(true);
				send.setEnabled(false);
				send.setText("Loading");
				service.login(user, new AsyncCallback<String>(){

					@Override
					public void onFailure(Throwable caught) {
						errorMsg.setText(caught.getMessage()); // Fejlbesked
						send.setText("Send");
					}

					@Override
					public void onSuccess(final String token) {	
						vPane.clear();
						vPane.add(new Controller(token, service)); 
					}
				});
			}
		});
		
		// Tjek om user-id input er gyldigt
		userName.addKeyUpHandler(new KeyUpHandler(){
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (!FieldVerifier.isValidUserId(userName.getText())) {
					userName.setStyleName("TextBox-Error");
					idValid = false;
				}
				else {
					userName.setStyleName("TextBox");
					idValid = true;
				}

				if (passValid&&idValid)
					send.setEnabled(true);
				else
					send.setEnabled(false);
			}
		});
		
		// Tjek om password input er gyldigt
		password.addKeyUpHandler(new KeyUpHandler(){
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (!FieldVerifier.isValidPassword(password.getText())) {
					password.setStyleName("TextBox-Error");
					passValid = false;
				}
				else {
					password.setStyleName("TextBox");
					passValid = true;
				}

				if (passValid&&idValid)
					send.setEnabled(true);
				else
					send.setEnabled(false);
			}
		});
	}
}
