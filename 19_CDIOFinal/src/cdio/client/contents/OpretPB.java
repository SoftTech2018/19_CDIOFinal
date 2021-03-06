package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ReceptDTO;
import cdio.shared.TokenException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpretPB extends Composite {

	private VerticalPanel vPane;
	private Label error;
	private FlexTable ft;
	private Button ok;
	private ListBox receptNr;

	public OpretPB(){
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

		ft.setText(0, 0, "Opret produktbatch");	
		ft.setText(1, 0, "Receptnummer:");
		receptNr = new ListBox();
		receptNr.setVisibleItemCount(1); // Laver det til en dropdown-menu
		ft.setWidget(1, 1, receptNr);
		Controller.service.getReceptList(Controller.token, new AsyncCallback<List<ReceptDTO>>(){

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
				} else {
					error.setText(caught.getMessage());
					error.setStyleName("TextLabel-ErrorMessage");
				}
			}

			@Override
			public void onSuccess(List<ReceptDTO> result) {
				receptNr.addItem("Vælg recept");
				for (int i=0; i<result.size(); i++){
					ReceptDTO r = result.get(i);
					receptNr.addItem(r.getReceptId() + " : " + r.getReceptNavn());
				}
				Controller.refreshToken();
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
			ProduktBatchDTO pb = new ProduktBatchDTO();
			pb.setPbId(0);
			int receptId = -1;
			String recept = receptNr.getItemText(receptNr.getSelectedIndex());
			for (int i=0; i<recept.length(); i++){
				if (recept.charAt(i) == ':')
					receptId = Integer.parseInt(recept.substring(0, i-1));
			}

			if (receptId == -1){
				error.setText("Du skal vælge et recept id.");
				error.setStyleName("TextLabel-ErrorMessage");
			} else {
				ok.setEnabled(false);
				ok.setText("Loading");
				pb.setReceptId(receptId);
				pb.setStatus(0);
				Controller.service.createPB(Controller.token, pb, new AsyncCallback<ProduktBatchDTO>(){

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
						} else {
							error.setText(caught.getMessage());
							error.setStyleName("TextLabel-ErrorMessage");												
						}
					}

					@Override
					public void onSuccess(ProduktBatchDTO result) {
						vPane.clear();
						vPane.add(new PrintPB(result));
						Controller.refreshToken();
					}
				});
			}
		}
	}
}
