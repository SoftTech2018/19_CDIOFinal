package cdio.client;

import cdio.client.contents.StartPage;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Controller extends Composite {

	private String token;
	private ServiceAsync service;
	private VerticalPanel vPane;
	private HorizontalPanel hPane;
	private Menu menu;
	private VerticalPanel content;
	private Header header;
	private Footer footer;

	public Controller(String token, final ServiceAsync service) {
		this.token = token;
		this.service = service;
		vPane = new VerticalPanel();
		initWidget(vPane);
		hPane = new HorizontalPanel();

		// Lav de indledende elementer
		header = new Header();
		footer = new Footer();
		menu = new Menu(this);
		content = new VerticalPanel();
		content.add(new StartPage(this));

		// Sæt style på hver element (bestemmer delvis placering)
		header.setStyleName("Header");
		footer.setStyleName("Footer");
		menu.setStyleName("Menu");
		content.setStyleName("Content");

		// Tilføj basis-elementerne til controlleren
		vPane.add(header);
		hPane.add(menu);
		hPane.add(content);
		vPane.add(hPane);
		vPane.add(footer);

		service.getRole(token, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				logud();
			}

			@Override
			public void onSuccess(String result) {
				menu.changeMenu(result); // Lav en specifik menu baseret på brugerens rolle
			}
		});
	}

	// Fjerner content og erstatter det med den tilsendte widget
	public void setContent(Composite content){
		this.content.clear();
		this.content.add(content);
	}

	public String getToken(){
		return token;
	}

	public ServiceAsync getService(){
		return service;
	}

	// Fjerner alle widgets og laver en ny login-session.
	public void logud(){
		this.token = null;
		vPane.clear();
		RootPanel.get().clear();
		RootPanel.get().add(new Login(service));
	}

	public void setColor(String color) {
		switch(color.toUpperCase()){
		case "BLUE":
			header.setStyleName("Header");
			footer.setStyleName("Footer");
		break;
		case "RED":
			header.setStyleName("Header-Red");
			footer.setStyleName("Footer-Red");
		break;
		case "GREEN":
			header.setStyleName("Header-Green");
			footer.setStyleName("Footer-Green");
		break;
		default: 
			header.setStyleName("Header");
			footer.setStyleName("Footer");
		}
	}

}
