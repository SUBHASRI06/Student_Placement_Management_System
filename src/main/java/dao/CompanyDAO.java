package dao;

import model.Company;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {

    public boolean addCompany(Company company) {
        String sql = "INSERT INTO companies (company_name, min_cgpa, required_branch, job_role, package_lpa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, company.getCompanyName());
            stmt.setDouble(2, company.getMinCgpa());
            stmt.setString(3, company.getRequiredBranch());
            stmt.setString(4, company.getJobRole());
            stmt.setDouble(5, company.getPackageLpa());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT * FROM companies";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Company c = new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getDouble("min_cgpa"),
                        rs.getString("required_branch"),
                        rs.getString("job_role"),
                        rs.getDouble("package_lpa")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Company getCompanyById(int id) {
        String sql = "SELECT * FROM companies WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Company(
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getDouble("min_cgpa"),
                            rs.getString("required_branch"),
                            rs.getString("job_role"),
                            rs.getDouble("package_lpa")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCompany(Company company) {
        String sql = "UPDATE companies SET company_name = ?, min_cgpa = ?, required_branch = ?, job_role = ?, package_lpa = ? WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, company.getCompanyName());
            stmt.setDouble(2, company.getMinCgpa());
            stmt.setString(3, company.getRequiredBranch());
            stmt.setString(4, company.getJobRole());
            stmt.setDouble(5, company.getPackageLpa());
            stmt.setInt(6, company.getCompanyId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating company: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCompany(int companyId) {
        String sql = "DELETE FROM companies WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting company: " + e.getMessage());
            return false;
        }
    }

    public List<Company> getEligibleCompanies(double studentCgpa, String studentBranch) {
        List<Company> eligibleCompanies = new ArrayList<>();
        String sql = "SELECT * FROM companies WHERE min_cgpa <= ? AND (UPPER(required_branch) = 'ANY' OR UPPER(required_branch) = UPPER(?))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, studentCgpa);
            ps.setString(2, studentBranch);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    eligibleCompanies.add(new Company(
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getDouble("min_cgpa"),
                            rs.getString("required_branch"),
                            rs.getString("job_role"),
                            rs.getDouble("package_lpa")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching eligible companies: " + e.getMessage());
        }
        return eligibleCompanies;
    }

    public List<Company> getEligibleCompaniesForStudent(int studentId) {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT c.* FROM companies c " +
                "JOIN students s ON s.student_id = ? " +
                "WHERE s.cgpa >= c.min_cgpa " +
                "AND (UPPER(c.required_branch) = 'ANY' OR UPPER(c.required_branch) = UPPER(s.branch))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Company(
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getDouble("min_cgpa"),
                            rs.getString("required_branch"),
                            rs.getString("job_role"),
                            rs.getDouble("package_lpa")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching eligible companies: " + e.getMessage());
        }
        return list;
    }
}