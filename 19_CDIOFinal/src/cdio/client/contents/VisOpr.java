package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisOpr extends Composite {
	
	private VerticalPanel vPane;
	
	public VisOpr(String token, final ServiceAsync service) {
		vPane = new VerticalPanel();
		initWidget(vPane);
		
		
		
		service.getOprList(token, new AsyncCallback<List<UserDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<UserDTO> result) {
				FlexTable ft = new FlexTable();
				ft.setText(0, 0, "ID");
				ft.setText(0, 1, "Navn");
				ft.setText(0, 2, "Initialer");
				ft.setText(0, 3, "Cpr nr");
				//ft.setText(0, 5, "Password");
				ft.setText(0, 4, "Admin");
				ft.setText(0, 5, "Farmaceut");
				ft.setText(0, 6, "Værkfører");
				ft.setText(0, 7, "Operatør");
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "25px");
				ft.getFlexCellFormatter().setWidth(0, 1, "175px");
				ft.getFlexCellFormatter().setWidth(0, 2, "50px");
				ft.getFlexCellFormatter().setWidth(0, 3, "100px");
				ft.getFlexCellFormatter().setWidth(0, 4, "50px");
				ft.getFlexCellFormatter().setWidth(0, 5, "100px");
				ft.getFlexCellFormatter().setWidth(0, 6, "100px");
				ft.getFlexCellFormatter().setWidth(0, 7, "100px");
				
				for (int i = 0; i < result.size(); i++) {
					ft.setText(i+1, 0, Integer.toString(result.get(i).getUserId()));
					ft.setText(i+1, 1, result.get(i).getName());
					ft.setText(i+1, 2, result.get(i).getIni());
					ft.setText(i+1, 3, result.get(i).getCpr());
					
					CheckBox adm = new CheckBox();
					adm.setValue(result.get(i).isAdmin());
					adm.setEnabled(false);
					ft.setWidget(i+1, 4, adm);
					
					CheckBox farm = new CheckBox();
					farm.setValue(result.get(i).isFarmaceut());
					farm.setEnabled(false);
					ft.setWidget(i+1, 5, farm);
					
					CheckBox vaerk = new CheckBox();
					vaerk.setValue(result.get(i).isVaerkfoerer());
					vaerk.setEnabled(false);
					ft.setWidget(i+1, 6, vaerk);
					
					CheckBox opr = new CheckBox();
					opr.setValue(result.get(i).isOperatoer());
					opr.setEnabled(false);
					ft.setWidget(i+1, 7, opr);
					
					vPane.add(ft);
				}
			}
		});
	}
}



