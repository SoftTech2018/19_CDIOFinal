package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisPB extends Composite {
	
	private VerticalPanel vPane;
	private Button pbkomp;
	private FlexTable ft;
	private String token;
	private ServiceAsync service;
	
	
	public VisPB(String token, final ServiceAsync service) {
		vPane = new VerticalPanel();
		initWidget(vPane);
		
		service.getPBList(token, new AsyncCallback<List<ProduktBatchDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<ProduktBatchDTO> result) {
				
				ft = new FlexTable();
				ft.setText(0, 0, "Produktbatch ID");
				ft.setText(0, 1, "Recept ID");
				ft.setText(0, 2, "Status");
				
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "75px");
				ft.getFlexCellFormatter().setWidth(0, 1, "75px");
				ft.getFlexCellFormatter().setWidth(0, 2, "75px");
				ft.getFlexCellFormatter().setWidth(0, 2, "50px");
				
				
				for (int i = 0; i < result.size(); i++) {
					ft.setText(i+1, 0, Integer.toString(result.get(i).getPbId()));
					ft.setText(i+1, 1, result.get(i).getReceptId());
					ft.setText(i+1, 2, result.get(i).getStatus());
					
					Button pbkomp = new Button("Komponenter");
					pbkomp.setStyleName("Button-Ret");
					pbkomp.addClickHandler(new kompClick());
					ft.setWidget(i+1, 3, pbkomp);
					
			}

			
}
});
}

	private class kompClick implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			
			final FlexTable ft2 = new FlexTable();
			
			
			service.getPBKList(token, new AsyncCallback<List<ProduktBatchKompDTO>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(List<ProduktBatchKompDTO> result) {
					ft.setWidget(ft.getCellForEvent(event).getRowIndex(), 3, ft2);
			
					ft2.setText(0, 0, "Råvarebatch");
					ft2.setText(0, 1, "Tara");
					ft2.setText(0, 2, "Netto");
					ft2.setText(0, 3, "Operatør ID");
					ft2.getRowFormatter().setStyleName(0, "FlexTable-Header");
					ft2.getFlexCellFormatter().setWidth(0, 0, "75px");
					ft2.getFlexCellFormatter().setWidth(0, 1, "75px");
					ft2.getFlexCellFormatter().setWidth(0, 2, "75px");
					ft2.getFlexCellFormatter().setWidth(0, 3, "75px");
			
			for (int i = 0; i < array.length; i++) {
				
			}
				}
				
			});
			
			
		}
		
	}
	
}
