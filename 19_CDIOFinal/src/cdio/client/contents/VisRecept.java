package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;
import cdio.shared.TokenException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisRecept extends Composite {

	private VerticalPanel vPane;
	private Label error, visRecept;
	private Button komp, skjul, kompNew;
	private FlexTable ft, ft2; 
	
	public VisRecept() {
		vPane = new VerticalPanel();
		error = new Label("Loading...");
		vPane.add(error);
		initWidget(vPane);

		Controller.service.getReceptList(Controller.token, new AsyncCallback<List<ReceptDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(List<ReceptDTO> result) {
				Controller.refreshToken();
				error.setText("");
				visRecept = new Label("Vis recept");
				visRecept.setStyleName("FlexTable-Header");
				vPane.add(visRecept);
				
				
				ft = new FlexTable();
				ft.setStyleName("FlexTable-Content");	
				ft.setText(0, 0, "Recept Id");
				ft.setText(0, 1, "Recept navn");
				
				
				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "100px");
				ft.getFlexCellFormatter().setWidth(0, 1, "100px");
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
					ft.getCellFormatter().setVerticalAlignment(i+1, 0, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 1, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 2, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 3, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 4, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 5, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 6, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 7, HasVerticalAlignment.ALIGN_TOP);

					vPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					ft.setText(i+1,0,Integer.toString(result.get(i).getReceptId()));
					ft.setText(i+1, 1, result.get(i).getReceptNavn());
					komp = new Button();
					komp.setText("Komponenter");
					komp.setStyleName("Button-Komponenter");
					komp.addClickHandler(new KompClick());
					ft.setWidget(i+1, 2, komp);
		
				}	
					
					vPane.add(ft);	
					vPane.add(error);
			}
		} );	
	}
	
	private class KompClick implements ClickHandler{
		private int eventRow;
		
		@Override
		public void onClick(ClickEvent event) {
			eventRow = ft.getCellForEvent(event).getRowIndex();
			((Button) ft.getWidget(eventRow, 2)).setText("Loading...");
			((Button) ft.getWidget(eventRow, 2)).setEnabled(false);
				

			Controller.service.getReceptKompListe(Controller.token, Integer.parseInt(ft.getText(eventRow,  0)), new AsyncCallback<List<ReceptKompDTO>>(){
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
						}	else{
							error.setText(caught.getMessage());
							error.setStyleName("TextBox-ErrorMessage");
						}	
					
				}

				@Override
				public void onSuccess(List<ReceptKompDTO> result) {
					Controller.refreshToken();
					ft2 = new FlexTable();
					skjul = new Button("Skjul");
					skjul.setStyleName("Button-Komponenter");
					skjul.addClickHandler(new SkjulClick());
					ft2.setWidget(0, 0, skjul); //Her sættes Skjul-knappen i flextable 2
					ft2.setText(0, 1, "");
					ft2.setText(0, 2, "");
						
					ft.setWidget(eventRow, 2, ft2);
					
					ft2.setText(1, 0, "Råvare ID");
					ft2.setText(1, 1, "Netto");
					ft2.setText(1, 2, "Tolerance");
					
					
					ft2.getRowFormatter().setStyleName(0, "FlexTable-Header");
					ft2.getFlexCellFormatter().setWidth(0, 0, "65px");
					ft2.getFlexCellFormatter().setWidth(0, 1, "65px");
					ft2.getFlexCellFormatter().setWidth(0, 2, "65px");
					ft2.setWidth("250px");

					for(int i = 0; i < result.size(); i++){
						ft2.setText(i+3, 0, Integer.toString(result.get(i).getRaavareId()));
						ft2.setText(i+3, 1, Double.toString(result.get(i).getNomNetto()));
						ft2.setText(i+3, 2, Double.toString(result.get(i).getTolerance()));
			
					
					}
				
				
				}
					
			
				
				
			});
			
		}
		
	}
	
	private class SkjulClick implements ClickHandler{

		int eventRow; 
		
		@Override
		public void onClick(ClickEvent event) {
			eventRow = ft.getCellForEvent(event).getRowIndex();
			kompNew = new Button("Komponenter");
			kompNew.setStyleName("Button-Komponenter");
			kompNew.addClickHandler(new KompClick());

			ft.setWidget(eventRow, 2, kompNew);

			
		}
		
		
	}
	
	
}	