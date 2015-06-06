package cdio.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Menu extends Composite {

	// Tom menu
	public Menu(Controller controller) {
		VerticalPanel vPane = new VerticalPanel();
		initWidget(vPane);
		
		Label header = new Label("Menu");
		vPane.add(header);
		
		//Style elementet som Menu
		vPane.setStyleName("Menu");
	}
	
	public Menu(Controller controller, String role){
		VerticalPanel vPane = new VerticalPanel();
		initWidget(vPane);
		Label error;
		
		//Style elementet som Menu
		vPane.setStyleName("Menu");
		
		// Tilføj menuen på GUI
		controller.setMenu(this);
		
		switch(role.toUpperCase()){
		case "ADMIN":
			break; // SLET?
		case "FARMACEUT":
			break; // SLET?
		case "VAERKFOERER":
			break;
		default:
			error = new Label("FEJL. Din rolle kunne ikke genkendes. Kontakt admin.");
			error.setStyleName("TextLabel-ErrorMessage");
			vPane.add(error);
		}
		
		// Tilføj log-ud knap
		
		
	}

}
