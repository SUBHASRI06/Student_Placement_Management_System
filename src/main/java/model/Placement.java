package model;

public class Placement {
    private int placementId;
    private int studentId;
    private int companyId;
    private String status;

    private String studentName;
    private String companyName;
    private String jobRole;

    public Placement() {}

    public Placement(int placementId, String studentName, String companyName, String jobRole, String status) {
        this.placementId = placementId;
        this.studentName = studentName;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.status = status;
    }

    public int getPlacementId() { return placementId; }
    public void setPlacementId(int placementId) { this.placementId = placementId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
}