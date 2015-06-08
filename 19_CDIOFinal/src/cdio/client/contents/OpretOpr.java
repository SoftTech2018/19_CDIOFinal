package cdio.client.contents;

import cdio.client.ServiceAsync;
import cdio.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpretOpr extends Composite {
	
	private String token;
	private ServiceAsync service;
	private VerticalPanel vPane;
	private Label error;
	private FlexTable ft;
	private TextBox id, navn, ini, cpr, pass;
	private CheckBox admin, farm, vaerk, opr;
	private Button ok;
	
	public OpretOpr(String token, ServiceAsync service){
		this.token = token;
		this.service = service;
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}
	
	private void run(){
		vPane.clear();
		ft = new FlexTable();
		ft.setStyleName("FlexTable-Content");
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Opret Operatør");
		
		ft.setText(1, 0, "Bruger ID:");
		id = new TextBox();
		id.setStyleName("TextBox");
		ft.setWidget(1, 1, id);
		
		ft.setText(2, 0, "Navn:");
		navn = new TextBox();
		navn.setStyleName("TextBox");
		ft.setWidget(2, 1, navn);
		
		ft.setText(3, 0, "Initialer:");
		ini = new TextBox();
		ini.setStyleName("TextBox");
		ft.setWidget(1, 1, ini);
		
		ft.setText(4, 0, "Cpr nr:");
		cpr = new TextBox();
		cpr.setStyleName("TextBox");
		ft.setWidget(4, 1, cpr);
		
		ft.setText(5, 0, "Password:");
		pass = new TextBox();
		pass.setStyleName("TextBox");
		ft.setWidget(5, 1, pass);
		
		ft.setText(6, 0, "Admin:");
		admin = new CheckBox();
		ft.setWidget(6, 1, admin);
		
		ft.setText(7, 0, "Farmaceut:");
		farm = new CheckBox();
		ft.setWidget(7, 1, farm);
		
		ft.setText(8, 0, "Værkfører:");
		vaerk = new CheckBox();
		ft.setWidget(8, 1, vaerk);
		
		ft.setText(9, 0, "Operatør:");
		opr = new CheckBox();
		ft.setWidget(9, 1, opr);
		
		ok = new Button("Opret");
		ok.addClickHandler(new OpretClick());
		ft.setWidget(10, 1, ok);
		
		vPane.add(ft);
		vPane.add(error);
	}
	
	private class OpretClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			ok.setEnabled(false);
			UserDTO user = new UserDTO(id.getText(),
					navn.getText(),
					ini.getText(),
					cpr.getText(),
					pass.getText(),
					admin.getValue(),
					farm.getValue(),
					vaerk.getValue(),
					opr.getValue());
			
			service.createUser(token, user, new AsyncCallback<UserDTO>(){

				@Override
				public void onFailure(Throwable caught) {
					ok.setEnabled(true);
					error.setText(caught.getMessage());
					error.setStyleName("TextBox-ErrorMessage");
				}

				@Override
				public void onSuccess(UserDTO result) {
					Window.alert("Bruger " + result.getName() + " blev oprettet.");
					run();
				}
			});
		}
	}

}
