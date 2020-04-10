package Students;

import java.io.Serializable;

public class Student implements Serializable {
    private String id, name, department;
    private double gpa;
    private int semester;
    public Student(String id, String name, String department, double gpa, int semester)
    {
        this.id = id;
        this.name = name;
        this.department = department;
        this.gpa = gpa;
        this.semester = semester;
    }
    public String getId()
    {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public double getGpa() {
        return gpa;
    }
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    public int getSemester() {
        return semester;
    }
    public void setSemester(int semester) {
        this.semester = semester;
    }
}
