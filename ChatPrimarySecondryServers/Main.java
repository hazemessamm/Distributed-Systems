package ChatPrimarySecondryServers;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Main
{

        public static void main(String[] args)
        {
            int pId = 2;
            try
            {
                System.out.println("Process: " + ClientInterface.services[pId]);

                Client obj = new Client(pId);
                initServer(obj);
                initClient(obj, pId);

            }
            catch (RemoteException | NotBoundException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public static void initServer(Client obj) throws RemoteException
    {
        Registry reg = LocateRegistry.createRegistry(obj.myPort);
        reg.rebind(obj.myService, obj);
    }

    private static void initClient(Client obj, int pId) throws RemoteException, NotBoundException
    {

        Timer timer = new Timer();
        FileServant ser = new FileServant();
        int current_state = 0;
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run() {
                obj.fetchNewTransaction();
            }
        }, 0, 100);

        while(true)
        {
            System.out.println("Enter Message");
            String message = obj.scan.nextLine();

            String tId = UUID.randomUUID().toString();
            String sender = obj.myService;
            MessageTransaction t;
            obj.lClock++;
            t = new MessageTransaction(tId, pId, sender, obj.lClock, message);
            try {
                obj.multicastTransaction(t);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}