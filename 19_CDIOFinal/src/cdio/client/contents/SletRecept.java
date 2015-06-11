package cdio.client.contents;

import cdio.client.ServiceAsync;
import cdio.shared.FieldVerifier;
import cdio.shared.ProduktBatchDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SletRecept extends Composite {

	private String token;
	private ServiceAsync service;
	private VerticalPanel vPane;
	private Label error;
	private FlexTable ft;
	private TextBox id;
	private Label desc;
	private Button btn;

	public SletRecept(String token, final ServiceAsync service){
		this.token = token;
		this.service = service;
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}

	private void run(){
		vPane.clear();

		desc = new Label("Indtast ID for Recept der ønskes slettet");
		id = new TextBox();
		id.addKeyUpHandler(new idCheck());

		btn = new Button("Slet ID");

		ft = new FlexTable();

		ft.setWidget(0, 0, desc);
		ft.setWidget(1, 0, id);
		ft.setWidget(2, 0, btn);

		vPane.add(ft);

		btn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				service.deleteRecept(token, Integer.parseInt(id.getText()), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub

					}

				});

			}

		});
	}

	private class idCheck implements KeyUpHandler{

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox id = (TextBox) event.getSource();
			FieldVerifier.isValidUserId(id.getText());
		}
	}

}


