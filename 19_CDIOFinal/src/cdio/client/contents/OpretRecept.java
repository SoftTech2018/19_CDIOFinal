package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.DALException;
import cdio.shared.FieldVerifier;
import cdio.shared.RaavareDTO;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpretRecept extends Composite {

	
	
	private VerticalPanel vPane, vPane1;
	private FlexTable ft, ft3, ft2, ft4;
	private TextBox receptid, navn, raavareid, nomNetto, tolerance;
	private Button opret, tilfoej, gemKomp, ok, nyRecept;
	private Label error;
	private boolean receptidValid, navnValid, nettoValid, tolValid, raavareidValid, receptOprettet, netEle, tolEle, ravEle;
	private HorizontalPanel hp;
	private int[] liste, receptListe;

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
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setStyleName("FlexTable-Content");
		ft.setText(0, 0, "Opret Recept");

		ft.setText(2, 0, "Receptnavn:");
		navn = new TextBox();

		navn.addKeyUpHandler(new NameCheck()); 
		navn.setStyleName("TextBox-Opret");
		navn.setWidth("70%");
		ft.setWidget(2, 1, navn);

		ft.setText(3, 0, "Receptnummer:");
		receptid = new TextBox();
		receptid.addKeyUpHandler(new IdCheck()); 
		receptid.setStyleName("Textbox-Opret");
		receptid.setWidth("70%");
		ft.setWidget(3, 1, receptid);

		nyRecept = new Button("Start forfra");
		nyRecept.addClickHandler(new nyReceptClick());
		nyRecept.setEnabled(true);
		nyRecept.setStyleName("Recept-Komponenter");
		ft.setWidget(10, 0, nyRecept);
		
		opret = new Button("Opret");
		opret.setStyleName("Recept-Komponenter");
		opret.addClickHandler(new OpretClick());
		opret.setEnabled(false);
		ft.setWidget(10, 1, opret);		

		ft3 = new FlexTable();
		ft3.setStyleName("FlexTable-Content");
		tilfoej = new Button("Ny komponent");
		tilfoej.setStyleName("Recept-Komponenter");
		tilfoej.setEnabled(false);
		tilfoej.addClickHandler(new kompClick());
		ft3.setWidget(0, 1, tilfoej);
		ft3.setText(0, 0, "");

		error = new Label("");

		ft4 = new FlexTable();
//		ft4.setStyleName("FlexTable-Content");
		ft4.setWidget(12, 0, error);
		ft4.setText(0, 1, "");
		ft4.setWidget(0, 2, error);

		vPane.add(ft);	
		vPane.add(ft4);
		vPane1.add(ft3);

		vPane.setWidth("400px");
		vPane1.setWidth("400px");
		//hp.add(error);
		hp.add(vPane);
		hp.add(vPane1);

		getReceptListe();
		
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
				}
				else{
					ft.setText(1, 2, "Fejl i listekald");
				}}

			
			@Override
			public void onSuccess(List<RaavareDTO> result) {
				Controller.refreshToken();
				tilfoej.setText("Ny komponent");
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
	
	
	private void getReceptListe(){
		Controller.service.getReceptList(Controller.token, new AsyncCallback<List<ReceptDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				ft.setText(2, 2, caught.getMessage());
				
			}

			@Override
			public void onSuccess(List<ReceptDTO> result) {
				Controller.refreshToken();
				int i = 0;
				for(ReceptDTO rc : result){
					if(rc.getReceptId()>i){
						i=rc.getReceptId();
					}
				}
				
				receptListe = new int[i+1];
				for(ReceptDTO rc : result){
					receptListe[rc.getReceptId()]=1;
				}
			}
			
		});
		
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
			nyRecept.setEnabled(false);
			gemKomp.setText("Loading");
			gemKomp.setEnabled(false);

			String netto = nomNetto.getText();
			for(int i = 0; i<netto.length(); i++){
				if (netto.charAt(i)==','){
					netto = netto.replace(",", ".");
				}
			}

			String tol = tolerance.getText();
			for(int i=0; i < tol.length(); i++){
				if(tol.charAt(i)==','){
					tol = tol.replace(",", ".");
				}
			}

			ReceptKompDTO receptKomp = new ReceptKompDTO(
					Integer.parseInt(receptid.getText()),
					Integer.parseInt(raavareid.getText()),
					Double.parseDouble(netto),
					Double.parseDouble(tol));

			Controller.service.createReceptKomp(Controller.token, receptKomp, new AsyncCallback<Void>(){

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
						gemKomp.setText("Gem Komponent");
						gemKomp.setEnabled(true);
						nyRecept.setEnabled(true);
					}
					else{
						nyRecept.setEnabled(true);
						gemKomp.setText("Gem Komponent");
						gemKomp.setEnabled(true);
						error.setText(caught.getMessage());
						error.setStyleName("Recept-Error");
					}
				}

				@Override
				public void onSuccess(Void result) {
					Controller.refreshToken();
					gemKomp.setText("Gem Komponent");
					gemKomp.setEnabled(false);
					Window.alert("Receptkomponent oprettet!");
					error.setStyleName("Recept-Positiv");
					error.setText("Receptkomponent med råvareid "+raavareid.getText() +" er oprettet. Tilføj flere ved at vælge 'ny komponent'");
					tilfoej.setEnabled(true);
					nyRecept.setEnabled(true);
				}
			});
		}
	}

	private class OpretClick implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			opret.setText("loading");
			opret.setEnabled(false);
			
			ReceptDTO recept = new ReceptDTO(
					Integer.parseInt(receptid.getText()), 
					navn.getText());

			Controller.service.createRecept(Controller.token, recept, new AsyncCallback<Void>(){

				
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
						}
								);
						opret.setText("Opret");
						opret.setEnabled(true);		
					}

					else{
						opret.setText("Opret");
						opret.setEnabled(true);	
						error.setText(caught.getMessage());
						error.setStyleName("Recept-Error");
					}
				}

				@Override
				public void onSuccess(Void result) {
					Controller.refreshToken();
					opret.setText("Opret");
					opret.setEnabled(false);
					Window.alert("Recept " + navn.getText() + " blev oprettet!");
					receptOprettet = true;
					tilfoej.setEnabled(true);
					error.setText("Recept er oprettet. Tilføj nye receptkomponenter!");
					error.setStyleName("Recept-Positiv");
				}
			}
					);
		}
	}

	private class NameCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox name = (TextBox) event.getSource();
			if (!FieldVerifier.isValidReceptName(name.getText())){
				name.setStyleName("TextBox-OpretError");
				navnValid = false;
			} else {
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
			error.setText("");
			if(!FieldVerifier.isValidUserId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				receptidValid = false;
			}	
			
			else{
				id.setStyleName("TextBox-Opret");
				receptidValid = true;
			}
			
			if(receptidValid){
				if(receptListe[Integer.parseInt(id.getText())]==1){
					error.setText("ReceptId optaget. Vælg et andet.");
					error.setStyleName("Recept-Error");
					id.setStyleName("TextBox-OpretError");
					receptidValid = false;
				}
			}

			if(navnValid && receptidValid)
				opret.setEnabled(true);
			else opret.setEnabled(false);
		}
	}

	private class kompClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			tilfoej.setText("loading..");
			getRaavareListe();
			opret.setEnabled(false);
			tilfoej.setEnabled(false);
			FlexTable ft2 = new FlexTable();
			ft2.setStyleName("FlexTable-Content");
			ft2.setText(1, 0, "Indtast råvareID");
			raavareid = new TextBox();
			raavareid.setStyleName("TextBox-Opret");
			raavareid.addKeyUpHandler(new rIdCheck()); 
			ft2.setWidget(1, 1, raavareid);
			String rid = raavareid.getText();
			if (rid != null){
				ravEle = true;}

			ft2.setText(2, 0, "Indtast nettovægt mellem 0,05-20,0 kg");
			nomNetto = new TextBox();
			nomNetto.setStyleName("TextBox-Opret");
			nomNetto.addKeyUpHandler(new nettoCheck());
			String nNet = nomNetto.getText();
			if(nNet != null){
				netEle = true;
			}

			ft2.setWidget(2, 1, nomNetto);

			ft2.setText(3, 0, "Indtast tolerance mellem 0,1-10,0%");
			tolerance = new TextBox();
			tolerance.setStyleName("TextBox-Opret");
			tolerance.addKeyUpHandler(new tolCheck());
			String tol = tolerance.getText();
			if(tol != null){
				tolEle = true; 
			}
			ft2.setWidget(3, 1, tolerance);

			gemKomp = new Button();
			gemKomp = new Button("Gem Komponent");
			gemKomp.setStyleName("Recept-Komponenter");
			gemKomp.addClickHandler(new gemKomp());
			gemKomp.setEnabled(false);
			ft2.setWidget(1, 8, gemKomp);


			vPane1.add(ft2);

		}
	}

	private class rIdCheck implements KeyUpHandler{
		@Override
		public void onKeyUp(KeyUpEvent event) {
			error.setText("");
			TextBox id = (TextBox) event.getSource();
			if(!FieldVerifier.isValidUserId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				raavareidValid = false;

			} else{
				id.setStyleName("TextBox-Opret");
				raavareidValid = true;
			}

			if(raavareidValid){
				if(liste[Integer.parseInt(id.getText())]!=1){
					error.setText("Ukendt råvareId, prøv et andet");
					error.setStyleName("Recept-Error");
					id.setStyleName("TextBox-OpretError");
					raavareidValid = false;
				}

				if( receptOprettet && raavareidValid && nettoValid && tolValid && netEle && tolEle && ravEle){
					gemKomp.setEnabled(true);
				}
				else{ gemKomp.setEnabled(false);
				tilfoej.setEnabled(false);}
				
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
			else {gemKomp.setEnabled(false);
			tilfoej.setEnabled(false);
			}}
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
			else {gemKomp.setEnabled(false);
			tilfoej.setEnabled(false);
			}}
	}
}