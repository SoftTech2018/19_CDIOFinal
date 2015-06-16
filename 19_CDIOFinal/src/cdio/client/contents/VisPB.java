package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.PopupLogin;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.TokenException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisPB extends Composite {

	private VerticalPanel vPane;
	private FlexTable ft;
	private Button pbkomp, pbkompnew, skjul, print, tilbage;
	private Label error, visPB;
	private Button ok;

	public VisPB() {
		vPane = new VerticalPanel();
		error = new Label("Loading...");
		vPane.add(error);
		initWidget(vPane);
		ft = new FlexTable();
		run();
	}

	private void run() {
		Controller.service.getPBList(Controller.token, new AsyncCallback<List<ProduktBatchDTO>>() {

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
					ok.setEnabled(true);
				} else {
					ok.setEnabled(true);
					error.setText(caught.getMessage());
					error.setStyleName("TextBox-ErrorMessage");	
				}
			}

			@Override
			public void onSuccess(List<ProduktBatchDTO> result) {
				Controller.refreshToken();
				vPane.clear();
				visPB = new Label("Vis produktbatch");
				visPB.setStyleName("FlexTable-Header");
				vPane.add(visPB);
				vPane.add(ft);
				ft.setText(0, 0, "PB ID");
				ft.setText(0, 1, "Recept ID");
				ft.setText(0, 2, "Status");
				ft.setText(0, 3, "Oprettet");
				ft.setText(0, 4, "Påbegyndt");
				ft.setText(0, 5, "Afsluttet");

				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "35px");
				ft.getFlexCellFormatter().setWidth(0, 1, "60px");
				ft.getFlexCellFormatter().setWidth(0, 2, "45px");
				ft.getFlexCellFormatter().setWidth(0, 3, "70px");
				ft.getFlexCellFormatter().setWidth(0, 4, "130px");
				ft.getFlexCellFormatter().setWidth(0, 5, "130px");

				for (int i = 0; i < result.size(); i++) {
					ft.setText(i+1, 0, Integer.toString(result.get(i).getPbId()));
					ft.setText(i+1, 1, Integer.toString(result.get(i).getReceptId()));
					ft.setText(i+1, 2, Integer.toString(result.get(i).getStatus()));
					ft.setText(i+1, 3, result.get(i).getDato());
					ft.setText(i+1, 4, result.get(i).getBegyndt());
					ft.setText(i+1, 5, result.get(i).getAfsluttet());

					ft.getCellFormatter().setVerticalAlignment(i+1, 0, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 1, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 2, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 3, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 4, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 5, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 6, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 7, HasVerticalAlignment.ALIGN_TOP);

					print = new Button("Udprint");
					print.setStyleName("Button-Ret"); 
					print.addClickHandler(new Udprint());
					ft.setWidget(i+1, 6, print);

					pbkomp = new Button("Komponenter");
					pbkomp.setStyleName("Button-Komponenter"); 
					pbkomp.addClickHandler(new KompClick());
					ft.setWidget(i+1, 7, pbkomp);
				}
			}
		});
	}

	private class KompClick implements ClickHandler {

		private int eventRow;

		@Override
		public void onClick(ClickEvent event) {

			eventRow = ft.getCellForEvent(event).getRowIndex();
			((Button) ft.getWidget(eventRow, 7)).setText("Loading...");
			((Button) ft.getWidget(eventRow, 7)).setEnabled(false);
			//			ft.setWidget(eventRow, 3, pbkompnew);

			Controller.service.getPBKList(Controller.token, Integer.parseInt(ft.getText(eventRow, 0)),  new AsyncCallback<List<ProduktBatchKompDTO>>(){

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
						ok.setEnabled(true);
					} else {
						ok.setEnabled(true);
						error.setText(caught.getMessage());
						error.setStyleName("TextBox-ErrorMessage");	
					}
				}

				@Override
				public void onSuccess(List<ProduktBatchKompDTO> result) {
					FlexTable ft2 = new FlexTable();
					skjul = new Button("Skjul");
					skjul.setStyleName("Button-Ret");
					skjul.addClickHandler(new SkjulClick());
					ft2.setWidget(0, 0, skjul); //Her sættes Skjul-knappen i flextable 2

					//ft2.setWidget(0, 0, ft.getWidget(eventRow, 3)); //
					ft.setWidget(eventRow, 7, ft2);//her sættes ft2 i den korrekte referede række

					ft2.setText(1, 0, "RB ID");
					ft2.setText(1, 1, "Tara");
					ft2.setText(1, 2, "Netto");
					ft2.setText(1, 3, "Opr ID");
					ft2.setText(1, 4, "Terminal");

					ft2.getRowFormatter().setStyleName(0, "FlexTable-Header");
					ft2.getFlexCellFormatter().setWidth(0, 0, "35px");
					ft2.getFlexCellFormatter().setWidth(0, 1, "35px");
					ft2.getFlexCellFormatter().setWidth(0, 2, "35px");
					ft2.getFlexCellFormatter().setWidth(0, 3, "45");
					ft2.getFlexCellFormatter().setWidth(0, 4, "100px");

					for (int i = 0; i < result.size(); i++) {
						ft2.setText(i+3, 0, Integer.toString(result.get(i).getRbId()));
						ft2.setText(i+3, 1, Double.toString(result.get(i).getTara()));
						ft2.setText(i+3, 2, Double.toString(result.get(i).getNetto()));
						ft2.setText(i+3, 3, Integer.toString(result.get(i).getOprId()));
						ft2.setText(i+3, 4, result.get(i).getHost());
						//						ft2.setText(i+2, 5, result.get(i).getIP); Mangler database implementering
					}
				}
			});
		}
	}

	private class SkjulClick implements ClickHandler {
		int eventRow;

		@Override
		public void onClick(ClickEvent event) {
			eventRow = ft.getCellForEvent(event).getRowIndex();
			pbkompnew = new Button("Komponenter");
			pbkompnew.setStyleName("Button-Komponenter");
			pbkompnew.addClickHandler(new KompClick());

			ft.setWidget(eventRow, 7, pbkompnew);
		}
	}

	private class Udprint implements ClickHandler {
		int eventRow;

		@Override
		public void onClick(ClickEvent event) {
			eventRow = ft.getCellForEvent(event).getRowIndex();

			ProduktBatchDTO pb = new ProduktBatchDTO();
			pb.setPbId(Integer.parseInt( ft.getText(eventRow, 0)));
			pb.setReceptId(Integer.parseInt( ft.getText(eventRow, 1)));
			pb.setDato(ft.getText(eventRow, 3));
			pb.setBegyndt(ft.getText(eventRow, 4));
			pb.setAfsluttet(ft.getText(eventRow, 5));

			tilbage = new Button("Tilbage");
			tilbage.setStyleName("Button-Ret");
			tilbage.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					tilbage.setEnabled(false);
					run();
				}
			});
			
			vPane.clear();
			vPane.add(tilbage);
			HTML gap = new HTML();
			gap.setHTML("<br><br>");
			vPane.add(gap);
			vPane.add(new PrintPB(pb));
		}
	}
}