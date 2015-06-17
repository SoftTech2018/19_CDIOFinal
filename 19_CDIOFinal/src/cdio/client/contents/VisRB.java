package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.RaavareBatchDTO;
import cdio.shared.TokenException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisRB extends Composite {

	private VerticalPanel vPane;
	private FlexTable ft;
	private Label error, visRB;
	private Button ok;

	public VisRB() {
		vPane = new VerticalPanel();
		initWidget(vPane);
		ft = new FlexTable();
		error = new Label("Loading...");
		vPane.add(error);

		Controller.service.getRaavareBatchList(Controller.token, new AsyncCallback<List<RaavareBatchDTO>>() {

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
			public void onSuccess(List<RaavareBatchDTO> result) {
				Controller.refreshToken();
				vPane.clear();
				visRB = new Label("Vis råvarebatch");
				visRB.setStyleName("FlexTable-Header");
				vPane.add(visRB);
				ft.setText(0, 0, "RB ID");
				ft.setText(0, 1, "Raavare ID");
				ft.setText(0, 2, "Mængde");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "50px");
				ft.getFlexCellFormatter().setWidth(0, 1, "85px");
				ft.getFlexCellFormatter().setWidth(0, 2, "80px");

				for (int i = 0; i < result.size(); i++) {
					ft.setText(i+1, 0, Integer.toString(result.get(i).getRbId()));
					ft.setText(i+1, 1, Integer.toString(result.get(i).getRaavareId()));
					ft.setText(i+1, 2, Double.toString(result.get(i).getMaengde()));	
				}
				vPane.add(ft);
			}
		});
	}
}