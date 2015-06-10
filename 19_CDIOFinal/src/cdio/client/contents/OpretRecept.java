package cdio.client.contents;

import java.util.jar.Attributes.Name;

import sun.print.resources.serviceui;
import cdio.client.ServiceAsync;
import cdio.shared.FieldVerifier;
import cdio.shared.ReceptDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class OpretRecept extends Composite {

	private VerticalPanel vPane;
	private FlexTable ft;
	private TextBox receptid, navn;
	private Button opret;
	private Label error;
	private boolean receptidValid, navnValid;
	private ServiceAsync service;
	private String token;

	//receptid: området 199999999 heltal 
	//navn: 2-20 karakterer

	public OpretRecept(String token, ServiceAsync service) {
		this.service = service;
		this.token = token;
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	private void run(){
		receptidValid = true;
		navnValid = false;
		vPane.clear();

		error = new Label("");
		ft = new FlexTable();
		ft.setStyleName("FlexTable-Content");
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Opret Recept");

		ft.setText(2, 0, "Receptnavn:");
		navn = new TextBox();
		navn.addKeyUpHandler(new NameCheck()); 
		navn.setStyleName("TextBox-Opret");
		ft.setWidget(2, 1, navn);

		ft.setText(3, 0, "Receptnummer:");
		receptid = new TextBox();
		receptid.addKeyUpHandler(new IdCheck()); 
		receptid.setStyleName("Textbox-Opret");
		ft.setWidget(3, 1, receptid);


		opret = new Button("Opret");
		opret.addClickHandler(new OpretClick());
		opret.setEnabled(false);
		ft.setWidget(10, 1, opret);

		vPane.add(ft);
		vPane.add(error);

	}

	private class OpretClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			opret.setEnabled(false);
			ReceptDTO recept = new ReceptDTO(
					Integer.parseInt(receptid.getText()), 
					navn.getText());

			service.createRecept(token, recept, new AsyncCallback<Void>(){

				@Override
				public void onFailure(Throwable caught) {
				opret.setEnabled(true);	
				error.setText(caught.getMessage());
				error.setStyleName("TextBox-ErrorMessage");
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Recept " + navn.getText() + " blev oprettet!");
					run();
				}

			} );

		}

	}

	private class NameCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox name = (TextBox) event.getSource();
			if (!FieldVerifier.isValidReceptName(name.getText())){
				name.setStyleName("TextBox-OpretError");
				navnValid = false;
			}
			else{
				name.setStyleName("TextBox-Opret");
				navnValid = true;
			} 
			if(navnValid && receptidValid)
				opret.setEnabled(true);
			else
				opret.setEnabled(false);	
		}

	}

	private class IdCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox id = (TextBox) event.getSource();
			if(!FieldVerifier.isValidUserId(id.getText())){
			id.setStyleName("TextBox-OpretError");
			receptidValid = false;
		} else{
			id.setStyleName("TextBox-Opret");
			receptidValid = true;
		}
			
			if(navnValid && receptidValid)
				opret.setEnabled(true);
			else opret.setEnabled(false);
		}
		
		
		
		
	}
	
	
}
