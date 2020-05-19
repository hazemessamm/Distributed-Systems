package ChatPrimarySecondryServers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SecondryServer {

    public static FileServant servant;

    static {
        try {
            servant = new FileServant();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {

            Registry registry = LocateRegistry.createRegistry(FileServerI.secondaryPort);
            registry.rebind(FileServerI.serviceName, servant);
            System.out.println("Secondary Server is running...");


        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
