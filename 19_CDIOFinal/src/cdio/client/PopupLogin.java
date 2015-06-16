package cdio.client;

import cdio.shared.FieldVerifier;
import cdio.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PopupLogin extends PopupPanel {

	private Label userName;
	private PasswordTextBox password;
	private Label errorMsg, load;
	private VerticalPanel vPane;
	private Button send;
	private boolean passValid;
	private String user;

	public PopupLogin(){
		setAutoHideEnabled(false);; // Sikrer man ikke kan fjerne popop-vinduet ved at klikke uden for det
		setGlassEnabled(true); // Grå baggrund
		vPane = new VerticalPanel();
		vPane.setStyleName("Popup-Login");
		setWidget(vPane);
		errorMsg = new Label("Din session er udløbet");
		errorMsg.setStyleName("TextLabel-ErrorMessage");
		load = new Label("Loading...");
		vPane.add(load);

		Controller.service.getUserId(Controller.token, new AsyncCallback<Integer>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
				Controller.logud();
			}

			@Override
			public void onSuccess(Integer result) {
				load.setText("");
				user = Integer.toString(result);

				// Opretter de nødvendige elementer
				Label userTxt = new Label("Bruger ID:");
				Label passTxt = new Label("Adgangskode:");
				userName = new Label(user);
				password = new PasswordTextBox();
				send = new Button("Login");
				send.setEnabled(false);

				// Sæt curser i username feltet
				password.setFocus(true);
				password.selectAll();

				// Formatering/Design via CSS
				userTxt.setStyleName("TextLabel-Login");
				passTxt.setStyleName("TextLabel-Login");
				userName.setStyleName("TextBox");
				password.setStyleName("TextBox");
				errorMsg.setStyleName("TextLabel-ErrorMessage");
				vPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

				// Tilføj elementerne til FlexTable
				FlexTable ft = new FlexTable();
				ft.setStyleName("FlexTable-Login"); // Design i CSS
				ft.setWidget(1, 0, userTxt);
				ft.setWidget(2, 0, userName);
				ft.setWidget(3, 0, passTxt);
				ft.setWidget(4, 0, password);
				FlexTable ft2 = new FlexTable();
				ft2.setWidth("100%");
				ft2.setWidget(0, 0, errorMsg);
				ft2.setWidget(0, 1, send);
				ft2.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
				ft.setWidget(5, 0, ft2);

				Button cancel = new Button("Annuller");
				cancel.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						hide();
						Controller.logud();
					}
				});
				
				// Tilføj elementerne i korrekt rækkefølge til siden
				vPane.add(ft);
				vPane.add(cancel);

				send.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						UserDTO user = new UserDTO(userName.getText(), password.getText());
						send.setEnabled(false);
						send.setText("Loading");
						Controller.service.login(user, new AsyncCallback<String>(){

							@Override
							public void onFailure(Throwable caught) {
								errorMsg.setText(caught.getMessage()); // Fejlbesked
								send.setText("Login");
								password.setText("");
								password.setFocus(true);
								password.setStyleName("TextBox-Error");
								passValid = false;
							}

							@Override
							public void onSuccess(final String token) {	
								Controller.token = token;
								Controller.refreshToken(); // nulstiller timeren til 30 min
								Timer t = new Timer(){
									@Override
									public void run() {
										if (!token.equals(Controller.token)){
											hide();
										}
									}
								};
								t.scheduleRepeating(100);
							}
						});
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

						if (passValid)
							send.setEnabled(true);
						else
							send.setEnabled(false);
					}
				});
			}
		});
	}
}
