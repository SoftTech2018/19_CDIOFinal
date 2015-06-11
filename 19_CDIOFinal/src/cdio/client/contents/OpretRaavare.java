package cdio.client.contents;

import cdio.client.ServiceAsync;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpretRaavare extends Composite {
	
	private ServiceAsync service;
	private String token;
	private VerticalPanel vPane;

	public OpretRaavare(String token, ServiceAsync service) {
		this.service=service;
		this.token=token;
		initWidget(vPane);
		run();
	}
	
	public void run(){
		
		
	}

}
