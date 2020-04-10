package Students;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface StudentInterface extends Remote {
    String ServiceName = "StudentsServices";
    int port = 2000;
    void AddStudent(Student student) throws RemoteException;
    Student getStudentInfo(String id) throws RemoteException;
    ArrayList<Student> AboveAverageGPA() throws RemoteException;
}
