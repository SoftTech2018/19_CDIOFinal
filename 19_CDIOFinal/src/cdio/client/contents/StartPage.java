package cdio.client.contents;

import cdio.client.Controller;
import cdio.client.Header;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StartPage extends Composite {
	
	public StartPage(final Controller con){
		VerticalPanel vPane = new VerticalPanel();
		initWidget(vPane);
		
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Vælg farveskema:");
		
		Button blue = new Button("Blå");
		blue.setStyleName("Button-Blue");
		blue.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setColor("blue");
			}
		});
		
		
		Button red = new Button("Rød");
		red.setStyleName("Button-Red");
		red.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setColor("red");
			}
		});
		
		Button green = new Button("Grøn");
		green.setStyleName("Button-Green");
		green.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setColor("green");
			}
		});
		
		ft.setWidget(0, 1, blue);
		ft.setWidget(0, 2, red);
		ft.setWidget(0, 3, green);
		vPane.add(ft);
		
		
	}

}
