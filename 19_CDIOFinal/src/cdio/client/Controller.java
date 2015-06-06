package cdio.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Controller extends Composite {

	private String token;
	private ServiceAsync service;
	private VerticalPanel vPane;
	private Composite content, header, menu, footer;
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
			}

			@Override
			public void onSuccess(String result) {
				new Menu(con, result);
			}
		});
	}
	
	public void setContent(Composite content){
		this.content = content;
	}
	
	public void setMenu(Composite menu){
		this.menu = menu;
	}
	
	public void setHeader(Composite header){
		this.header = header;
	}
	
	public void setFooter(Composite footer){
		this.footer = footer;
	}
	
	public String getToken(){
		return token;
	}
	
	public ServiceAsync getService(){
		return service;
	}

}
