package model;

public class Student {
    private int studentId;
    private String name;
    private String branch;
    private double cgpa;
    private String email;
    private String phone;

    public Student() {}

    public Student(int studentId, String name, String branch, double cgpa, String email, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.branch = branch;
        this.cgpa = cgpa;
        this.email = email;
        this.phone = phone;
    }

    public Student(String name, String branch, double cgpa, String email, String phone) {
        this.name = name;
        this.branch = branch;
        this.cgpa = cgpa;
        this.email = email;
        this.phone = phone;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}