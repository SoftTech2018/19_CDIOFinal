package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.shared.FieldVerifier;
import cdio.shared.TokenException;
import cdio.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SletOpr extends Composite {

	private VerticalPanel vPane;
	private Label error, sletBrugere;
	private FlexTable ft;

	public SletOpr() {
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	private void run(){
		vPane.clear();
		sletBrugere = new Label("Slet brugere");
		sletBrugere.setStyleName("FlexTable-Header");
		vPane.add(sletBrugere);
		error = new Label("Loading...");
		vPane.add(error);

		Controller.service.getOprList(Controller.token, new AsyncCallback<List<UserDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof TokenException){
					final PopupLogin pop = new PopupLogin();
					pop.center();
					Timer t = new Timer(){
						@Override
						public void run() {
							if (!pop.isShowing()){
								this.cancel();
								SletOpr.this.run();
							}
						}
					};
					t.scheduleRepeating(100);
					
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
					// Tilføj kun de brugere der ikke er deaktiverede
					if (FieldVerifier.isValidRoles(result.get(i))){	
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

						Button slet = new Button("Slet");
						slet.setStyleName("Button-Ret");
						slet.addClickHandler(new SletClick());
						ft.setWidget(i+1, 9, slet);
					}
				}
				vPane.add(ft);
				Controller.refreshToken();
			}

		});
	}

	private class SletClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			int eventRow = ft.getCellForEvent(event).getRowIndex();
			final int userId = Integer.parseInt(ft.getText(eventRow, 0));
			((Button) ft.getWidget(eventRow, 9)).setEnabled(false);
			((Button) ft.getWidget(eventRow, 9)).setText("Loading");
			Controller.service.deleteUser(Controller.token, userId, new AsyncCallback<Void>(){

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
						vPane.add(error);
						error.setText(caught.getMessage());
						error.setStyleName("TextLabel-ErrorMessage");						
					}
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Bruger " + userId + " er blevet slettet.");
					run();
				}
			});	
		}
	}
}