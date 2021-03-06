package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.FieldVerifier;
import cdio.shared.TokenException;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RetOpr extends Composite {

	private Label error, retBrugere;
	private VerticalPanel vPane;
	private int eventRow, openEventRow;
	private FlexTable ft;
	private String uNavn, uIni, uCpr, uPass;
	private boolean uAdmin, uFarm, uVeark, uOpr, passValid, nameValid, iniValid, cprValid, roleValid;
	private TextBox oNavn, oIni, oCpr, oPass;
	private CheckBox oAdmin, oFarm, oVaerk, oOpr;

	public RetOpr(){
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	public void run(){	
		vPane.clear();
		retBrugere = new Label("Ret brugere");
		retBrugere.setStyleName("FlexTable-Header");
		vPane.add(retBrugere);
		error = new Label("Loading...");
		vPane.add(error);
		Controller.service.getOprList(Controller.token, new AsyncCallback<List<UserDTO>>(){

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
				} else {
					error.setText(caught.getMessage());	
					error.setStyleName("TextLabel-ErrorMessage");					
				}
			}

			@Override
			public void onSuccess(List<UserDTO> result) {
				error.setText("");
				ft = new FlexTable();
				ft.setStyleName("FlexTable-Content");
				ft.setText(0, 0, "ID");
				ft.setText(0, 1, "Navn");
				ft.setText(0, 2, "Initialer");
				ft.setText(0, 3, "Cpr nr");
				ft.setText(0, 4, "Password");
				ft.setText(0, 5, "Admin");
				ft.setText(0, 6, "Farmaceut");
				ft.setText(0, 7, "Værkfører");
				ft.setText(0, 8, "Operatør");
				ft.setText(0, 9, "");
				ft.setText(0, 10, "");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "30px");
				ft.getFlexCellFormatter().setWidth(0, 1, "150px");
				ft.getFlexCellFormatter().setWidth(0, 2, "55px");
				ft.getFlexCellFormatter().setWidth(0, 3, "85px");
				ft.getFlexCellFormatter().setWidth(0, 4, "85px");
				ft.getFlexCellFormatter().setWidth(0, 5, "55px");
				ft.getFlexCellFormatter().setWidth(0, 6, "75px");
				ft.getFlexCellFormatter().setWidth(0, 7, "75px");
				ft.getFlexCellFormatter().setWidth(0, 8, "75px");
				ft.getFlexCellFormatter().setWidth(0, 9, "45px");
				ft.getFlexCellFormatter().setWidth(0, 10, "45px");

				for (int i=0; i<result.size(); i++){
					ft.setText(i+1, 0, Integer.toString(result.get(i).getUserId()));
					ft.setText(i+1, 1, result.get(i).getName());
					ft.setText(i+1, 2, result.get(i).getIni());
					ft.setText(i+1, 3, result.get(i).getCpr());
					ft.setText(i+1, 4, result.get(i).getPassword());
					CheckBox adm = new CheckBox();
					adm.setValue(result.get(i).isAdmin());
					adm.setEnabled(false);
					ft.setWidget(i+1, 5, adm);
					CheckBox farm = new CheckBox();
					farm.setValue(result.get(i).isFarmaceut());
					farm.setEnabled(false);
					ft.setWidget(i+1, 6, farm);
					CheckBox vaerk = new CheckBox();
					vaerk.setValue(result.get(i).isVaerkfoerer());
					vaerk.setEnabled(false);
					ft.setWidget(i+1, 7, vaerk);
					CheckBox opr = new CheckBox();
					opr.setValue(result.get(i).isOperatoer());
					opr.setEnabled(false);
					ft.setWidget(i+1, 8, opr);

					Button ret = new Button("Ret");
					ret.setStyleName("Button-Ret");
					ret.addClickHandler(new RetClick());
					ft.setWidget(i+1, 9, ret);
				}
				vPane.add(ft);
				Controller.refreshToken();
			}	
		});
	}

	private class RetClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			if (openEventRow != 0){
				((Button) ft.getWidget(openEventRow, 10)).click();
			}
			eventRow = ft.getCellForEvent(event).getRowIndex();
			openEventRow = eventRow;

			//Gem de oprindelige værdier
			uNavn = ft.getText(eventRow, 1);
			uIni = ft.getText(eventRow, 2);
			uCpr = ft.getText(eventRow, 3);
			uPass = ft.getText(eventRow, 4);
			uAdmin = ((CheckBox) ft.getWidget(eventRow, 5)).getValue();
			uFarm = ((CheckBox) ft.getWidget(eventRow, 6)).getValue();
			uVeark = ((CheckBox) ft.getWidget(eventRow, 7)).getValue();
			uOpr = ((CheckBox) ft.getWidget(eventRow, 8)).getValue();

			passValid = FieldVerifier.isValidPassword(uPass);
			nameValid = FieldVerifier.isValidName(uNavn);
			iniValid = FieldVerifier.isValidInitial(uIni);
			cprValid = FieldVerifier.isValidCpr(uCpr);
			if (uAdmin || uFarm || uVeark || uOpr)
				roleValid = true;

			// Lav nye widgets der kan redigeres og erstat de oprindelige med disse
			oNavn = new TextBox();
			oNavn.setText(uNavn);
			oNavn.addKeyUpHandler(new NameCheck());
			oNavn.setStyleName("TextBox-Ret");
			ft.setWidget(eventRow, 1, oNavn);

			oIni = new TextBox();
			oIni.setText(uIni);
			oIni.addKeyUpHandler(new IniCheck());
			oIni.setStyleName("TextBox-Ret");
			ft.setWidget(eventRow, 2, oIni);

			oCpr = new TextBox();
			oCpr.setText(uCpr);
			oCpr.addKeyUpHandler(new CprCheck());
			oCpr.setStyleName("TextBox-Ret");
			ft.setWidget(eventRow, 3, oCpr);

			oPass = new TextBox();
			oPass.setText(uPass);
			oPass.addKeyUpHandler(new PassWordCheck());
			oPass.setStyleName("TextBox-Ret");
			ft.setWidget(eventRow, 4, oPass);

			oAdmin = new CheckBox();
			oAdmin.setEnabled(true);
			oAdmin.setValue(uAdmin);
			oAdmin.addClickHandler(new RolleCheck());
			ft.setWidget(eventRow, 5, oAdmin);

			oFarm = new CheckBox();
			oFarm.setEnabled(true);
			oFarm.setValue(uFarm);
			oFarm.addClickHandler(new RolleCheck());
			ft.setWidget(eventRow, 6, oFarm);

			oVaerk = new CheckBox();
			oVaerk.setEnabled(true);
			oVaerk.setValue(uVeark);
			oVaerk.addClickHandler(new RolleCheck());
			ft.setWidget(eventRow, 7, oVaerk);

			oOpr = new CheckBox();
			oOpr.setEnabled(true);
			oOpr.setValue(uOpr);
			oOpr.addClickHandler(new RolleCheck());
			ft.setWidget(eventRow, 8, oOpr);

			Button ok = new Button("Ok");
			ok.setStyleName("Button-Ret");
			ok.addClickHandler(new OkClick());
			ft.setWidget(eventRow, 9, ok);

			Button cancel = new Button("Cancel");
			cancel.setStyleName("Button-Ret");
			cancel.addClickHandler(new CancelClick());
			ft.setWidget(eventRow, 10, cancel);
		}
	}

	private class OkClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			((Button) ft.getWidget(eventRow, 9)).setText("Loading");
			((Button) ft.getWidget(eventRow, 9)).setEnabled(false);

			UserDTO user = new UserDTO(ft.getText(eventRow, 0), 
					((TextBox) ft.getWidget(eventRow, 1)).getText(), 
					((TextBox) ft.getWidget(eventRow, 2)).getText(), 
					((TextBox) ft.getWidget(eventRow, 3)).getText(), 
					((TextBox) ft.getWidget(eventRow, 4)).getText(),
					((CheckBox) ft.getWidget(eventRow, 5)).getValue(), 
					((CheckBox) ft.getWidget(eventRow, 6)).getValue(), 
					((CheckBox) ft.getWidget(eventRow, 7)).getValue(), 
					((CheckBox) ft.getWidget(eventRow, 8)).getValue());

			Controller.service.updateUser(Controller.token, user, new AsyncCallback<UserDTO>(){

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
					} else {
						((Button) ft.getWidget(eventRow, 9)).setText("Ok");
						((Button) ft.getWidget(eventRow, 9)).setEnabled(false);
						error.setText(caught.getMessage());
						error.setStyleName("TextLabel-ErrorMessage");						
					}
				}

				@Override
				public void onSuccess(UserDTO result) {		
					uNavn = ((TextBox) ft.getWidget(eventRow, 1)).getText();
					uIni = ((TextBox) ft.getWidget(eventRow, 2)).getText();
					uCpr = ((TextBox) ft.getWidget(eventRow, 3)).getText();
					uPass = ((TextBox) ft.getWidget(eventRow, 4)).getText();
					uAdmin = ((CheckBox) ft.getWidget(eventRow, 5)).getValue();
					uFarm = ((CheckBox) ft.getWidget(eventRow, 6)).getValue();
					uVeark = ((CheckBox) ft.getWidget(eventRow, 7)).getValue();
					uOpr = ((CheckBox) ft.getWidget(eventRow, 8)).getValue();

					openEventRow = 0;
					Window.alert("Bruger " + result.getName() + " blev opdateret.");
					run(); // Reload siden
					Controller.refreshToken();
				}
			});
		}
	}

	private class CancelClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			error.setText("");

			// Sætter text/widgets tilbage til oprindelige status
			int eventRow = ft.getCellForEvent(event).getRowIndex();

			ft.setText(eventRow, 1, uNavn);
			ft.setText(eventRow, 2, uIni);
			ft.setText(eventRow, 3, uCpr);
			ft.setText(eventRow, 4, uPass);
			CheckBox adm = new CheckBox();
			adm.setValue(uAdmin);
			adm.setEnabled(false);
			ft.setWidget(eventRow, 5, adm);
			CheckBox farm = new CheckBox();
			farm.setValue(uFarm);
			farm.setEnabled(false);
			ft.setWidget(eventRow, 6, farm);
			CheckBox vaerk = new CheckBox();
			vaerk.setValue(uVeark);
			vaerk.setEnabled(false);
			ft.setWidget(eventRow, 7, vaerk);
			CheckBox opr = new CheckBox();
			opr.setValue(uOpr);
			opr.setEnabled(false);
			ft.setWidget(eventRow, 8, opr);

			Button ret = new Button("Ret");
			ret.setStyleName("Button-Ret");
			ret.addClickHandler(new RetClick());
			ft.setWidget(eventRow, 9, ret);

			ft.setText(eventRow, 10, "");
			openEventRow =0;
		}
	}

	private class PassWordCheck implements KeyUpHandler{
		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox password = (TextBox) event.getSource();
			if (!FieldVerifier.isValidPassword(password.getText())) {
				password.setStyleName("TextBox-RetError");
				passValid = false;
			}
			else {
				password.setStyleName("TextBox-Ret");
				passValid = true;
			}

			if (passValid && nameValid && cprValid && iniValid && roleValid)
				((Button) ft.getWidget(eventRow, 9)).setEnabled(true);
			else
				((Button) ft.getWidget(eventRow, 9)).setEnabled(false);
		}
	}

	private class NameCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox name = (TextBox) event.getSource();
			if (!FieldVerifier.isValidName(name.getText())) {
				name.setStyleName("TextBox-RetError");
				nameValid = false;
			}
			else {
				name.setStyleName("TextBox-Ret");
				nameValid = true;
			}

			if (passValid && nameValid && cprValid && iniValid && roleValid)
				((Button) ft.getWidget(eventRow, 9)).setEnabled(true);
			else
				((Button) ft.getWidget(eventRow, 9)).setEnabled(false);
		}
	}

	private class CprCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox cpr = (TextBox) event.getSource();
			if (!FieldVerifier.isValidCpr(cpr.getText())) {
				cpr.setStyleName("TextBox-RetError");
				cprValid = false;
			}
			else {
				cpr.setStyleName("TextBox-Ret");
				cprValid = true;
			}

			if (passValid && nameValid && cprValid && iniValid && roleValid)
				((Button) ft.getWidget(eventRow, 9)).setEnabled(true);
			else
				((Button) ft.getWidget(eventRow, 9)).setEnabled(false);
		}
	}

	private class IniCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox ini = (TextBox) event.getSource();
			if (!FieldVerifier.isValidInitial(ini.getText())) {
				ini.setStyleName("TextBox-RetError");
				iniValid = false;
			}
			else {
				ini.setStyleName("TextBox-Ret");
				iniValid = true;
			}

			if (passValid && nameValid && cprValid && iniValid && roleValid)
				((Button) ft.getWidget(eventRow, 9)).setEnabled(true);
			else
				((Button) ft.getWidget(eventRow, 9)).setEnabled(false);
		}
	}

	private class RolleCheck implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			UserDTO user = new UserDTO();
			user.setAdmin(((CheckBox) ft.getWidget(eventRow, 5)).getValue());
			user.setFarmaceut(((CheckBox) ft.getWidget(eventRow, 6)).getValue());
			user.setVaerkfoerer(((CheckBox) ft.getWidget(eventRow, 7)).getValue());
			user.setOperatoer(((CheckBox) ft.getWidget(eventRow, 8)).getValue());
			if (FieldVerifier.isValidRoles(user)) {
				roleValid = true;
			} else {
				roleValid = false;
			}

			if (passValid && nameValid && cprValid && iniValid && roleValid)
				((Button) ft.getWidget(eventRow, 9)).setEnabled(true);
			else
				((Button) ft.getWidget(eventRow, 9)).setEnabled(false);
		}
	}
}
