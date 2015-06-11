package cdio.client.contents;

import java.util.jar.Attributes.Name;

import sun.print.resources.serviceui;
import cdio.client.ServiceAsync;
import cdio.shared.FieldVerifier;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class OpretRecept extends Composite {

	private VerticalPanel vPane;
	private FlexTable ft;
	private TextBox receptid, navn, raavareid, nomNetto, tolerance;
	private Button opret, tilfoej, ok;
	private Label error;
	private boolean receptidValid, navnValid, nettoValid, tolValid, raavareidValid, kompValid, netEle, tolEle, ravEle;
	private ServiceAsync service;
	private String token;
	private String[] split;

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
		nettoValid = false;
		tolValid = false;
		raavareidValid=false;
		kompValid=false;
		netEle = false;
		tolEle = false;
		ravEle = false;

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

		tilfoej = new Button("Ny komponent");
		tilfoej.setStyleName("Recept-Komponenter");
		tilfoej.addClickHandler(new kompClick());
		ft.setWidget(4, 0, tilfoej);

		vPane.add(ft);
		vPane.add(error);


	}

	private class OpretClick implements ClickHandler{




		@Override
		public void onClick(ClickEvent event) {
			opret.setEnabled(false);

			ReceptKompDTO receptKomp = new ReceptKompDTO(
					Integer.parseInt(receptid.getText()),
					Integer.parseInt(raavareid.getText()),
					Double.parseDouble(nomNetto.getText()),
					Double.parseDouble(tolerance.getText()));

			service.createReceptKomp(token, receptKomp, new AsyncCallback<Void>(){

				@Override
				public void onFailure(Throwable caught) {
					opret.setEnabled(true);
					error.setText(caught.getMessage());
					error.setStyleName("TextBox-ErrorMessage");

				}

				@Override
				public void onSuccess(Void result) {

					
				}

			});
			
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

	private class kompClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			FlexTable ft2 = new FlexTable();
			ok = new Button("Tilføj");
			ft2.setText(1, 0, "RåvareID");
			raavareid = new TextBox();
			raavareid.addKeyUpHandler(new rIdCheck()); 
			ft2.setWidget(1, 1, raavareid);
			String rid = raavareid.getText();
			if (rid != null){
				ravEle = true;}



			ft2.setText(2, 0, "Nominel Netto");
			nomNetto = new TextBox();
			nomNetto.addKeyUpHandler(new nettoCheck());
			String nNet = nomNetto.getText();
			if(nNet != null){
				netEle = true;
			}



			ft2.setWidget(2, 1, nomNetto);


			ft2.setText(3, 0, "Tolerance");
			tolerance = new TextBox();
			tolerance.addKeyUpHandler(new tolCheck());
			String tol = tolerance.getText();
			if(tol != null){
				tolEle = true; 
			}


			ft2.setWidget(3, 1, tolerance);

			ft2.getFlexCellFormatter().setWidth(0, 0, "100px");
			ft2.getFlexCellFormatter().setWidth(0, 1, "100px");
			ft2.getFlexCellFormatter().setWidth(0, 2, "70px");
			ft2.getFlexCellFormatter().setWidth(0, 3, "70px");
			ft2.getFlexCellFormatter().setWidth(0, 4, "100px");
			vPane.add(ft2);			


		}



	}

	private class rIdCheck implements KeyUpHandler{
		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox id = (TextBox) event.getSource();
			if(!FieldVerifier.isValidUserId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				raavareidValid = false;

			} else{
				id.setStyleName("TextBox-Opret");
				raavareidValid = true;
			}

			if(raavareidValid){
				service.getRaavareID(token, Integer.parseInt(id.getText()), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						raavareidValid = false;
						error.setText(caught.getMessage());
						error.setStyleName("TextBox-ErrorMessage");
					}

					@Override
					public void onSuccess(Void result) {
						raavareidValid = true;
					}

				});

				if(navnValid && receptidValid && raavareidValid && nettoValid && tolValid && netEle && tolEle && ravEle){
					opret.setEnabled(true);
				}
				else opret.setEnabled(false);
			}

		}
	}
	private class nettoCheck implements KeyUpHandler{
		public void onKeyUp(KeyUpEvent event) {
			TextBox tb = (TextBox) event.getSource();

			if(!FieldVerifier.isValidNetto(tb.getText())){

				tb.setStyleName("TextBox-OpretError");
				nettoValid = false;

			} else{
				tb.setStyleName("TextBox-Opret");
				nettoValid = true;
			}
			if(navnValid && receptidValid && raavareidValid && nettoValid && tolValid && netEle && tolEle && ravEle)
				opret.setEnabled(true);
			else opret.setEnabled(false);
		}
	}

	private class tolCheck implements KeyUpHandler{

		public void onKeyUp(KeyUpEvent event) {
			TextBox ab = (TextBox) event.getSource();
			if(!FieldVerifier.isValidTol(ab.getText())){
				ab.setStyleName("TextBox-OpretError");
				tolValid= false;
			} else{
				ab.setStyleName("TextBox-Opret");
				tolValid = true;
			}
			if(navnValid && receptidValid && raavareidValid && nettoValid && tolValid && netEle && tolEle && ravEle)
				opret.setEnabled(true);
			else opret.setEnabled(false);
		}

	}
}

