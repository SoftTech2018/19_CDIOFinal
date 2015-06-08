package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RetOpr extends Composite {
	
	private Label error;
	private VerticalPanel vPane;
	private int eventRow;
	private FlexTable ft;
	
	public RetOpr(String token, final ServiceAsync service){
		vPane = new VerticalPanel();
		initWidget(vPane);
		
		error = new Label("Loading...");
		vPane.add(error);
		service.getOprList(token, new AsyncCallback<List<UserDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				error.setText(caught.getMessage());				
			}

			@Override
			public void onSuccess(List<UserDTO> result) {
				ft = new FlexTable();
				ft.setStyleName("FlexTable-Content");
				ft.setText(0, 0, "ID");
				ft.setText(0, 1, "Navn");
				ft.setText(0, 2, "Initialer");
				ft.setText(0, 3, "Cpr nr");
				ft.setText(0, 4, "Password");
				ft.setText(0, 5, "Admin");
				ft.setText(0, 6, "Farmaceut");
				ft.setText(0, 7, "Værkfører");
				ft.setText(0, 8, "Operatør");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				
				for (int i=0; i<result.size(); i++){
					ft.setText(i+1, 0, Integer.toString(result.get(i).getUserId()));
					ft.setText(i+1, 1, result.get(i).getName());
					ft.setText(i+1, 2, result.get(i).getIni());
					ft.setText(i+1, 3, result.get(i).getCpr());
					ft.setText(i+1, 4, result.get(i).getPassword());
					CheckBox adm = new CheckBox();
					adm.setValue(result.get(i).isAdmin());
					adm.setEnabled(false);
					ft.setWidget(i+1, 5, adm);
					CheckBox farm = new CheckBox();
					farm.setValue(result.get(i).isFarmaceut());
					farm.setEnabled(false);
					ft.setWidget(i+1, 6, farm);
					CheckBox vaerk = new CheckBox();
					vaerk.setValue(result.get(i).isVaerkfoerer());
					vaerk.setEnabled(false);
					ft.setWidget(i+1, 7, vaerk);
					CheckBox opr = new CheckBox();
					opr.setValue(result.get(i).isOperatoer());
					opr.setEnabled(false);
					ft.setWidget(i+1, 8, opr);
					
					Anchor ret = new Anchor("Ret");
					ret.addClickHandler(new RetClick());
					ft.setWidget(i+1, 9, ret);
				}
				
			}
			
		});
		
		
	}

	private class RetClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			eventRow = ft.getCellForEvent(event).getRowIndex();
			TextBox navn = new TextBox();
			navn.setText(ft.getText(eventRow, 1));
			ft.setWidget(eventRow, 1, navn);
			
			TextBox ini = new TextBox();
			ini.setText(ft.getText(eventRow, 2));
			ft.setWidget(eventRow, 2, ini);
			
			TextBox cpr = new TextBox();
			cpr.setText(ft.getText(eventRow, 3));
			ft.setWidget(eventRow, 3, cpr);
			
			TextBox password = new TextBox();
			password.setText(ft.getText(eventRow, 4));
			ft.setWidget(eventRow, 4, password);
			
			CheckBox adm = new CheckBox();
			adm.setEnabled(true);
			adm.setValue(((CheckBox) ft.getWidget(eventRow, 5)).getValue());
			ft.setWidget(eventRow, 5, adm);
			
			CheckBox farm = new CheckBox();
			farm.setEnabled(true);
			farm.setValue(((CheckBox) ft.getWidget(eventRow, 6)).getValue());
			ft.setWidget(eventRow, 6, farm);
			
			CheckBox vaerk = new CheckBox();
			vaerk.setEnabled(true);
			vaerk.setValue(((CheckBox) ft.getWidget(eventRow, 7)).getValue());
			ft.setWidget(eventRow, 7, vaerk);
			
			CheckBox opr = new CheckBox();
			opr.setEnabled(true);
			opr.setValue(((CheckBox) ft.getWidget(eventRow, 8)).getValue());
			ft.setWidget(eventRow, 8, opr);
			
			Anchor ok = new Anchor("ok");
			ft.setWidget(eventRow, 9, ok);
			
			Anchor cancel = new Anchor("cancel");
			ft.setWidget(eventRow, 10, cancel);
		}
	}
}
