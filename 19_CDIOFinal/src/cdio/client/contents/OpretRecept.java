package cdio.client.contents;

import java.util.jar.Attributes.Name;

import sun.print.resources.serviceui;
import cdio.client.Controller;
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

	private VerticalPanel vPane, vPane1;
	private FlexTable ft, ft3, ft2, ft4;
	private TextBox receptid, navn, raavareid, nomNetto, tolerance;
	private Button opret, tilfoej, gemKomp, ok, nyRecept;
	private Label error;
	private boolean receptidValid, navnValid, nettoValid, tolValid, raavareidValid, receptOprettet, netEle, tolEle, ravEle;
	private HorizontalPanel hp;
	
	
	public OpretRecept() {
		hp = new HorizontalPanel();
		initWidget(hp);
		run();
	}

	private void run(){
		vPane = new VerticalPanel();
		vPane1 = new VerticalPanel();
		receptidValid = true;
		navnValid = false;
		nettoValid = false;
		tolValid = false;
		raavareidValid=false;
		receptOprettet=false;
		netEle = false;
		tolEle = false;
		ravEle = false;

		hp.clear();
		
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

		nyRecept = new Button("Start forfra");
		nyRecept.addClickHandler(new nyReceptClick());
		nyRecept.setEnabled(true);
		ft.setWidget(10, 0, nyRecept);

		opret = new Button("Opret");
		opret.addClickHandler(new OpretClick());
		opret.setEnabled(false);
		ft.setWidget(10, 1, opret);		

		ft3 = new FlexTable();
		tilfoej = new Button("Ny komponent");
		tilfoej.setStyleName("Recept-Komponenter");
		tilfoej.addClickHandler(new kompClick());
		ft3.setWidget(0, 1, tilfoej);
		ft3.setText(0, 0, "Ny receptKomponent");

		error = new Label("");
		
		ft4 = new FlexTable();
		ft4.setWidget(12, 0, error);
		ft4.setText(0, 1, "");
		ft4.setWidget(0, 2, error);
		
		
		
		vPane.add(ft);	
		vPane.add(ft4);
		vPane1.add(ft3);
		
		
		
		
		vPane.setWidth("400 px");
		vPane1.setWidth("400 px");
		//hp.add(error);
		hp.add(vPane);
		hp.add(vPane1);
		
	}
	
	private class nyReceptClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			run();
		}
	}
	
	
	
	private class gemKomp implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			ReceptKompDTO receptKomp = new ReceptKompDTO(
					Integer.parseInt(receptid.getText()),
					Integer.parseInt(raavareid.getText()),
					Double.parseDouble(nomNetto.getText()),
					Double.parseDouble(tolerance.getText()));
			
			Controller.service.createReceptKomp(Controller.token, receptKomp, new AsyncCallback<Void>(){

				@Override
				public void onFailure(Throwable caught) {
					opret.setEnabled(true);
					error.setText(caught.getMessage());
					error.setStyleName("Recept-Error");
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Receptkomponent oprettet!");
					error.setStyleName("Recept-Error");
					error.setText("Receptkomponent med råvareid "+raavareid.getText() +" er oprettet. Tilføj flere ved at vælge 'ny komponent'");
				

				}

			});

		}
		
	}

	private class OpretClick implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			opret.setEnabled(false);

			ReceptDTO recept = new ReceptDTO(
					Integer.parseInt(receptid.getText()), 
					navn.getText());
			
			Controller.service.createRecept(Controller.token, recept, new AsyncCallback<Void>(){

				@Override
				public void onFailure(Throwable caught) {
					opret.setEnabled(true);	
					error.setText(caught.getMessage());
					error.setStyleName("Recept-Error");
					
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Recept " + navn.getText() + " blev oprettet!");
					receptOprettet = true;
					error.setText("Recept er oprettet. Tilføj nye receptkomponenter!");
					error.setStyleName("Recept-Error");
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
			opret.setEnabled(false);
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

			gemKomp = new Button();
			gemKomp = new Button("Gem Komponent");
			gemKomp.setStyleName("Recept-Komponenter");
			gemKomp.addClickHandler(new gemKomp());
			ft3.setWidget(0, 8, gemKomp);
			gemKomp.setEnabled(true);
			
			ft2.setWidget(3, 1, tolerance);

			
			vPane1.add(ft2);

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
				Controller.service.getRaavareID(Controller.token, Integer.parseInt(id.getText()), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						raavareidValid = false;
						error.setText(caught.getMessage());
						error.setStyleName("Recept-Error");
					}

					@Override
					public void onSuccess(Void result) {
						raavareidValid = true;
					}

				});

				if( receptOprettet && raavareidValid && nettoValid && tolValid && netEle && tolEle && ravEle){
					gemKomp.setEnabled(true);
				}
				else gemKomp.setEnabled(false);
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
			if( receptOprettet && raavareidValid && nettoValid && tolValid && netEle && tolEle && ravEle)
				gemKomp.setEnabled(true);
			else gemKomp.setEnabled(false);
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
			if( receptOprettet && raavareidValid && nettoValid && tolValid && netEle && tolEle && ravEle)
				gemKomp.setEnabled(true);
			else gemKomp.setEnabled(false);
		}

	}
}