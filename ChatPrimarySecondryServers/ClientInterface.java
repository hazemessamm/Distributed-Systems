package ChatPrimarySecondryServers;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{
    String[] ipAddr = {"127.0.0.1", "127.0.0.1", "127.0.0.1"};
    String[] services = {"A", "B", "C"};
    Integer[] ports = {2000, 3000, 4000};
    int NumberOfNodes = 3;
    int Factor = 3;

    void performTransaction(MessageTransaction t) throws RemoteException, NotBoundException;
    void ack(MessageTransaction t) throws RemoteException;
}
