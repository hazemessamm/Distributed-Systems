package Students;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class StudentsClient {
    public static void main(String[] args)  {
        try
        {
            ArrayList<Student> average_gpa;
            Registry registry = LocateRegistry.getRegistry(2000);
            StudentInterface ServerStub = (StudentInterface) registry.lookup(StudentInterface.ServiceName);
            ServerStub.AddStudent(new Student("12345670", "Alice", "CS", 3.1, 5));
            ServerStub.AddStudent(new Student("12345671", "Bob", "SE", 3.0, 6));
            ServerStub.AddStudent(new Student("12345672", "Carol", "IS", 3.6, 5));
            ServerStub.AddStudent(new Student("12345673", "Dan", "GM", 3.7, 7));
            average_gpa = ServerStub.AboveAverageGPA();
            for (Student student : average_gpa) {
                System.out.println(student.getName());
            }
            //System.out.println(ServerStub.getStudentInfo("2").getName());
        }
        catch (RemoteException | NotBoundException s)
        {
            System.out.println(s);
        }
    }
}
