package ChatPrimarySecondryServers;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class FileServant extends UnicastRemoteObject implements FileServerI {

	private static final long serialVersionUID = 1L;
	public static int firstTime = 0;
	public static HashMap<Character, List<String>> saved_data = new HashMap<>();
	public static int factor = 3;
	public static Queue<String> q = new LinkedList<>();
	public static String mainPath = "C:\\Users\\hazem\\IdeaProjects\\ChatWithServers\\src\\ChatPrimarySecondryServers\\";



	protected FileServant() throws RemoteException {
		super();
		for(int i = 1; i <= 4; i++)
		{
			q.add("Node"+i);
		}
	}


	public HashMap<Character, List<String>> getSavedData()
	{
		return saved_data;
	}
	public void setRecordSavedData(HashMap<Character, List<String>> record)
	{
		saved_data = record;
	}
	public int getFirstTime()
	{
		return firstTime;
	}

	public static void setFirstTime(int firstTime) {

		firstTime = firstTime;
	}

	public Queue<String> getQueue()
	{
		return q;
	}
	public void setQueue(Queue<String> q1)
	{
		q = q1;
	}


	@Override
	public FileSerializable downloadFile(String fileName) {
		System.out.println("Downloading File...");
		try {
			File f = new File(FileServer.PATH + fileName);
			FileSerializable fs = new FileSerializable();
			
			int fileSize = (int) f.length();
			byte[] buffer = new byte[fileSize];
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			in.read(buffer, 0, buffer.length);
			in.close();
			
			fs.setData(buffer);
			fs.setName(fileName);
			fs.setLastModifiedDate(new Date(f.lastModified()));
			
			return fs;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void distributeOnNodes(String fileName, String path) throws IOException {
		File distributedFile = new File(path+fileName);
		File localFile = new File(FileServer.PATH+fileName);
		FileSerializable fs = new FileSerializable();
		if(!localFile.exists())
		{
			return;
		}
		int fileSize = (int) localFile.length();
		byte[] buffer = new byte[fileSize];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(localFile));
		in.read(buffer, 0, buffer.length);
		in.close();

		fs.setData(buffer);
		fs.setName(fileName);
		fs.setLastModifiedDate(new Date(localFile.lastModified()));

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(distributedFile));
		out.write(fs.getData(), 0, fs.getData().length);
		out.flush();

		out.close();
	}

	@Override
	public boolean uploadFile(FileSerializable fs) {
		System.out.println("Uploading File...");
		File localFile = new File(FileServer.PATH + fs.getName());
		
		if(!localFile.exists()) {
			localFile.getParentFile().mkdir();
		}
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
			out.write(fs.getData(), 0, fs.getData().length);
			out.flush();

			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean searchFiles(String fileName) {
		System.out.println("Searching for File...");
		File f = new File(FileServer.PATH + fileName);
		return f.exists();
	}

	@Override
	public boolean deleteFile(String fileName) {
		System.out.println("Deleting File...");
		File f = new File(FileServer.PATH + fileName);
		return f.delete();
	}
	@Override
	public void distributeSavedChat(int factor, String fileName) throws IOException {
		File node1 = new File(mainPath+"Node1");
		File node2 = new File(mainPath+"Node2");
		File node3 = new File(mainPath+"Node3");
		File node4 = new File(mainPath+"Node4");
		if(firstTime == 0) {
			if (node1.exists()) {
				System.out.println("Backup Node1 available");
			} else {
				new File(mainPath + "Node1").mkdir();
				System.out.println("Backup Node1 wasn't available but created now");
			}

			if (node2.exists()) {
				System.out.println("Backup Node2 available");
			} else {
				new File(mainPath + "Node2").mkdir();
				System.out.println("Backup Node2 wasn't available but created now");
			}
			if (node3.exists()) {
				System.out.println("Backup Node3 available");
			} else {
				new File(mainPath + "Node3").mkdir();
				System.out.println("Backup Node3 wasn't available but created now");
			}
			if (node4.exists()) {
				System.out.println("Backup Node4 available");
			} else {
				new File(mainPath + "Node4").mkdir();
				System.out.println("Backup Node4 wasn't available but created now");
			}
		}


		String []places = new String[factor];
		int current_node_saved=65;
			for(int j = 0; j < factor; j++)
			{
				String removedNode = q.remove();
				places[j] = removedNode;
				if(removedNode.equalsIgnoreCase("node1"))
				{
					distributeOnNodes("\\"+fileName, node1.getPath());
					q.add(removedNode);
				}
				else if(removedNode.equalsIgnoreCase("node2"))
				{
					distributeOnNodes("\\"+fileName, node2.getPath());
					q.add(removedNode);
				}
				else if(removedNode.equalsIgnoreCase("node3"))
				{
					distributeOnNodes("\\"+fileName, node3.getPath());
					q.add(removedNode);
				}
				else if(removedNode.equalsIgnoreCase("node4"))
				{
					distributeOnNodes("\\"+fileName, node4.getPath());
					q.add(removedNode);
				}
				//System.out.println("Places  " + places);
				if(firstTime == 0) {
					saved_data.put(fileName.charAt(0), new ArrayList<String>(Arrays.asList(places)));
				}
			}

			current_node_saved++;
		firstTime = 1;
	}

	public void getChatFromNode(String nodePath, String fileName) throws IOException {
		File f = new File(nodePath+fileName);
		File downloaded = new File(FileServer.PATH+fileName);
		FileSerializable fs = new FileSerializable();

		if(f.exists())
		{
			System.out.println("File exists downloading...");
			int fileSize = (int) f.length();
			byte[] buffer = new byte[fileSize];
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			in.read(buffer, 0, buffer.length);
			in.close();

			fs.setData(buffer);
			fs.setName(fileName);
			fs.setLastModifiedDate(new Date(f.lastModified()));

			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(downloaded));
			out.write(fs.getData(), 0, fs.getData().length);
			out.flush();

			out.close();


		}
		else
		{
			System.out.println("File not found");
		}


	}

	public void displayHashMap()
	{
		System.out.println(saved_data);
	}

}
