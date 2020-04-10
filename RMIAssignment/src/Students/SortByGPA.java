package Students;

import java.util.Comparator;

public class SortByGPA implements Comparator<Student> {
    public int compare(Student a, Student b)
    {
        if (a.getGpa() < b.getGpa()) return -1;
        if(a.getGpa() > b.getGpa()) return 1;
        return 0;
    }
}
