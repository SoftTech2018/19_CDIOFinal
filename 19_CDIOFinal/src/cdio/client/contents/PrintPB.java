package cdio.client.contents;

import java.util.List;

import cdio.client.ServiceAsync;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PrintPB extends Composite{

	private VerticalPanel pbvPane;
	private Label error;
	private FlexTable ft;

	/**
	 * Viser en produktbatch som kan printes
	 * @param pb Den pågældende produktbatch - skal indeholde pb_id og recept_id
	 * @param token 
	 * @param service
	 */
	public PrintPB(final ProduktBatchDTO pb, String token, ServiceAsync service){
		pbvPane = new VerticalPanel();
		initWidget(pbvPane);
		ft = new FlexTable();
		error = new Label("Loading...");;
		pbvPane.add(ft);
		pbvPane.add(error);

		ft.setStyleName("FlexTable-PB"); // Dashed border
		
		// Hvilken produktbatch
		FlexTable ft2 = new FlexTable();
		ft2.setText(0, 0, "Udskrevet");
		ft2.setText(0, 1, pb.getDato());

		ft2.setText(1, 0, "Produkt Batch nr.");
		ft2.setText(1, 1, Integer.toString(pb.getPbId()));

		ft2.setText(2, 0, "Recept nr.");
		ft2.setText(2, 1, Integer.toString(pb.getReceptId()));
		ft2.setText(3, 0, "");
		ft.setWidget(0, 0, ft2);
		
		ft.setText(1, 0, "");

		service.getPbView(token, pb.getPbId(), new AsyncCallback<List<PbViewDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				error.setText(caught.getMessage());
				error.setStyleName("TextLabel-ErrorMessage");
			}

			@Override
			public void onSuccess(List<PbViewDTO> result) {
				error.setText("");
				int row = 2; 
				for (int i=0; i<result.size(); i++){
					PbViewDTO p = result.get(i);
					FlexTable ft3 = new FlexTable();
					ft3.setText(0, 0, "Råvare nr.");
					ft3.setText(0, 1, Integer.toString(p.getRaavareId()));
					ft3.setText(1, 0, "Råvare navn:");
					ft3.setText(1, 1, p.getRaavareNavn());
					ft.setWidget(row, 0, ft3);
					row++;
					
					HTML line = new HTML();
					line.setHTML("<hr style=\"border: 1px dashed black;\" width=\"100%\">");
					ft.setWidget(row, 0, line);
					row++;
					
					FlexTable ft4 = new FlexTable();
					ft4.setStyleName("FlexTable-Content");
					ft4.getColumnFormatter().setWidth(0, "75px");
					ft4.getColumnFormatter().setWidth(1, "75px");
					ft4.getColumnFormatter().setWidth(2, "75px");
					ft4.getColumnFormatter().setWidth(3, "75px");
					ft4.getColumnFormatter().setWidth(4, "75px");
					ft4.getColumnFormatter().setWidth(5, "75px");
					ft4.getColumnFormatter().setWidth(6, "75px");
					ft4.getColumnFormatter().setWidth(7, "75px");
					
					ft4.setText(0, 0, "Del");
					ft4.setText(0, 1, "Mængde");
					ft4.setText(0, 2, "Tolerance");
					ft4.setText(0, 3, "Tara");
					ft4.setText(0, 4, "Netto (kg)");
					ft4.setText(0, 5, "Batch");
					ft4.setText(0, 6, "Opr.");
					ft4.setText(0, 7, "Terminal");

					ft4.setText(1, 0, "1");
					ft4.setText(1, 1, Double.toString(p.getMaengde()).replace(".", ","));
					ft4.setText(1, 2, Double.toString(p.getTolerance())+ "%");
					ft4.setText(1, 3, Double.toString(p.getTara()).replace(".", ","));
					ft4.setText(1, 4, Double.toString(p.getNetto()).replace(".", ","));
					ft4.setText(1, 5, Integer.toString(p.getBatch()));
					ft4.setText(1, 6, p.getOpr());
					ft4.setText(1, 7, p.getTerminal());
					ft.setWidget(row, 0, ft4);;
					row++;
					
					ft.setText(row, 0, "");
					row++;
					ft.setText(row, 0, "");
					row++;
				}
				
				ft.setText(row, 0, "");
				row++;	
				ft.setText(row, 0, "");
				row++;
				
				double taraSum = 0;
				double nettoSum = 0;
				for (int i=0; i<result.size(); i++){
					taraSum = taraSum + result.get(i).getTara();
					nettoSum = nettoSum + result.get(i).getNetto();
				}
				FlexTable ft5 = new FlexTable();
				ft5.getColumnFormatter().setWidth(0, "75px");
				ft5.getColumnFormatter().setWidth(1, "75px");
				ft5.getColumnFormatter().setWidth(2, "75px");
				ft5.getColumnFormatter().setWidth(3, "75px");
				ft5.getColumnFormatter().setWidth(4, "75px");
				ft5.getColumnFormatter().setWidth(5, "75px");
				ft5.getColumnFormatter().setWidth(6, "75px");
				ft5.getColumnFormatter().setWidth(7, "75px");
				
				ft5.setText(0, 0, "Sum Tara:");
				ft5.setText(0, 3, Double.toString(taraSum).replace(".", ","));
				ft5.setText(1, 0, "Sum Netto:");
				ft5.setText(1, 4, Double.toString(nettoSum).replace(".", ","));
				ft.setWidget(row, 0, ft5);
				row++;
				
				ft.setText(row, 0, "");
				row++;
				ft.setText(row, 0, "");
				row++;
				
				String status;
				switch(pb.getStatus()){
				case 0: 
					status = "Oprettet";
					break;
				case 1:
					status = "Under produktion";
					break;
				case 2: 
					status = "Afsluttet";
					break;
				default:
					status = "Fejl i status";
				}
				
				FlexTable ft6 = new FlexTable();
				ft6.setText(0,  0, "Produktion Status:");
				ft6.setText(0, 1, status);
				ft6.setText(1, 0, "Produktion Startet:");
				ft6.setText(2, 0, "Produktion Slut:");
				ft.setWidget(row, 0, ft6);
				row++;
			}
		});
	}
}