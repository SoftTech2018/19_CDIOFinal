package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.RaavareDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RetRaavare  extends Composite {
	
	private Label error;
	private VerticalPanel vPane;
	private ServiceAsync service;
	private String token;
	private FlexTable ft;

	public RetRaavare(String token, ServiceAsync service) {
		this.service = service;
		this.token = token;
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}
	
	public void run(){
		vPane.clear();
		error = new Label("Loading...");
		vPane.add(error);
		service.getRaavareList(token, new AsyncCallback<List<RaavareDTO>>(){

		
			public void onFailure(Throwable caught) {
				error.setText(caught.getMessage());	
				error.setStyleName("TextLabel-ErrorMessage");
			}

		
			public void onSuccess(List<RaavareDTO> result) {
				error.setText("");
				ft = new FlexTable();
				ft.setStyleName("FlexTable-Content");
				ft.setText(0, 0, "ID");
				ft.setText(0, 1, "Navn");
				ft.setText(0, 2, "Leverandør");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "30px");
				ft.getFlexCellFormatter().setWidth(0, 1, "150px");
				ft.getFlexCellFormatter().setWidth(0, 2, "150px");
				
				for (int i=0; i<result.size(); i++){
					ft.setText(i+1, 0, Integer.toString(result.get(i).getRaavareId()));
					ft.setText(i+1, 1, result.get(i).getRaavareNavn());
					ft.setText(i+1, 2, result.get(i).getLeverandoer());
				}
				
				vPane.add(ft);
			}
			
		});
	}

}
