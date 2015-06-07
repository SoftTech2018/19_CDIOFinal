package cdio.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Controller extends Composite {

	private String token;
	private ServiceAsync service;
	private VerticalPanel vPane;
	private Menu menu;
//	private Content content;
	private Header header;
	private Footer footer;
	private Controller con;

	public Controller(String token, final ServiceAsync service) {
		this.token = token;
		this.service = service;
		vPane = new VerticalPanel();
		initWidget(vPane);
		con = this;
		
		// Lav de indledende elementer
		header = new Header();
		footer = new Footer();
		menu = new Menu(con);
		
		// Tilføj basis-elementerne til controlleren
		vPane.add(header);
		vPane.add(menu);
//		vPane.add(content);
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
	
//	public void setContent(Content content){
//		this.content = content;
//	}
	
	public void setMenu(Menu menu){
		this.menu = menu;
	}
	
	public void setHeader(Header header){
		this.header = header;
	}
	
	public void setFooter(Footer footer){
		this.footer = footer;
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

}
