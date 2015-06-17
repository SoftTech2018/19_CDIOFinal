package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.FieldVerifier;
import cdio.shared.RaavareDTO;
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

public class RetRaavare  extends Composite {

	private Label error, retRaavarer, id;
	private VerticalPanel vPane;
	private String uID, uNavn, uLeverandoer;
	private FlexTable ft;
	private int eventRow, openEventRow;
	private boolean nameValid, leverandoerValid;
	private TextBox oNavn, oLeverandoer;

	public RetRaavare() {
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	public void run(){
		vPane.clear();
		error = new Label("Loading...");
		vPane.add(error);
		Controller.service.getRaavareList(Controller.token, new AsyncCallback<List<RaavareDTO>>(){

			public void onFailure(Throwable caught) {
				error.setText(caught.getMessage());	
				error.setStyleName("TextLabel-ErrorMessage");
			}

			public void onSuccess(List<RaavareDTO> result) {
				retRaavarer = new Label("Ret råvarer");
				id = new Label("ID");
				retRaavarer.setStyleName("FlexTable-Header");
				vPane.add(retRaavarer);
				error.setText("");
				ft = new FlexTable();
				ft.setStyleName("FlexTable-Content");
				ft.setText(0, 0, "id");
				ft.setText(0, 1, "Navn");
				ft.setText(0, 2, "Leverandør");
				ft.setText(0, 3, "");
				ft.setText(0, 4, "");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "30px");
				ft.getFlexCellFormatter().setWidth(0, 1, "150px");
				ft.getFlexCellFormatter().setWidth(0, 2, "150px");
				ft.getFlexCellFormatter().setWidth(0, 3, "45px");
				ft.getFlexCellFormatter().setWidth(0, 4, "45px");

				for (int i=0; i<result.size(); i++){
					ft.setText(i+1, 0, Integer.toString(result.get(i).getRaavareId()));
					ft.setText(i+1, 1, result.get(i).getRaavareNavn());
					ft.setText(i+1, 2, result.get(i).getLeverandoer());

					Button ret = new Button("Ret");
					ret.setStyleName("Button-Ret");
					ret.addClickHandler(new RetClick());
					ft.setWidget(i+1, 3, ret);
				}
				vPane.add(ft);
			}
		});
	}

	private class RetClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			if (openEventRow != 0){
				((Button) ft.getWidget(openEventRow, 4)).click();
			}
			eventRow = ft.getCellForEvent(event).getRowIndex();
			openEventRow = eventRow;

//			uID = ft.getText(eventRow, 0);
			uNavn = ft.getText(eventRow, 1);
			uLeverandoer = ft.getText(eventRow, 2);

			nameValid = FieldVerifier.isValidName(uNavn);
			leverandoerValid = true; //unødvendigt

			//Her laves nye widgets der kan redigeres i og erstatter de oprindelige med disse
//			oID = new TextBox();
//			oID.setText(uID);
//			oID.addKeyUpHandler(new IDCheck());
//			oID.setStyleName("TextBox-Ret");
//			ft.setWidget(eventRow, 0, oID);

			oNavn = new TextBox();
			oNavn.setText(uNavn);
			oNavn.addKeyUpHandler(new NameCheck());
			oNavn.setStyleName("TextBox-Ret");
			ft.setWidget(eventRow, 1, oNavn);

			oLeverandoer = new TextBox();
			oLeverandoer.setText(uLeverandoer);
			oLeverandoer.setStyleName("TextBox-Ret");
			ft.setWidget(eventRow, 2, oLeverandoer);

			Button ok = new Button("Ok");
			ok.setStyleName("Button-Ret");
			ok.addClickHandler(new OkClick());
			ft.setWidget(eventRow, 3, ok);

			Button cancel = new Button("Cancel");
			cancel.setStyleName("Button-Ret");
			cancel.addClickHandler(new CancelClick());
			ft.setWidget(eventRow, 4, cancel);
		}
	}

	private class OkClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {

			for (int i = 1; i<ft.getRowCount(); i++){
				((Button) ft.getWidget(i, 3)).setEnabled(false);
			}
			((Button) ft.getWidget(eventRow, 4)).setEnabled(false);

			RaavareDTO raavare = new RaavareDTO(Integer.parseInt(ft.getText(eventRow, 0)), ((TextBox)ft.getWidget(eventRow, 1)).getText(), ((TextBox)ft.getWidget(eventRow, 2)).getText());
			Controller.service.updateRaavare(Controller.token, raavare, new AsyncCallback<Void>(){

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
					}
					error.setText(caught.getMessage());
					error.setStyleName("TextLabel-ErrorMessage");
					openEventRow = 0;
					run();
				}

				@Override
				public void onSuccess(Void result) {
					Controller.refreshToken();
//					uNavn = ((TextBox) ft.getWidget(eventRow, 1)).getText();
//					uLeverandoer = ((TextBox) ft.getWidget(eventRow, 2)).getText();
					openEventRow = 0;
					Window.alert("Raavare ID " + ft.getText(eventRow, 0) + " blev opdateret");
					run(); //Reload siden
				}
			});
		}
	}

	private class CancelClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			int eventRow = ft.getCellForEvent(event).getRowIndex();

			ft.setText(eventRow, 0, uID);
			ft.setText(eventRow, 1, uNavn);
			ft.setText(eventRow, 2, uLeverandoer);

			Button ret = new Button("Ret");
			ret.setStyleName("Button-Ret");
			ret.addClickHandler(new RetClick());
			ft.setWidget(eventRow, 3, ret);

			ft.setText(eventRow, 4, "");
			openEventRow =0;
		}
	}

//	private class IDCheck implements KeyUpHandler{
//
//		@Override
//		public void onKeyUp(KeyUpEvent event) {
//			TextBox id = (TextBox) event.getSource();
//			if(!FieldVerifier.isValidUserId(id.getText())){
//				id.setStyleName("TextBox-RetError");
//				idValid = false;
//			} else {
//				id.setStyleName("TextBox-Ret");
//				idValid = true;
//			}
//
//			if (idValid && nameValid && leverandoerValid)
//				((Button) ft.getWidget(eventRow, 3)).setEnabled(true);
//			else
//				((Button) ft.getWidget(eventRow, 3)).setEnabled(false);
//		}
//	}

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

			if (nameValid && leverandoerValid)
				((Button) ft.getWidget(eventRow, 3)).setEnabled(true);
			else
				((Button) ft.getWidget(eventRow, 3)).setEnabled(false);
		}
	}
}