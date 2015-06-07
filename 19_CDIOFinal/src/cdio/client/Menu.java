package cdio.client;

import cdio.client.contents.OpretOpr;
import cdio.client.contents.RetOpr;
import cdio.client.contents.SletOpr;
import cdio.client.contents.VisOpr;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Menu extends Composite {

	private VerticalPanel vPane;
	private Controller con;
	private String username;
	private Label error;

	// Tom menu
	public Menu(Controller controller) {
		this.con = controller;
		vPane = new VerticalPanel();
		initWidget(vPane);
		error = new Label("");
		error.setStyleName("TextLabel-ErrorMessage");

		Label header = new Label("Loading...");
		vPane.add(header);
	}

	public void changeMenu(final String role){
		con.getService().getUsername(con.getToken(), new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				error.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				vPane.clear(); // Fjerner den gamle menu
				username = result;				
				FlexTable ftMenu = new FlexTable();
				ftMenu.setStyleName("FlexTable-Menu");
				int i = 0;

				// Bruger-info og Logud-knap
				FlexTable userFt = new FlexTable();
				Label userTxt = new Label("Bruger:");
				userTxt.setStyleName("TextLabel-Logud");
				userFt.setWidget(0, 0, userTxt);
				Label userName = new Label(username);
				userName.setStyleName("TextLabel-Logud");
				userFt.setWidget(0, 1, userName);
				Button logud = new Button("Log ud");
				logud.setStyleName("Button-Logud");
				logud.addClickHandler(new Logud()); // Clickhandler
				userFt.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
				userFt.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
				
				userFt.setWidget(1, 1, logud);	
				userFt.setStyleName("FlexTable-Userinfo");

				ftMenu.setWidget(i, 0, userFt);
				i++;

				// Identificer hvilke menuer brugeren skal vises for
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
				}
				vPane.add(ftMenu);
			}
		});
		
		vPane.add(error);
	}

	// Bygger og returnerer Admin menu
	private FlexTable adminMenu(){
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Admin");

		Anchor vis = new Anchor("Vis operatører");
		vis.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new VisOpr());
			}
		});
		ft.setWidget(1, 0, vis);

		Anchor ret = new Anchor("Ret operatører");
		ret.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new RetOpr());
			}
		});
		ft.setWidget(2, 0, ret);

		Anchor opret = new Anchor("Opret operatør");
		opret.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new OpretOpr());
			}
		});
		ft.setWidget(3, 0, opret);

		Anchor slet = new Anchor("Slet operatør");
		slet.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new SletOpr());
			}
		});
		ft.setWidget(4, 0, slet);

		return ft;
	}

	// Bygger og returnerer Farmaceut menu
	private FlexTable farmMenu(){
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Farmaceut");

		Anchor vis = new Anchor("Vis råvarer");
		ft.setWidget(1, 0, vis);

		Anchor ret = new Anchor("Ret råvarer");
		ft.setWidget(2, 0, ret);

		Anchor opret = new Anchor("Opret råvare");
		ft.setWidget(3, 0, opret);

		Anchor visRecept = new Anchor("Vis recept");
		ft.setWidget(4, 0, visRecept);

		Anchor opretRecept = new Anchor("Opret recept");
		ft.setWidget(5, 0, opretRecept);

		return ft;
	}

	// Bygger og returnerer Værkfører menu
	private FlexTable vaerkMenu(){
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Værkfører");

		Anchor vis = new Anchor("Vis råvarebatch");
		ft.setWidget(1, 0, vis);

		Anchor opret = new Anchor("Opret råvarebatch");
		ft.setWidget(2, 0, opret);

		Anchor visP = new Anchor("Vis produktbatch");
		ft.setWidget(3, 0, visP);

		Anchor opretP = new Anchor("Opret produktbatch");
		ft.setWidget(4, 0, opretP);

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
