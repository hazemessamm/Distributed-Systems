package ChatPrimarySecondryServers;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//NOTICE!!
//THIS IS THE MAIN SERVER!!

public class FileServer {


	public static String PATH = "C:\\Users\\hazem\\IdeaProjects\\ChatWithServers\\src\\ChatPrimarySecondryServers\\Local\\";
	public static FileServant servant;


	static {
		try {
			servant = new FileServant();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		File f = new File(PATH);
		if(!f.exists())
		{
			System.out.println("Local directory didn't exist, creating Local directory...");
			f.mkdir();
		}
		else
		{
			System.out.println("Local Directory found, running the server...");
		}

		try {

			Registry registry = LocateRegistry.createRegistry(FileServerI.port);
			registry.rebind(FileServerI.serviceName, servant);
			System.out.println("Server is running...");


			//an example Retrieving chat from any available node path.
			//servant.getChatFromNode("C:\\Users\\hazem\\IdeaProjects\\ChatWithServers\\src\\ChatPrimarySecondryServers\\Node1\\", "A.txt");

		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
}
