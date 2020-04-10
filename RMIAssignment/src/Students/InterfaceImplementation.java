package Students;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class InterfaceImplementation extends UnicastRemoteObject implements StudentInterface
{
    ArrayList<Student> students_list = new ArrayList<>();
    ArrayList<Student> above_avg_gpa = new ArrayList<>();
    private static final long serialVersionUID = 20120731125400L;
    protected InterfaceImplementation() throws RemoteException
    {
        super();
    }

    @Override
    public void AddStudent(Student student) {
        students_list.add(student);
    }

    @Override
    public Student getStudentInfo(String id) {
        int i;
        for(i = 0; i < students_list.size(); i++)
        {
            if (students_list.get(i).getId().equalsIgnoreCase(id))
            {
                break;
            }
        }
        return students_list.get(i);

    }

    @Override
    public ArrayList<Student> AboveAverageGPA() {
        double sum = 0;
        for (Student student : students_list) sum += student.getGpa();
        double avg = sum / students_list.size();
        for (Student student : students_list)
        {
            if (student.getGpa() > avg)
            {
                above_avg_gpa.add(student);
            }
        }
        above_avg_gpa.sort(new SortByGPA());
        return above_avg_gpa;
    }
}
