package ChatPrimarySecondryServers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Client extends UnicastRemoteObject implements ClientInterface
{
    private static final long serialVersionUID = 1L;
    public static final String PATH = "C:\\Users\\hazem\\IdeaProjects\\ChatWithServers\\src\\ChatPrimarySecondryServers\\";
    final static int n = ipAddr.length;

    Registry registry;
    FileServerI fileServer;

    int lClock;
    int myPort;
    String myIp, myService;
    PriorityQueue<MessageTransaction> transactions;
    HashMap<String, Integer> transactionsAcks;
    Scanner scan;
    ArrayList<String> chat_history = new ArrayList<>();
    BufferedWriter file;

    protected Client(int idx) throws IOException, NotBoundException {
        myIp = ipAddr[idx];
        myPort = ports[idx];
        myService = services[idx];
        lClock = 0;
        transactions = new PriorityQueue<>();
        transactionsAcks = new HashMap<>();
        scan = new Scanner(System.in);
    }


    public int checkServer() throws RemoteException, NotBoundException {
        int flag = 0;
        try {
            this.registry = LocateRegistry.getRegistry("127.0.0.1", FileServerI.port);
            this.fileServer = (FileServerI) registry.lookup(FileServerI.serviceName);
            flag = 0;
        }
        catch (java.rmi.ConnectException ex)
        {
            try {
                this.registry = LocateRegistry.getRegistry("127.0.0.1", FileServerI.secondaryPort);
                this.fileServer = (FileServerI) registry.lookup(FileServerI.serviceName);
                flag = 1;
            }
            catch (java.rmi.ConnectException e)
            {
                flag = 2;
            }
        }
        catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public void multicastTransaction(MessageTransaction t) throws NotBoundException, IOException {
        boolean delay = false;
        int current_state = checkServer(); //CHECK WHETHER THE PRIMARY SERVER IS STILL ALIVE OR NOT (HEARTBEAT)
        if(current_state == 0)
        {
            File f = new File(PATH+"\\"+myService+".txt");

            String fileName = myService + ".txt";
            FileClientServant servant = new FileClientServant();
            if(!f.exists())
            {
                File f1 = new File(FileServer.PATH+fileName);
                if(f1.exists()) {
                    servant.download(fileServer, fileName);
                    System.out.println("Existing Chat found and downloaded");
                }
            }
            file = new BufferedWriter(new FileWriter(PATH+"\\"+myService+".txt", true));
            file.write(t.message+"\n");
            file.close();

            servant.upload(fileServer, fileName);
        }
        else if(current_state == 1)
        {
            File f = new File(PATH+"\\"+myService+".txt");
            String fileName = myService + ".txt";
            FileClientServant servant = new FileClientServant();
            if(!f.exists())
            {
                File f1 = new File(FileServer.PATH+fileName);
                if(f1.exists()) {
                    servant.download(fileServer, fileName);
                    System.out.println("Existing Chat found and downloaded");
                }

            }
            file = new BufferedWriter(new FileWriter(PATH+"\\"+myService+".txt", true));
            file.write(t.message+"\n");
            file.close();
            System.out.println("Primary server down, Switching to secondary server...");

            servant.upload(fileServer, fileName);

        }
        else
        {
            System.out.println("No available servers, messages won't be saved");
        }

        for(int i=0; i<n; i++){
            if(delay && t.sender.equals("A")) { // Delay Sending Transaction from A to C
                if(i == 2) {
                    Delayer delayer = new Delayer(ports[i], services[i], t);
                    new Thread(delayer).start();
                    continue;
                }
            }
            Registry reg = LocateRegistry.getRegistry(ports[i]);
            ClientInterface e = (ClientInterface) reg.lookup(services[i]);
            e.performTransaction(t);
        }
        System.out.println("End Multicast Transaction:");
        displayTransactions();
        displayAcks();
    }

    @Override
    public void performTransaction(MessageTransaction t) throws RemoteException, NotBoundException {
        transactions.add(t);
        if(!t.sender.equalsIgnoreCase(myService)) {
            lClock = Math.max(lClock, t.clock) + 1;
        }
        System.out.println("Perform Transaction:");
        displayTransactions();
        displayAcks();
        multicastAck(t);
    }

    private void multicastAck(MessageTransaction t) throws RemoteException, NotBoundException {
        boolean delay = false;
        for(int i=0; i<n; i++){
            if(delay && myService.equals("A")) { // Delay Sending Ack from A to C
                if(i == 2) {
                    System.out.println("|||||||||||||||||||||||||||||||||||||||||");
                    AckDelayer ackDelayer = new AckDelayer(ports[i], services[i], t);
                    new Thread(ackDelayer).start();
                    continue;
                }
            }
            Registry reg = LocateRegistry.getRegistry(ports[i]);
            ClientInterface e = (ClientInterface) reg.lookup(services[i]);
            e.ack(t);
        }
    }

    @Override
    public void ack(MessageTransaction t) {
        if(transactionsAcks.containsKey(t.mid)){
            transactionsAcks.put(t.mid, transactionsAcks.get(t.mid) + 1);
        }
        else transactionsAcks.put(t.mid, 1);
    }


    public void fetchNewTransaction() {
        if(transactions.size() > 0 && transactionsAcks.containsKey(transactions.peek().mid)
                && transactionsAcks.get(transactions.peek().mid) == n){

            System.out.println("Fetch New Transaction:");
            displayTransactions();
            displayAcks();

            MessageTransaction t = transactions.poll();
            transactionsAcks.remove(t.mid);
            //chat_history.add(t.message);

            System.out.println("After Fetch New Transaction:");
            displayTransactions();
            displayAcks();
            //displayHistory();

            System.out.println("Performing Message Transaction: " + t);
            System.out.println("New Message = " + t.message);
        }
    }

    private void displayTransactions() {
        System.out.println("---------------Transactions--------------");
        for(MessageTransaction t: transactions) {
            System.out.println(t.message + ", " + t.sender + ", " + t.clock + ", " + t.mid);
        }
        System.out.println("=========================================");
    }

    private void displayAcks() {
        System.out.println("---------------Acks--------------");
        Set<String> keys = transactionsAcks.keySet();
        for(String k: keys) {
            System.out.println("(" + k + ", " + transactionsAcks.get(k) + ")");
        }
        System.out.println("=========================================");
    }


}