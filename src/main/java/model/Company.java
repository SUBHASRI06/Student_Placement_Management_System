package model;

public class Company {
    private int companyId;
    private String companyName;
    private double minCgpa;
    private String requiredBranch;
    private String jobRole;
    private double packageLpa;

    public Company() {}

    public Company(int companyId, String companyName, double minCgpa, String requiredBranch, String jobRole, double packageLpa) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.minCgpa = minCgpa;
        this.requiredBranch = requiredBranch;
        this.jobRole = jobRole;
        this.packageLpa = packageLpa;
    }

    public Company(String companyName, double minCgpa, String requiredBranch, String jobRole, double packageLpa) {
        this.companyName = companyName;
        this.minCgpa = minCgpa;
        this.requiredBranch = requiredBranch;
        this.jobRole = jobRole;
        this.packageLpa = packageLpa;
    }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public String getRequiredBranch() { return requiredBranch; }
    public void setRequiredBranch(String requiredBranch) { this.requiredBranch = requiredBranch; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(double packageLpa) { this.packageLpa = packageLpa; }
}