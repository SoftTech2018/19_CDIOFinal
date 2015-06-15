package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.ServiceAsync;
import cdio.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisOpr extends Composite {

	private VerticalPanel vPane;
	private Label error;

	public VisOpr() {
		vPane = new VerticalPanel();
		error = new Label("Loading...");
		vPane.add(error);
		initWidget(vPane);


		Controller.service.getOprList(Controller.token, new AsyncCallback<List<UserDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<UserDTO> result) { //Formatering af cellerne når man viser brugere i systemet
				Controller.refreshToken();
				vPane.clear();
				FlexTable ft = new FlexTable();
				vPane.add(ft);
				ft.setStyleName("FlexTable-Content");
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
				ft.getFlexCellFormatter().setWidth(0, 0, "30px");
				ft.getFlexCellFormatter().setWidth(0, 1, "150px");
				ft.getFlexCellFormatter().setWidth(0, 2, "55px");
				ft.getFlexCellFormatter().setWidth(0, 3, "85px");
				ft.getFlexCellFormatter().setWidth(0, 4, "85px");
				ft.getFlexCellFormatter().setWidth(0, 5, "55px");
				ft.getFlexCellFormatter().setWidth(0, 6, "75px");
				ft.getFlexCellFormatter().setWidth(0, 7, "75px");
				ft.getFlexCellFormatter().setWidth(0, 8, "75px");
				ft.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_CENTER);
				ft.getCellFormatter().setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_CENTER);
				ft.getCellFormatter().setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_CENTER);
				ft.getCellFormatter().setHorizontalAlignment(0, 7, HasHorizontalAlignment.ALIGN_CENTER);

				for (int i = 0; i < result.size(); i++) {
					ft.getCellFormatter().setHorizontalAlignment(i+1, 4, HasHorizontalAlignment.ALIGN_CENTER);
					ft.getCellFormatter().setHorizontalAlignment(i+1, 5, HasHorizontalAlignment.ALIGN_CENTER);
					ft.getCellFormatter().setHorizontalAlignment(i+1, 6, HasHorizontalAlignment.ALIGN_CENTER);
					ft.getCellFormatter().setHorizontalAlignment(i+1, 7, HasHorizontalAlignment.ALIGN_CENTER);
//					vPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
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


				}
				Controller.service.getUserCount(Controller.token, new AsyncCallback<List<Integer>>() {

					@Override
					public void onFailure(Throwable caught) {
						FlexTable ft2 = new FlexTable();
						ft2.setText(0, 0, caught.getMessage());
						vPane.add(ft2);
						

					}

					@Override
					public void onSuccess(List<Integer> result) {
						FlexTable ft3 = new FlexTable();
						FlexTable ft2 = new FlexTable();
						HTML gap = new HTML();
						gap.setHTML("<br><br>");
						
						vPane.add(gap);
						vPane.add(ft3);
						vPane.add(ft2);
						
//						vPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
						
						ft3.getCellFormatter().addStyleName(0, 0, "FlexTable-Header");
						ft3.setText(0, 0, "Antal brugere i systemet: ");
						ft2.setText(1, 0, "Admins: ");
						ft2.setText(2, 0, "Farmaceuter: ");
						ft2.setText(3, 0, "Værkførere: ");
						ft2.setText(4, 0, "Operatørere: ");

						
//							ft2.getCellFormatter().setHorizontalAlignment(1, 4, HasHorizontalAlignment.ALIGN_CENTER);
//							ft2.getCellFormatter().setHorizontalAlignment(1, 5, HasHorizontalAlignment.ALIGN_CENTER);
//							ft2.getCellFormatter().setHorizontalAlignment(1, 6, HasHorizontalAlignment.ALIGN_CENTER);
//							ft2.getCellFormatter().setHorizontalAlignment(1, 7, HasHorizontalAlignment.ALIGN_CENTER);
							
							ft2.getCellFormatter().setWidth(1, 0, "80px");
							ft2.getCellFormatter().setWidth(1, 1, "25px");
							ft2.setText(1, 1, Integer.toString(result.get(0))); //Admins
							ft2.setText(2, 1, Integer.toString(result.get(1))); //Farmaceuter
							ft2.setText(3, 1, Integer.toString(result.get(2)));	//Værkførere
							ft2.setText(4, 1, Integer.toString(result.get(3))); //Operatører

						

					}

				});
			}
		});



	}



}





