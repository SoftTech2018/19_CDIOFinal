package cdio.client.contents;

import cdio.client.ServiceAsync;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpretRB extends Composite {
	
	private VerticalPanel vPane;
	private FlexTable ft;
	private TextBox rbID, raavareID, maengde;

	public OpretRB() {
		vPane = new VerticalPanel();
		initWidget(vPane);
		run();
	}
	
	public void run(){
		
		ft = new FlexTable();
		ft.setStyleName("FlexTable-Content");
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Opret Råvare Batch");
		
		ft.setText(1, 0, "Opret Råvare Batch");
		
		
	}

}
