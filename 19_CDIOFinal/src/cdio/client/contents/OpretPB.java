package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ReceptDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpretPB extends Composite {

	private String token;
	private ServiceAsync service;
	private VerticalPanel vPane;
	private Label error;
	private FlexTable ft;
	private Button ok;
	private ListBox receptNr;
	
	public OpretPB(String token, final ServiceAsync service){
		this.token = token;
		this.service = service;
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}
	
	private void run(){
		// Reset til 'blank' position
		vPane.clear();
		
		// Byg siden
		error = new Label("");
		ft = new FlexTable();
		ft.setStyleName("FlexTable-Content");
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");	
		ft.setText(0, 0, "Opret Produktbatch");
		
		ft.setText(1, 0, "Receptnummer:");
		receptNr = new ListBox();
		receptNr.setVisibleItemCount(1); // Laver det til en dropdown-menu
		ft.setWidget(1, 1, receptNr);
		service.getReceptList(token, new AsyncCallback<List<ReceptDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				error.setText(caught.getMessage());
				error.setStyleName("TextLabel-ErrorMessage");
			}

			@Override
			public void onSuccess(List<ReceptDTO> result) {
				receptNr.addItem("Vælg recept");
				for (int i=0; i<result.size(); i++){
					ReceptDTO r = result.get(i);
					receptNr.addItem(r.getReceptId() + " : " + r.getReceptNavn());
				}
			}
			
		});
		ok = new Button("Opret");
		ok.addClickHandler(new OpretHandler());
		ft.setWidget(2, 1, ok);
		ft.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		
		vPane.add(ft);
		vPane.add(error);
		
	}
	
	private class OpretHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			ok.setEnabled(false);
			ok.setText("Loading");
			ProduktBatchDTO pb = new ProduktBatchDTO();
			pb.setPbId(0);
			int receptId = -1;
			String recept = receptNr.getItemText(receptNr.getSelectedIndex());
			for (int i=0; i<recept.length(); i++){
				if (recept.charAt(i) == ':')
					receptId = Integer.parseInt(recept.substring(0, i-1));
			}
			pb.setReceptId(receptId);
			pb.setStatus(0);
			service.createPB(token, pb, new AsyncCallback<ProduktBatchDTO>(){

				@Override
				public void onFailure(Throwable caught) {
					error.setText(caught.getMessage());
					error.setStyleName("TextLabel-ErrorMessage");					
				}

				@Override
				public void onSuccess(ProduktBatchDTO result) {
					vPane.clear();
					vPane.add(new PrintPB(result));
				}
				
			});
			
		}
		
	}
	
	private class PrintPB extends Composite{
		
		private VerticalPanel pbvPane;
		private FlexTable ft;
		
		public PrintPB(ProduktBatchDTO pb){
			pbvPane = new VerticalPanel();
			initWidget(pbvPane);
			ft = new FlexTable();
			pbvPane.add(ft);
			
			ft.setText(0, 0, "Udskrevet");
			ft.setText(0, 1, pb.getDato());
			
			ft.setText(1, 0, "Produkt Batch nr.");
			ft.setText(1, 1, Integer.toString(pb.getPbId()));
			
			ft.setText(2, 0, "Recept nr.");
			ft.setText(2, 1, Integer.toString(pb.getReceptId()));
			ft.setText(3, 0, "");
			
			ft.setText(4, 0, "Loading...");
			service.getPbView(token, pb.getReceptId(), new AsyncCallback<List<PbViewDTO>>(){

				@Override
				public void onFailure(Throwable caught) {
					error.setText(caught.getMessage());
					error.setStyleName("TextLabel-ErrorMessage");
				}

				@Override
				public void onSuccess(List<PbViewDTO> result) {
					int row;
					for (row = 5; row-5<result.size(); row++){
						FlexTable ft2 = new FlexTable();
						PbViewDTO p = result.get(row-5);
						ft2.setText(0, 0, "Råvare nr.");
						ft2.setText(0, 1, p.getRaavareNavn());
						ft2.setText(1, 0, "Råvare navn:");
						ft2.setText(1, 1, Integer.toString(p.getRaavareId()));
						
						ft2.setText(2, 0, "Del");
						ft2.setText(2, 1, "Mængde");
						ft2.setText(2, 2, "Tolerance");
						ft2.setText(2, 3, "Tara");
						ft2.setText(2, 4, "Netto (kg)");
						ft2.setText(2, 5, "Batch");
						ft2.setText(2, 6, "Opr.");
						ft2.setText(2, 7, "Terminal)");
						
						ft2.setText(3, 0, "1");
						ft2.setText(3, 1, Double.toString(p.getMaengde()));
						ft2.setText(3, 2, Double.toString(p.getTolerance()));
						ft2.setText(3, 3, Double.toString(p.getTara()));
						ft2.setText(3, 4, Double.toString(p.getNetto()));
						ft2.setText(3, 5, Integer.toString(p.getBatch()));
						ft2.setText(3, 6, Integer.toString(p.getOpr()));
						ft2.setText(3, 7, Integer.toString(p.getTerminal()));
						
						ft.setWidget(row, 0, ft2);
					}
					double taraSum = 0;
					double nettoSum = 0;
					for (int i=0; i<result.size(); i++){
						taraSum = taraSum + result.get(i).getTara();
						nettoSum = nettoSum + result.get(i).getNetto();
					}
					ft.setText(row+1, 0, "Sum Tara:");
					ft.setText(row+1, 1, Double.toString(taraSum));
					ft.setText(row+2, 0, "Sum Netto:");
					ft.setText(row+2, 1, Double.toString(nettoSum));
					
					ft.setText(row+3,  0, "Produktion Status:");
				}
			});
		}
	}
}
