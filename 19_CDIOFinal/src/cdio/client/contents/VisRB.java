package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.server.DAL.dto.RaavareBatchDTO;
import cdio.shared.RaavareDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisRB extends Composite {

	private VerticalPanel vPane;


	public VisRB(String token, final ServiceAsync service) {
		vPane = new VerticalPanel();
		initWidget(vPane);


		service.getRaavareBatchList(token, new AsyncCallback<List<RaavareBatchDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<RaavareBatchDTO> result) {
				FlexTable ft = new FlexTable();
				ft.setText(0, 0, "RB ID");
				ft.setText(0, 1, "Raavare ID");
				ft.setText(0, 2, "Mængde");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "75px");
				ft.getFlexCellFormatter().setWidth(0, 1, "110px");
				ft.getFlexCellFormatter().setWidth(0, 2, "110px");


				for (int i = 0; i < result.size(); i++) {
					ft.setText(i+1, 0, Integer.toString(result.get(i).getRbId()));
					ft.setText(i+1, 1, Integer.toString(result.get(i).getRaavareId()));
					ft.setText(i+1, 2, Double.toString(result.get(i).getMaengde()));	
				}
			}
		});
	}
}



