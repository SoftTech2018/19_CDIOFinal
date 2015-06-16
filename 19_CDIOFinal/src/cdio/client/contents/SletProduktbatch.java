package cdio.client.contents;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.FieldVerifier;
import cdio.shared.TokenException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SletProduktbatch extends Composite{

	private VerticalPanel vPane;
	private Label error;
	private FlexTable ft;
	private TextBox id;
	private Label desc, sletPB;
	private Button btn;

	public SletProduktbatch(){
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	private void run(){
		vPane.clear();
		sletPB = new Label("Slet produktbatch");
		sletPB.setStyleName("FlexTable-Header");
		vPane.add(sletPB);
		
		desc = new Label("Indtast ID for Produktbatch der ønskes slettet");
		id = new TextBox();
		id.setStyleName("TextBox-Opret");
		id.addKeyUpHandler(new idCheck());
		btn = new Button("Slet Produktbatch");
		btn.setEnabled(false);
		ft = new FlexTable();
		ft.setWidget(0, 0, desc);
		ft.setWidget(1, 0, id);
		ft.setWidget(2, 0, btn);
		vPane.add(ft);

		btn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				btn.setText("Loading");
				btn.setEnabled(false);

				Controller.service.deleteProduktBatch(Controller.token, Integer.parseInt(id.getText()), new AsyncCallback<Void>(){

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
							id.setText("");
							btn.setText("Slet Recept");
							btn.setEnabled(true);
						} else {
							Window.alert(caught.getMessage());
							id.setText("");
							btn.setText("Slet Recept");
							btn.setEnabled(true);
						}
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Produktbatchen blev slettet");
						id.setText("");
						btn.setText("Slet Recept");
						btn.setEnabled(true);
						Controller.refreshToken();
					}
				});
			}
		});
	}

	private class idCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox id = (TextBox) event.getSource();
			if(!FieldVerifier.isValidUserId(id.getText())){
				id.setStyleName("TextBox-OpretError");
				btn.setEnabled(false);
			} else {
				id.setStyleName("TextBox-Opret");
				btn.setEnabled(true);
			}
		}
	}
}