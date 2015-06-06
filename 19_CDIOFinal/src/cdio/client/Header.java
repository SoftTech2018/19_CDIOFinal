package cdio.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Header extends Composite {
	
	public Header(){
		VerticalPanel vPane = new VerticalPanel();
		initWidget(vPane);
		Label header = new Label("Distribueret Afvejningssystem");
		vPane.add(header);
		
		// Style elementet som header
		header.setStyleName("Header-Text");
		vPane.setStyleName("Header");
	}

}
