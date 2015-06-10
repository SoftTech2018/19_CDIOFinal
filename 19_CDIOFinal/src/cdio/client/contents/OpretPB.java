package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ReceptDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
			pb.setReceptId(receptNr.getTabIndex()-1);
			pb.setStatus(0);
			service.createPB(token, pb, new AsyncCallback<ProduktBatchDTO>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ProduktBatchDTO result) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		}
		
	}
}
