package cdio.client;

import cdio.client.contents.OpretOpr;
import cdio.client.contents.OpretPB;
import cdio.client.contents.OpretRB;
import cdio.client.contents.OpretRaavare;
import cdio.client.contents.OpretRecept;
import cdio.client.contents.RetOpr;
import cdio.client.contents.RetRaavare;
import cdio.client.contents.SletOpr;
import cdio.client.contents.SletProduktbatch;
import cdio.client.contents.SletRecept;
import cdio.client.contents.StartPage;
import cdio.client.contents.VisOpr;
import cdio.client.contents.VisPB;
import cdio.client.contents.VisRB;
import cdio.client.contents.VisRaavarer;
import cdio.client.contents.VisRecept;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
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
	private Label time;
	private Button logud;
	private Timer sec;

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
		Controller.service.getUsername(Controller.token, new AsyncCallback<String>(){

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
				
				Label session = new Label("Session:");
				session.setStyleName("TextLabel-Logud");
				userFt.setWidget(1, 0, session);
				time = new Label("");
				time.setStyleName("TextLabel-Logud");
				userFt.setWidget(1, 1, time);

				logud = new Button("Log ud");
				logud.setStyleName("Button-Logud");
				logud.addClickHandler(new Logud()); // Clickhandler
				userFt.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
				userFt.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
				userFt.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);

				userFt.setWidget(2, 1, logud);	
				userFt.setStyleName("FlexTable-Userinfo");

				ftMenu.setWidget(i, 0, userFt);
				i++;

				Anchor start = new Anchor("Startside");
				start.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						con.setContent(new StartPage(con));
					}
				});
				ftMenu.setWidget(i, 0, start);
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

		Anchor vis = new Anchor("Vis brugere");
		vis.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new VisOpr());
			}
		});
		ft.setWidget(1, 0, vis);

		Anchor ret = new Anchor("Ret bruger");
		ret.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new RetOpr());
			}
		});
		ft.setWidget(2, 0, ret);

		Anchor opret = new Anchor("Opret bruger");
		opret.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new OpretOpr());				
			}
		});
		ft.setWidget(3, 0, opret);

		Anchor slet = new Anchor("Slet bruger");
		ft.setWidget(4, 0, slet);
		slet.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new SletOpr());
			}
		});

		return ft;
	}

	// Bygger og returnerer Farmaceut menu
	private FlexTable farmMenu(){
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Farmaceut");

		Anchor vis = new Anchor("Vis råvarer");
		ft.setWidget(1, 0, vis);
		vis.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new VisRaavarer());
			}
		});

		Anchor ret = new Anchor("Ret råvarer");
		ft.setWidget(2, 0, ret);
		ret.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new RetRaavare());
			}
		});

		Anchor opret = new Anchor("Opret råvare");
		ft.setWidget(3, 0, opret);
		opret.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new OpretRaavare());
			}
		});

		Anchor visRecept = new Anchor("Vis recept");
		ft.setWidget(4, 0, visRecept);
		visRecept.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new VisRecept());
			}
		});

		Anchor opretRecept = new Anchor("Opret recept");
		ft.setWidget(5, 0, opretRecept);
		opretRecept.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new OpretRecept());
			}
		});

		Anchor sletRecept = new Anchor("Slet recept");
		ft.setWidget(6, 0, sletRecept);
		sletRecept.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new SletRecept());
			}
		});

		return ft;
	}

	// Bygger og returnerer Værkfører menu
	private FlexTable vaerkMenu(){
		FlexTable ft = new FlexTable();
		ft.getRowFormatter().setStyleName(0, "FlexTable-Header");
		ft.setText(0, 0, "Værkfører");

		Anchor visRB = new Anchor("Vis råvarebatch");
		ft.setWidget(1, 0, visRB);
		visRB.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new VisRB());
			}
		});

		Anchor opretRB = new Anchor("Opret råvarebatch");
		ft.setWidget(2, 0, opretRB);
		opretRB.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new OpretRB());
			}
		});

		Anchor visPB = new Anchor("Vis produktbatch");
		ft.setWidget(3, 0, visPB);
		visPB.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new VisPB());
			}
		});

		Anchor opretPB = new Anchor("Opret produktbatch");
		ft.setWidget(4, 0, opretPB);
		opretPB.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new OpretPB());
			}
		});
		
		Anchor sletPB = new Anchor("Slet produktbatch");
		ft.setWidget(5, 0, sletPB);
		sletPB.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				con.setContent(new SletProduktbatch());
			}
		});

		return ft;
	}

	public void timer(){
		sec = new Timer(){
			@Override
			public void run() {
				if (Controller.seconds < 10)
					time.setText(Integer.toString(Controller.minutes) + ":0" + Controller.seconds);
				else	
					time.setText(Integer.toString(Controller.minutes) + ":" + Controller.seconds);
				Controller.seconds--;
				if (Controller.seconds == -1){
					Controller.seconds = 59;
					Controller.minutes--;
					if (Controller.minutes < 5){
						time.setStyleName("Time-Error");
						if (Controller.minutes == -1){
							Window.alert("Sessionen er udløbet.");
							cancelTimer();
							logud.click(); // Log brugeren ud
						}
					} 
					else 
						time.setStyleName("TextLabel-Logud");
				}
			}
		};
		sec.scheduleRepeating(1000); // Opdater hvert minut
	}
	
	public void cancelTimer(){
		sec.cancel();
	}

	// Clickhandler til logud-knappen
	private class Logud implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			con.logud();
		}

	}
}
