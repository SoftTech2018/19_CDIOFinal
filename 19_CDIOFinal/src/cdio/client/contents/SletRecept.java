package cdio.client.contents;

import cdio.client.Controller;
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

public class SletRecept extends Composite {

	private VerticalPanel vPane;
	private Label error, sletRecept;
	private FlexTable ft;
	private TextBox id;
	private Label desc;
	private Button btn;

	public SletRecept(){
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	private void run(){
		vPane.clear();
		sletRecept = new Label("Slet recept");
		sletRecept.setStyleName("FlexTable-Header");
		vPane.add(sletRecept);
		desc = new Label("Indtast ID for Recept der ønskes slettet");
		id = new TextBox();
		id.setStyleName("TextBox-Opret");
		id.addKeyUpHandler(new idCheck());

		btn = new Button("Slet Recept");
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
				Controller.service.checkReceptID(Controller.token, Integer.parseInt(id.getText()), new AsyncCallback<Void>(){

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
						Window.alert("Recepten blev slettet");
						id.setText("");
						btn.setText("Slet Recept");
						Controller.refreshToken();
						btn.setEnabled(true);
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