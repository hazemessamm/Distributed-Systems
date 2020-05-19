package ChatPrimarySecondryServers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileServerI extends Remote {
	int port = 5000;
	int secondaryPort = 6000;
	String serviceName = "FileServer";
	String secondaryServiceName = "SecondaryFileServer";
	FileSerializable downloadFile(String fileName) throws RemoteException;
	boolean uploadFile(FileSerializable f) throws RemoteException;
	boolean searchFiles(String fileName) throws RemoteException;
	boolean deleteFile(String fileName) throws RemoteException;
	void distributeSavedChat(int factor, String fileName) throws RemoteException, FileNotFoundException, IOException;
}
