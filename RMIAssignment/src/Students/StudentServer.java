package Students;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StudentServer {
    public static void main(String[] args) {
        try
        {
            InterfaceImplementation InterfaceObj = new InterfaceImplementation();
            Registry registry = LocateRegistry.createRegistry(StudentInterface.port);
            registry.rebind(StudentInterface.ServiceName, InterfaceObj);
            System.out.println("Server is currently running...");
        }
        catch (RemoteException s)
        {
            System.out.println(s);
        }
    }
}
