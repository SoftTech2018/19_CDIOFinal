package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.ServiceAsync;
import cdio.shared.RaavareDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VisRaavarer extends Composite {
	
		private VerticalPanel vPane;
		
		public VisRaavarer() {
			vPane = new VerticalPanel();
			initWidget(vPane);
			
			Controller.service.getRaavareList(Controller.token, new AsyncCallback<List<RaavareDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					
					FlexTable ft = new FlexTable();
					ft.setText(0, 0, "Fejl");
					vPane.add(ft);
				}

				@Override
				public void onSuccess(List<RaavareDTO> result) {
					FlexTable ft = new FlexTable();
					ft.setText(0, 0, "ID");
					ft.setText(0, 1, "Navn");
					ft.setText(0, 2, "Leverandør");
					ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
					ft.getFlexCellFormatter().setWidth(0, 0, "75px");
					ft.getFlexCellFormatter().setWidth(0, 1, "110px");
					ft.getFlexCellFormatter().setWidth(0, 2, "110px");
					
					
					for (int i = 0; i < result.size(); i++) {
						ft.setText(i+1, 0, Integer.toString(result.get(i).getRaavareId()));
						ft.setText(i+1, 1, result.get(i).getRaavareNavn());
						ft.setText(i+1, 2, result.get(i).getLeverandoer());
						
					}
					vPane.add(ft);
				}
				
			});
		}
}
