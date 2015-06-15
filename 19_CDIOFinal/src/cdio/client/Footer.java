package cdio.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Footer extends Composite {
	
	public Footer(){
		VerticalPanel vPane = new VerticalPanel();
		initWidget(vPane);
		Label footer = new Label("Copyright © Gruppe 19");
		vPane.add(footer);
		
		//Style elementet som Menu
		footer.setStyleName("Footer-Text");
		vPane.setStyleName("Footer");
	}

}
