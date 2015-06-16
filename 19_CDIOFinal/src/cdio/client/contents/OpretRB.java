package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.DALException;
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
	private TextBox rbID, raavareID, mængde;
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

		ft.setText(3, 0, "Mængde:");
		mængde = new TextBox();
		mængde.addKeyUpHandler(new MaengdeCheck()); 
		mængde.setStyleName("Textbox-Opret");
		ft.setWidget(3, 1, mængde);

		opret = new Button("Opret");
		opret.addClickHandler(new OpretClick());
		opret.setEnabled(false);
		ft.setWidget(10, 1, opret);

		vPane.add(ft);
		getRaavareListe();
		getRBListe();
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
					ft.setText(2, 2, caught.getMessage());
				}
			}

			@Override
			public void onSuccess(List<RaavareDTO> result) {
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
					ft.setText(1, 2, "Fejl i råvarebatch listekald");					
				}
			}

			@Override
			public void onSuccess(List<RaavareBatchDTO> result) {
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
			}					
		});
	}

	private class OpretClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			opret.setEnabled(false);
			RaavareBatchDTO raavareBatch = new RaavareBatchDTO(Integer.parseInt(rbID.getText()),Integer.parseInt(raavareID.getText()),Double.parseDouble(mængde.getText()));

			Controller.service.createRaavareBatch(Controller.token, raavareBatch, new AsyncCallback<Void>(){

				@Override
				//				public void onFailure(Throwable caught) {
				//					opret.setEnabled(true);	
				//					error.setText(caught.getMessage());
				//					error.setStyleName("TextBox-ErrorMessage");
				//				}

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
						opret.setEnabled(true);
					} else {
						opret.setEnabled(true);
						error.setText(caught.getMessage());
						error.setStyleName("TextBox-ErrorMessage");	
					}
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Råvare Batch " + rbID.getText() + " blev oprettet!");
					vPane.clear();
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
				rbIDValid = false;
			} else{				
				if(rbListe[Integer.parseInt(id.getText())]==1){
					ft.setText(1, 2, "Råvare Batch ID optaget. Vælg et andet.");
					id.setStyleName("TextBox-OpretError");
					rbIDValid = false;
				} else {
					id.setStyleName("TextBox-Opret");
					rbIDValid = true;
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
				raavareIDValid = false;
			} else{
				if(raavareListe[Integer.parseInt(id.getText())]==1){
					id.setStyleName("TextBox-Opret");
					raavareIDValid = true;
				} else {
					ft.setText(2, 2, "Råvare ID ukendt. Vælg et andet.");
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
			if (!FieldVerifier.isValidMaengde(mængde.getText())){
				name.setStyleName("TextBox-OpretError");
				maengdeValid = false;
			}
			else{
				name.setStyleName("TextBox-Opret");
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