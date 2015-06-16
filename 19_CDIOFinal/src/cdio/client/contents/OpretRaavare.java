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

public class OpretRaavare extends Composite {

	private VerticalPanel vPane;
	private FlexTable ft;
	private TextBox id, navn, leverandør;
	private Button opret;
	private Label error, opretRaavarer;
	private boolean idValid=false, navnValid=false, levValid=false;
	private int[] liste;

	public OpretRaavare() {
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	public void run(){
		//		opretRaavarer = new Label("Opret råvare");
		//		opretRaavarer.setStyleName("FlexTable-Header");
		//		vPane.add(opretRaavarer);

		ft = new FlexTable();
		ft.setStyleName("FlexTable-Content");
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Opret Råvare");

		ft.setText(1, 0, "Råvare ID:");
		id = new TextBox();
		id.addKeyUpHandler(new IdCheck()); 
		id.setStyleName("TextBox-Opret");
		ft.setWidget(1, 1, id);

		ft.setText(2, 0, "Råvare navn:");
		navn = new TextBox();
		navn.addKeyUpHandler(new NameCheck()); 
		navn.setStyleName("Textbox-Opret");
		ft.setWidget(2, 1, navn);

		ft.setText(3, 0, "Leverandør:");
		leverandør = new TextBox();
		leverandør.addKeyUpHandler(new LevCheck()); 
		leverandør.setStyleName("Textbox-Opret");
		ft.setWidget(3, 1, leverandør);

		opret = new Button("Opret");
		opret.addClickHandler(new OpretClick());
		opret.setEnabled(false);
		ft.setWidget(10, 1, opret);



		vPane.add(ft);
		getListe();
	}

	private void getListe(){
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
					ft.setText(1, 2, "Fejl i listekald");
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

				liste = new int[i+1];

				for(RaavareDTO rv : result){
					liste[rv.getRaavareId()]=1;
				}
			}					
		});
	}

	private class OpretClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			opret.setEnabled(false);
			RaavareDTO raavare = new RaavareDTO(Integer.parseInt(id.getText()),navn.getText(),leverandør.getText());

			Controller.service.createRaavare(Controller.token, raavare, new AsyncCallback<Void>(){

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
						opret.setEnabled(true);
					} else {
						opret.setEnabled(true);
						error.setText(caught.getMessage());
						error.setStyleName("TextBox-ErrorMessage");	
					}
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Råvare " + navn.getText() + " blev oprettet!");
					vPane.clear();
					run();
				}

			} );

		}

	}

	private class IdCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			final TextBox id = (TextBox) event.getSource();
			ft.setText(1, 2, "");
			if(!FieldVerifier.isValidRaavareId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				idValid = false;
			} else{				
				if(liste[Integer.parseInt(id.getText())]==1){
					ft.setText(1, 2, "Råvare ID optaget. Vælg et andet.");
					id.setStyleName("TextBox-OpretError");
					idValid = false;
				} else {
					id.setStyleName("TextBox-Opret");
					idValid = true;
				}
			}

			if(navnValid && idValid && levValid)
				opret.setEnabled(true);
			else {
				opret.setEnabled(false);
			}
		}

	}

	private class NameCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox name = (TextBox) event.getSource();
			if (!FieldVerifier.isValidRaavareName(name.getText())){
				name.setStyleName("TextBox-OpretError");
				navnValid = false;
			}
			else{
				name.setStyleName("TextBox-Opret");
				navnValid = true;

			} 
			if(navnValid && idValid && levValid)
				opret.setEnabled(true);
			else {
				opret.setEnabled(false);
			}
		}

	}

	private class LevCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox name = (TextBox) event.getSource();
			if (!FieldVerifier.isValidLeverandorName(name.getText())){
				name.setStyleName("TextBox-OpretError");
				levValid = false;
			}
			else{
				name.setStyleName("TextBox-Opret");
				levValid = true;

			} 
			if(navnValid && idValid && levValid)
				opret.setEnabled(true);
			else {
				opret.setEnabled(false);
			}

		}

	}




}
