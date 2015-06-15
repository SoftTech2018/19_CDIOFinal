package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.ServiceAsync;
import cdio.shared.ReceptDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisRecept extends Composite {

	private VerticalPanel vPane;
	private Label error;

	public VisRecept() {
		vPane = new VerticalPanel();
		error = new Label("Loading...");
		vPane.add(error);
		initWidget(vPane);

		Controller.service.getReceptList(Controller.token, new AsyncCallback<List<ReceptDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ReceptDTO> result) {
				error.setText("");
				FlexTable ft = new FlexTable();
				ft.setStyleName("FlexTable-Content");	
				ft.setText(0, 0, "Recept Id");
				ft.setText(0, 1, "Recept navn");


				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "150px");
				ft.getFlexCellFormatter().setWidth(0, 1, "150px");
				ft.getFlexCellFormatter().setWidth(0, 2, "55px");
				ft.getFlexCellFormatter().setWidth(0, 3, "85px");
				ft.getFlexCellFormatter().setWidth(0, 4, "85px");
				ft.getFlexCellFormatter().setWidth(0, 5, "55px");
				ft.getFlexCellFormatter().setWidth(0, 6, "75px");
				ft.getFlexCellFormatter().setWidth(0, 7, "75px");
				ft.getFlexCellFormatter().setWidth(0, 8, "75px");

				ft.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_CENTER);
				ft.getCellFormatter().setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_CENTER);
				ft.getCellFormatter().setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_CENTER);
				ft.getCellFormatter().setHorizontalAlignment(0, 7, HasHorizontalAlignment.ALIGN_CENTER);
				for (int i = 0; i < result.size(); i++) {
					ft.getCellFormatter().setHorizontalAlignment(i+1, 4, HasHorizontalAlignment.ALIGN_CENTER);
					ft.getCellFormatter().setHorizontalAlignment(i+1, 5, HasHorizontalAlignment.ALIGN_CENTER);
					ft.getCellFormatter().setHorizontalAlignment(i+1, 6, HasHorizontalAlignment.ALIGN_CENTER);
					ft.getCellFormatter().setHorizontalAlignment(i+1, 7, HasHorizontalAlignment.ALIGN_CENTER);
					vPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					ft.setText(i+1,0,Integer.toString(result.get(i).getReceptId()));
					ft.setText(i+1, 1, result.get(i).getReceptNavn());


					vPane.add(ft);
				}

			}
		
		} );
	}
}	
	