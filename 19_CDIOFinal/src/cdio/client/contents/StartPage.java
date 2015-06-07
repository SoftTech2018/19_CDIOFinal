package cdio.client.contents;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StartPage extends Composite {
	
	public StartPage(){
		VerticalPanel vPane = new VerticalPanel();
		initWidget(vPane);
		Label test = new Label("Start page test Start page test Start page test Start page test Start page test Start page test"
				+ "Start page test Start page test Start page test Start page test Start page test Start page test Start page test" 
				+ "Start page test Start page test Start page test");
		vPane.add(test);
		
		
	}

}
