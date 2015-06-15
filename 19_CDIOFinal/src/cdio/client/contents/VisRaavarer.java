package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.ServiceAsync;
import cdio.shared.RaavareDTO;
import cdio.shared.TokenException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VisRaavarer extends Composite {
	
		private VerticalPanel vPane;
		private Label error;
		private Button ok;
		
		public VisRaavarer() {
			vPane = new VerticalPanel();
			error = new Label("Loading...");
			vPane.add(error);
			initWidget(vPane);
			
			
			Controller.service.getRaavareList(Controller.token, new AsyncCallback<List<RaavareDTO>>() {

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
						ok.setEnabled(true);
					} else {
						ok.setEnabled(true);
						error.setText(caught.getMessage());
						error.setStyleName("TextBox-ErrorMessage");	
					}
				}

				@Override
				public void onSuccess(List<RaavareDTO> result) {
					Controller.refreshToken();
					vPane.clear();
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
