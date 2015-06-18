package ASE;

import java.io.IOException;
import java.net.Socket;

public interface IProcedureController {

	public abstract void start() throws IOException;

	void connect(Socket socket);

}