package cdio.server.ASE;

public interface IProcedureController {

	public abstract void start();

	void connect(String host, int port);

}