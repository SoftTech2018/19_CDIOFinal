package cdio.client.contents;

import java.util.List;

import cdio.client.Controller;
import cdio.client.ServiceAsync;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisPB extends Composite {

	private VerticalPanel vPane;
	private FlexTable ft;
	//	private ToggleButton pbkomp;
	private Button pbkomp, pbkompnew, skjul, print, tilbage;
	private Label error;


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
				ft.setText(0, 0, caught.getMessage());
			}

			@Override
			public void onSuccess(List<ProduktBatchDTO> result) {
				Controller.refreshToken();
				vPane.clear();
				vPane.add(ft);
				ft.setText(0, 0, "PB ID");
				ft.setText(0, 1, "Recept ID");
				ft.setText(0, 2, "Status");
				ft.setText(0, 3, "Påbegyndt");
				ft.setText(0, 4, "Afsluttet");

				ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
				ft.getFlexCellFormatter().setWidth(0, 0, "50px");
				ft.getFlexCellFormatter().setWidth(0, 1, "75px");
				ft.getFlexCellFormatter().setWidth(0, 2, "55px");
				ft.getFlexCellFormatter().setWidth(0, 3, "130px");
				ft.getFlexCellFormatter().setWidth(0, 4, "130px");



				for (int i = 0; i < result.size(); i++) {
					ft.setText(i+1, 0, Integer.toString(result.get(i).getPbId()));
					ft.setText(i+1, 1, Integer.toString(result.get(i).getReceptId()));
					ft.setText(i+1, 2, Integer.toString(result.get(i).getStatus()));
					ft.setText(i+1, 3, result.get(i).getBegyndt());
					ft.setText(i+1, 4, result.get(i).getAfsluttet());
					
					ft.getCellFormatter().setVerticalAlignment(i+1, 0, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 1, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 2, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 3, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 4, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 5, HasVerticalAlignment.ALIGN_TOP);
					ft.getCellFormatter().setVerticalAlignment(i+1, 6, HasVerticalAlignment.ALIGN_TOP);
					
					print = new Button("Udprint");
					print.setStyleName("Button-Ret"); 
					print.addClickHandler(new Udprint());
					ft.setWidget(i+1, 5, print);

					pbkomp = new Button("Komponenter");
					pbkomp.setStyleName("Button-Komponenter"); 
					pbkomp.addClickHandler(new KompClick());
					ft.setWidget(i+1, 6, pbkomp);

				}

			}

		});
	}

	private class KompClick implements ClickHandler {

		int eventRow;


		@Override
		public void onClick(ClickEvent event) {
			
			eventRow = ft.getCellForEvent(event).getRowIndex();
			((Button) ft.getWidget(eventRow, 6)).setText("Loading...");
			((Button) ft.getWidget(eventRow, 6)).setEnabled(false);
			//			ft.setWidget(eventRow, 3, pbkompnew);

			Controller.service.getPBKList(Controller.token, Integer.parseInt(ft.getText(eventRow, 0)),  new AsyncCallback<List<ProduktBatchKompDTO>>(){

				@Override
				public void onFailure(Throwable caught) {

					ft.setText(0, 0, caught.getMessage());

				}

				@Override
				public void onSuccess(List<ProduktBatchKompDTO> result) {
					FlexTable ft2 = new FlexTable();
					skjul = new Button("Skjul");
					skjul.setStyleName("Button-Ret");
					skjul.addClickHandler(new SkjulClick());
					ft2.setWidget(0, 0, skjul); //Her sættes Skjul-knappen i flextable 2

					//ft2.setWidget(0, 0, ft.getWidget(eventRow, 3)); //
					ft.setWidget(eventRow, 6, ft2);//her sættes ft2 i den korrekte referede række

					ft2.setText(0, 1, "RB ID");
					ft2.setText(0, 2, "Tara");
					ft2.setText(0, 3, "Netto");
					ft2.setText(0, 4, "Opr ID");
					ft2.setText(0, 5, "Terminal");

					ft2.getRowFormatter().setStyleName(0, "FlexTable-Header");
					ft2.getFlexCellFormatter().setWidth(0, 0, "90px");
					ft2.getFlexCellFormatter().setWidth(0, 1, "100px");
					ft2.getFlexCellFormatter().setWidth(0, 2, "50px");
					ft2.getFlexCellFormatter().setWidth(0, 3, "50px");
					ft2.getFlexCellFormatter().setWidth(0, 4, "100px");
					ft2.getFlexCellFormatter().setWidth(0, 5, "100px");

					for (int i = 0; i < result.size(); i++) {
						ft2.setText(i+2, 1, Integer.toString(result.get(i).getRbId()));
						ft2.setText(i+2, 2, Double.toString(result.get(i).getTara()));
						ft2.setText(i+2, 3, Double.toString(result.get(i).getNetto()));
						ft2.setText(i+2, 4, Integer.toString(result.get(i).getOprId()));
						ft2.setText(i+2, 5, result.get(i).getHost());
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

			ft.setWidget(eventRow, 6, pbkompnew);

		}

	}

	private class Udprint implements ClickHandler {
		int eventRow;


		@Override
		public void onClick(ClickEvent event) {
			eventRow = ft.getCellForEvent(event).getRowIndex();
//			System.out.println("test1");

			ProduktBatchDTO pb = new ProduktBatchDTO();
			pb.setPbId(Integer.parseInt( ft.getText(eventRow, 0)));
			pb.setReceptId(Integer.parseInt( ft.getText(eventRow, 1)));
			pb.setDato(ft.getText(eventRow, 3));

			tilbage = new Button("Tilbage");
			tilbage.setStyleName("Button-Ret");
			tilbage.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
//					tilbage.setText("Loading...");
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
