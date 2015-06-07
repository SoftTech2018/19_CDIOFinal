package cdio.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Menu extends Composite {
	
	private VerticalPanel vPane;
	private Controller con;

	// Tom menu
	public Menu(Controller controller) {
		this.con = controller;
		vPane = new VerticalPanel();
		initWidget(vPane);
		
		Label header = new Label("Menu");
		vPane.add(header);
		
		//Style elementet som Menu
		vPane.setStyleName("Menu");
	}
	
	public void changeMenu(String role){
		vPane.clear(); // Fjerner den gamle menu
		Label error;
		FlexTable ftMenu = new FlexTable();
		ftMenu.setStyleName("FlexTable-Menu");
		int i = 0;
		
		switch(role.toUpperCase()){
		case "ADMIN":			
			ftMenu.setWidget(i, 0, adminMenu());
			i++;
		case "FARMACEUT":			
			ftMenu.setWidget(i, 0, farmMenu());
			i++;
		case "VAERKFOERER":	
			ftMenu.setWidget(i, 0, vaerkMenu());
			i++;
			break;
		default:
			error = new Label("FEJL. Din rolle kunne ikke genkendes. Kontakt admin.");
			error.setStyleName("TextLabel-ErrorMessage");
			vPane.add(error);
		}
		
		// Tilføj log-ud knap
		Button logud = new Button("Log ud");
		logud.addClickHandler(new Logud()); // Clickhandler
		ftMenu.setWidget(i, 0, logud);
		ftMenu.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		vPane.add(ftMenu);
	}
	
	// Bygger og returnerer Admin menu
	private FlexTable adminMenu(){
		FlexTable ftAdm = new FlexTable();
		ftAdm.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ftAdm.setText(0, 0, "Admin");
		return ftAdm;
	}
	
	// Bygger og returnerer Farmaceut menu
	private FlexTable farmMenu(){
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Farmaceut");
		return ft;
	}
	
	// Bygger og returnerer Værkfører menu
	private FlexTable vaerkMenu(){
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Værkfører");
		return ft;
	}
	
	// Clickhandler til logud-knappen
	private class Logud implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			con.logud();
		}
		
	}
}
