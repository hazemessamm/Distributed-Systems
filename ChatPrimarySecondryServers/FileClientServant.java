package ChatPrimarySecondryServers;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Date;

public class FileClientServant {
	
	public void download(FileServerI fileServer, String fileName) throws RemoteException {
		FileSerializable fs = fileServer.downloadFile(fileName);
		File localFile = new File(Client.PATH + fileName);
		
		if(!localFile.exists()) {
			localFile.getParentFile().mkdir();
		}
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
			out.write(fs.getData(), 0, fs.getData().length);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void upload(FileServerI fileServer, String fileName) throws IOException {
		FileSerializable fs = new FileSerializable();
		File localFile = new File(Client.PATH + fileName);
		
		int fileSize = (int) localFile.length();
		byte[] buffer = new byte[fileSize];
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(localFile));
			in.read(buffer, 0, buffer.length);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fs.setData(buffer);
		fs.setName(fileName);
		fs.setLastModifiedDate(new Date(localFile.lastModified()));
		
		boolean res = fileServer.uploadFile(fs);
		fileServer.distributeSavedChat(ClientInterface.Factor, fileName);
		if(res)System.out.println("File uploaded successfully.");
		else System.out.println("An error occurred. Try again later");
	}
	
	public void search(FileServerI fileServer, String fileName) throws RemoteException {
		boolean res = fileServer.searchFiles(fileName);
		if(res)System.out.println("File found");
		else System.out.println("No matches found");
	}
	
	public void delete(FileServerI fileServer, String fileName) throws RemoteException {
		boolean res = fileServer.deleteFile(fileName);
		if(res)System.out.println("File deleted successfully.");
		else System.out.println("An error occurred. Try again later");
	}
	
}
