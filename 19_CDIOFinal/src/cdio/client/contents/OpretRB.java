package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.FieldVerifier;
import cdio.shared.RaavareBatchDTO;
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

public class OpretRB extends Composite {

	private VerticalPanel vPane;
	private FlexTable ft;
	private TextBox rbID, raavareID, maengde;
	private Button opret;
	private Label error;
	private boolean rbIDValid=false, raavareIDValid=false, maengdeValid=false;
	private int[] raavareListe,rbListe;

	public OpretRB() {
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	public void run(){
		ft = new FlexTable();
		ft.setStyleName("FlexTable-Content");
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Opret råvarebatch");

		ft.setText(1, 0, "Råvare Batch ID:");
		rbID = new TextBox();
		rbID.addKeyUpHandler(new BatchIdCheck()); 
		rbID.setStyleName("TextBox-Opret");
		ft.setWidget(1, 1, rbID);

		ft.setText(2, 0, "Råvare ID:");
		raavareID = new TextBox();
		raavareID.addKeyUpHandler(new IdCheck()); 
		raavareID.setStyleName("Textbox-Opret");
		ft.setWidget(2, 1, raavareID);

		ft.setText(3, 0, "Mængde(kg):");
		maengde = new TextBox();
		maengde.addKeyUpHandler(new MaengdeCheck()); 
		maengde.setStyleName("Textbox-Opret");
		ft.setWidget(3, 1, maengde);

		opret = new Button("Opret");
		opret.addClickHandler(new OpretClick());
		opret.setEnabled(false);
		ft.setWidget(10, 1, opret);

		vPane.add(ft);
		getRaavareListe();
	}

	private void getRaavareListe(){
		Controller.service.getRaavareList(Controller.token, new AsyncCallback<List<RaavareDTO>>(){

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
					ft.setText(2,2,caught.getMessage());
				}
			}

			@Override
			public void onSuccess(List<RaavareDTO> result) {
				ft.setText(2,2,"");
				int i = 0;
				for(RaavareDTO rv : result){
					if(rv.getRaavareId()>i){
						i=rv.getRaavareId();
					}
				}

				raavareListe = new int[i+1];
				for(RaavareDTO rv : result){
					raavareListe[rv.getRaavareId()]=1;
				}
				Controller.refreshToken();
				getRBListe();
			}
		});
	}

	private void getRBListe(){
		Controller.service.getRaavareBatchList(Controller.token, new AsyncCallback<List<RaavareBatchDTO>>(){

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
					ft.setText(2,2,caught.getMessage());
				}
			}

			@Override
			public void onSuccess(List<RaavareBatchDTO> result) {
				ft.setText(2,2,"");
				int i = 0;
				for(RaavareBatchDTO rv : result){
					if(rv.getRaavareId()>i){
						i=rv.getRbId();
					}
				}

				rbListe = new int[i+1];

				for(RaavareBatchDTO rv : result){
					rbListe[rv.getRbId()]=1;
				}
				Controller.refreshToken();
			}					
		});
	}

	private class OpretClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			opret.setText("loading");
			opret.setEnabled(false);
			RaavareBatchDTO raavareBatch = new RaavareBatchDTO(Integer.parseInt(rbID.getText()),Integer.parseInt(raavareID.getText()),Double.parseDouble(maengde.getText()));

			Controller.service.createRaavareBatch(Controller.token, raavareBatch, new AsyncCallback<Void>(){

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
						opret.setText("Opret");
						opret.setEnabled(true);
					} else {
						opret.setText("Opret");
						opret.setEnabled(true);
						error.setText(caught.getMessage());
						error.setStyleName("TextBox-ErrorMessage");	
					}
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Råvare Batch " + rbID.getText() + " blev oprettet!");
					vPane.clear();
					Controller.refreshToken();
					run();
				}
			} );
		}
	}

	private class BatchIdCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			final TextBox id = (TextBox) event.getSource();
			ft.setText(1, 2, "");
			if(!FieldVerifier.isValidRaavareBatchId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				ft.setText(1,2,"Ugyldigt. Vælg et andet.");	
				ft.getCellFormatter().setStyleName(1, 2, "Recept-Error");
				rbIDValid = false;
			} else{
				if(rbListe[Integer.parseInt(id.getText())]==1){
					ft.setText(1,2,"Optaget. Vælg et andet.");	
					ft.getCellFormatter().setStyleName(1, 2, "Recept-Error");
					id.setStyleName("TextBox-OpretError");
					rbIDValid = false;
				} else {
					id.setStyleName("TextBox-Opret");
					rbIDValid = true;
					ft.getCellFormatter().setStyleName(1,2,"Recept-Positiv");
					ft.setText(1,2,"Godkendt!");
				}
			}

			if(rbIDValid && raavareIDValid && maengdeValid)
				opret.setEnabled(true);
			else {
				opret.setEnabled(false);
			}
		}
	}

	private class IdCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			final TextBox id = (TextBox) event.getSource();
			ft.setText(2, 2, "");
			if(!FieldVerifier.isValidRaavareId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				ft.setText(2, 2, "Ugyldigt. Vælg et andet.");
				ft.getCellFormatter().setStyleName(2, 2, "Recept-Error");
				raavareIDValid = false;
			} else{
				if(raavareListe[Integer.parseInt(id.getText())]==1){
					id.setStyleName("TextBox-Opret");
					ft.getCellFormatter().setStyleName(2,2,"Recept-Positiv");
					ft.setText(2,2,"Godkendt!");
					raavareIDValid = true;
				} else {
					ft.setText(2, 2, "Ukendt. Vælg et andet.");
					ft.getCellFormatter().setStyleName(2, 2, "Recept-Error");
					id.setStyleName("TextBox-OpretError");
					raavareIDValid = false;
				}
			}

			if(rbIDValid && raavareIDValid && maengdeValid)
				opret.setEnabled(true);
			else {
				opret.setEnabled(false);
			}
		}
	}

	private class MaengdeCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox name = (TextBox) event.getSource();
			String inTXT = maengde.getText();
			if (!FieldVerifier.isValidMaengde(inTXT)){
				name.setStyleName("TextBox-OpretError");
				maengdeValid = false;
				double input;
				try{
					String ny=inTXT;
					for(int i=0; i<inTXT.length(); i++){
						if(inTXT.charAt(i)==','){
							ny = inTXT.replace(",", ".");
						}
					}
					input = Double.parseDouble(ny);
					if(input<0.0000){
						ft.setText(3, 2, "For lille. Vælg en anden.");
						ft.getCellFormatter().setStyleName(3, 2, "Recept-Error");
					} else if (input>99999999.0000){
						ft.setText(3, 2, "For stor. Vælg en anden.");
						ft.getCellFormatter().setStyleName(3, 2, "Recept-Error");
					} else {
						for(int i=0; i<ny.length(); i++){
							if(ny.charAt(i)=='.'){
								String[] sString = ny.split("\\.");
								if (sString[1].length()>4){
									ft.setText(3, 2, "For mange decimaler. Vælg en anden.");
									ft.getCellFormatter().setStyleName(3, 2, "Recept-Error");
								}
							}
						}
					}

				} catch (NumberFormatException e){
					ft.setText(3, 2, "Ugyldigt format. Vælg en anden.");
					ft.getCellFormatter().setStyleName(3, 2, "Recept-Error");					
				}
			}
			else{
				name.setStyleName("TextBox-Opret");
				ft.getCellFormatter().setStyleName(3, 2, "Recept-Positiv");
				ft.setText(3, 2, "Godkendt!");
				maengdeValid = true;
			}

			if(rbIDValid && raavareIDValid && maengdeValid)
				opret.setEnabled(true);
			else {
				opret.setEnabled(false);
			}
		}
	}
}