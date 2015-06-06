package cdio.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Menu extends Composite {
	
	private VerticalPanel vPane;

	// Tom menu
	public Menu(Controller controller) {
		vPane = new VerticalPanel();
		initWidget(vPane);
		
		Label header = new Label("Menu");
		vPane.add(header);
		
		//Style elementet som Menu
		vPane.setStyleName("Menu");
	}
	
	public void changeMenu(Controller controller, String role){
		vPane.clear(); // Fjerner den gamle menu
		
		Label error;
		FlexTable ftMenu = new FlexTable();
		vPane.add(ftMenu);
		
		switch(role.toUpperCase()){
		case "ADMIN":
			FlexTable ftAdm = new FlexTable();
			ftAdm.getRowFormatter().setStyleName(0, "FlexTable-Header");
			ftAdm.setText(0, 0, "Admin");
			
			ftMenu.setWidget(0, 0, ftAdm);
		case "FARMACEUT":
			FlexTable ftFar = new FlexTable();
			ftFar.getRowFormatter().setStyleName(0, "FlexTable-Header");
			ftFar.setText(0, 0, "Farmaceut");
			
			ftMenu.setWidget(1, 0, ftFar);
		case "VAERKFOERER":
			FlexTable ftV = new FlexTable();
			ftV.getRowFormatter().setStyleName(0, "FlexTable-Header");
			ftV.setText(0, 0, "Værkfører");
			
			ftMenu.setWidget(2, 0, ftV);
			break;
		default:
			error = new Label("FEJL. Din rolle kunne ikke genkendes. Kontakt admin.");
			error.setStyleName("TextLabel-ErrorMessage");
			vPane.add(error);
		}
		
		// Tilføj log-ud knap
		
		
	}

}
