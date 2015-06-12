package cdio.client.contents;

import cdio.client.Controller;
import cdio.shared.FieldVerifier;
import cdio.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpretOpr extends Composite {
	
	private VerticalPanel vPane;
	private Label error;
	private FlexTable ft;
	private TextBox id, navn, ini, cpr, pass;
	private CheckBox admin, farm, vaerk, opr;
	private Button ok;
	private boolean idValid, navnValid, passValid, cprValid, iniValid, roleValid;
	
	public OpretOpr(){
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}
	
	private void run(){
		// Reset til 'blank' position
		idValid = true; // Benyttes ikke
		navnValid = false;
		passValid = false;
		cprValid = false;
		iniValid = false;
		vPane.clear();
		
		// Byg siden
		error = new Label("");
		ft = new FlexTable();
		ft.setStyleName("FlexTable-Content");
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");	
		ft.setText(0, 0, "Opret Operatør");
		
//		ft.setText(1, 0, "Bruger ID:");
//		id = new TextBox();
//		id.addKeyUpHandler(new IdCheck());
//		id.setStyleName("TextBox-Opret");
//		ft.setWidget(1, 1, id);
		
		ft.setText(2, 0, "Navn:");
		navn = new TextBox();
		navn.addKeyUpHandler(new NameCheck());
		navn.setStyleName("TextBox-Opret");
		ft.setWidget(2, 1, navn);
		
		ft.setText(3, 0, "Initialer:");
		ini = new TextBox();
		ini.addKeyUpHandler(new IniCheck());
		ini.setStyleName("TextBox-Opret");
		ft.setWidget(3, 1, ini);
		
		ft.setText(4, 0, "Cpr nr:");
		cpr = new TextBox();
		cpr.addKeyUpHandler(new CprCheck());
		cpr.setStyleName("TextBox-Opret");
		ft.setWidget(4, 1, cpr);
		
		ft.setText(5, 0, "Password:");
		pass = new TextBox();
		pass.addKeyUpHandler(new PassCheck());
		pass.setStyleName("TextBox-Opret");
		ft.setWidget(5, 1, pass);
		
		ft.setText(6, 0, "Admin:");
		admin = new CheckBox();
		admin.addClickHandler(new RolleCheck());
		ft.setWidget(6, 1, admin);
		
		ft.setText(7, 0, "Farmaceut:");
		farm = new CheckBox();
		farm.addClickHandler(new RolleCheck());
		ft.setWidget(7, 1, farm);
		
		ft.setText(8, 0, "Værkfører:");
		vaerk = new CheckBox();
		vaerk.addClickHandler(new RolleCheck());
		ft.setWidget(8, 1, vaerk);
		
		ft.setText(9, 0, "Operatør:");
		opr = new CheckBox();
		opr.addClickHandler(new RolleCheck());
		ft.setWidget(9, 1, opr);
		
		ok = new Button("Opret");
		ok.addClickHandler(new OpretClick());
		ok.setEnabled(false);
		ft.setWidget(10, 1, ok);
		
		vPane.add(ft);
		vPane.add(error);
	}
	
	private class OpretClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			ok.setEnabled(false);
			UserDTO user = new UserDTO("0",
					navn.getText(),
					ini.getText(),
					cpr.getText(),
					pass.getText(),
					admin.getValue(),
					farm.getValue(),
					vaerk.getValue(),
					opr.getValue());
			
			Controller.service.createUser(Controller.token, user, new AsyncCallback<Void>(){

				@Override
				public void onFailure(Throwable caught) {
					ok.setEnabled(true);
					error.setText(caught.getMessage());
					error.setStyleName("TextBox-ErrorMessage");
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Bruger " + navn.getText() + " blev oprettet.");
					run();
					Controller.refreshToken();
				}

			});
		}
	}
	
	private class PassCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox password = (TextBox) event.getSource();
			if (!FieldVerifier.isValidPassword(password.getText())) {
				password.setStyleName("TextBox-OpretError");
				passValid = false;
			}
			else {
				password.setStyleName("TextBox-Opret");
				passValid = true;
			}

			if (passValid && navnValid && cprValid && iniValid && idValid  && roleValid)
				ok.setEnabled(true);
			else
				ok.setEnabled(false);
		}
	}
	
	private class NameCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox name = (TextBox) event.getSource();
			if (!FieldVerifier.isValidName(name.getText())) {
				name.setStyleName("TextBox-OpretError");
				navnValid = false;
			}
			else {
				name.setStyleName("TextBox-Opret");
				navnValid = true;
			}

			if (passValid && navnValid && cprValid && iniValid && idValid  && roleValid)
				ok.setEnabled(true);
			else
				ok.setEnabled(false);
		}
	}
	
//	private class IdCheck implements KeyUpHandler{
//
//		@Override
//		public void onKeyUp(KeyUpEvent event) {
//			TextBox id = (TextBox) event.getSource();
//			if (!FieldVerifier.isValidUserId(id.getText())) {
//				id.setStyleName("TextBox-OpretError");
//				idValid = false;
//			}
//			else {
//				id.setStyleName("TextBox-Opret");
//				idValid = true;
//			}
//
//			if (passValid && navnValid && cprValid && iniValid && idValid  && roleValid)
//				ok.setEnabled(true);
//			else
//				ok.setEnabled(false);
//		}
//	}
	
	private class IniCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox ini = (TextBox) event.getSource();
			if (!FieldVerifier.isValidInitial(ini.getText())) {
				ini.setStyleName("TextBox-OpretError");
				iniValid = false;
			}
			else {
				ini.setStyleName("TextBox-Opret");
				iniValid = true;
			}

			if (passValid && navnValid && cprValid && iniValid && idValid  && roleValid)
				ok.setEnabled(true);
			else
				ok.setEnabled(false);
		}
	}
	
	private class CprCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox cpr = (TextBox) event.getSource();
			if (!FieldVerifier.isValidCpr(cpr.getText())) {
				cpr.setStyleName("TextBox-OpretError");
				cprValid = false;
			}
			else {
				cpr.setStyleName("TextBox-Opret");
				cprValid = true;
			}

			if (passValid && navnValid && cprValid && iniValid && idValid  && roleValid)
				ok.setEnabled(true);
			else
				ok.setEnabled(false);
		}
	}
	
	private class RolleCheck implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			if (admin.getValue() || farm.getValue() || vaerk.getValue() || opr.getValue()) {
				roleValid = true;
			}
			else {
				roleValid = false;
			}

			if (passValid && navnValid && cprValid && iniValid && idValid && roleValid)
				ok.setEnabled(true);
			else
				ok.setEnabled(false);
		}
	}

}
