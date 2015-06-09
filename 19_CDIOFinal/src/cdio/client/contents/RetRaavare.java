package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.FieldVerifier;
import cdio.shared.RaavareDTO;

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

public class RetRaavare  extends Composite {
	
	private Label error;
	private VerticalPanel vPane;
	private ServiceAsync service;
	private String token, uID, uNavn, uLeverandoer;
	private FlexTable ft;
	private int eventRow, openEventRow;
	private boolean idValid, nameValid, leverandoerValid;
	private TextBox oID, oNavn, oLeverandoer;

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
				ft.setText(0, 3, "");
				ft.setText(0, 4, "");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "30px");
				ft.getFlexCellFormatter().setWidth(0, 1, "150px");
				ft.getFlexCellFormatter().setWidth(0, 2, "150px");
				ft.getFlexCellFormatter().setWidth(0, 3, "45px");
				ft.getFlexCellFormatter().setWidth(0, 4, "45px");
				
				for (int i=0; i<result.size(); i++){
					ft.setText(i+1, 0, Integer.toString(result.get(i).getRaavareId()));
					ft.setText(i+1, 1, result.get(i).getRaavareNavn());
					ft.setText(i+1, 2, result.get(i).getLeverandoer());
					
					Button ret = new Button("Ret");
					ret.setStyleName("Button-Ret");
					ret.addClickHandler(new RetClick());
					ft.setWidget(i+1, 3, ret);
				}
				
				vPane.add(ft);
			}
			
		});
	}
	
	private class RetClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			if (openEventRow != 0){
				((Button) ft.getWidget(openEventRow, 10)).click();
			}
			eventRow = ft.getCellForEvent(event).getRowIndex();
			openEventRow = eventRow;
			
			uID = ft.getText(eventRow, 0);
			uNavn = ft.getText(eventRow, 1);
			uLeverandoer = ft.getText(eventRow, 2);
			
			idValid = FieldVerifier.isValidUserId(uID);
			nameValid = FieldVerifier.isValidName(uNavn);
			leverandoerValid = true; //Nødvendigt
			
			//Her laves nye widgets der kan redigeres i og erstatter de oprindelige med disse
			oID = new TextBox();
			oID.setText(uID);
			oID.addKeyUpHandler(new IDCheck());
			oID.setStyleName("TextBox-Ret");
			ft.setWidget(eventRow, 0, oID);
			
			oNavn = new TextBox();
			oNavn.setText(uNavn);
		}
		
	}
	
	private class IDCheck implements KeyUpHandler{
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			
		}
	}


}
