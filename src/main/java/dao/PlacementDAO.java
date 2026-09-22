package dao;

import model.Company;
import model.Placement;
import model.Student;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlacementDAO {

    /**
     * Registers a student for a company using JDBC Transaction Management.
     * Ensures atomic execution: verifies vacancies, inserts placement record,
     * and decrements company vacancy count within a single transaction.
     */
    public boolean registerPlacement(int studentId, int companyId) {
        String checkSeatsSql = "SELECT vacancies FROM companies WHERE company_id = ?";
        String insertPlacementSql = "INSERT INTO placements (student_id, company_id, status) VALUES (?, ?, 'Registered')";
        String updateCompanySql = "UPDATE companies SET vacancies = vacancies - 1 WHERE company_id = ?";

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();

            // 1. Disable Auto-Commit to begin manual transaction
            conn.setAutoCommit(false);

            // 2. Check remaining vacancies for the company
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSeatsSql)) {
                checkStmt.setInt(1, companyId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int currentVacancies = rs.getInt("vacancies");
                        if (currentVacancies <= 0) {
                            System.err.println("Transaction Aborted: No vacancies remaining for this company.");
                            conn.rollback();
                            return false;
                        }
                    } else {
                        System.err.println("Transaction Aborted: Company ID not found.");
                        conn.rollback();
                        return false;
                    }
                }
            }

            // 3. Insert record into placements table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertPlacementSql)) {
                insertStmt.setInt(1, studentId);
                insertStmt.setInt(2, companyId);
                insertStmt.executeUpdate();
            }

            // 4. Decrement vacancies count in companies table
            try (PreparedStatement updateStmt = conn.prepareStatement(updateCompanySql)) {
                updateStmt.setInt(1, companyId);
                updateStmt.executeUpdate();
            }

            // 5. Commit transaction atomically
            conn.commit();
            System.out.println("Transaction Committed: Student registered and company vacancies updated.");
            return true;

        } catch (SQLException e) {
            System.err.println("Transaction Failed! Rolling back changes: " + e.getMessage());
            if (conn != null) {
                try {
                    // Roll back all operations executed in this transaction
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    // Reset auto-commit back to default true and close connection
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Checks if a student is already registered with a given company.
     */
    public boolean checkDuplicateRegistration(int studentId, int companyId) {
        String sql = "SELECT COUNT(*) FROM placements WHERE student_id = ? AND company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking duplicate registration: " + e.getMessage());
        }
        return false;
    }

    /**
     * Prints an explicit breakdown of eligibility checks for debugging and terminal reporting.
     */
    public void checkAndPrintEligibility(Student student, Company company) {
        System.out.println("\n-----------------------------------------");
        System.out.println("        ELIGIBILITY CHECK REPORT         ");
        System.out.println("-----------------------------------------");
        System.out.printf("Student : %s (CGPA: %.2f | Branch: %s)%n", student.getName(), student.getCgpa(), student.getBranch());
        System.out.printf("Company : %s (Min CGPA: %.2f | Required Branch: %s)%n", company.getCompanyName(), company.getMinCgpa(), company.getRequiredBranch());
        System.out.println("-----------------------------------------");

        boolean cgpaEligible = student.getCgpa() >= company.getMinCgpa();
        boolean branchEligible = company.getRequiredBranch().equalsIgnoreCase("ANY") ||
                company.getRequiredBranch().equalsIgnoreCase(student.getBranch());

        if (cgpaEligible && branchEligible) {
            System.out.println("STATUS  : ELIGIBLE ✅");
            System.out.println("Student meets all requirements for this role.");
        } else {
            System.out.println("STATUS  : NOT ELIGIBLE ❌");
            System.out.println("Reason(s) for Disqualification:");
            if (!cgpaEligible) {
                System.out.printf(" - CGPA criteria failed (Required: %.2f, Student: %.2f)%n", company.getMinCgpa(), student.getCgpa());
            }
            if (!branchEligible) {
                System.out.printf(" - Branch criteria failed (Required: %s, Student: %s)%n", company.getRequiredBranch(), student.getBranch());
            }
        }
        System.out.println("-----------------------------------------\n");
    }

    /**
     * Retrieves all placement records using SQL INNER JOINs.
     */
    public List<Placement> getAllPlacements() {
        List<Placement> list = new ArrayList<>();
        String sql = "SELECT p.placement_id, s.name AS student_name, c.company_name, c.job_role, p.status " +
                "FROM placements p " +
                "JOIN students s ON p.student_id = s.student_id " +
                "JOIN companies c ON p.company_id = c.company_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Placement pl = new Placement(
                        rs.getInt("placement_id"),
                        rs.getString("student_name"),
                        rs.getString("company_name"),
                        rs.getString("job_role"),
                        rs.getString("status")
                );
                list.add(pl);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching placements: " + e.getMessage());
        }
        return list;
    }

    /**
     * Updates the recruitment status for a specific placement ID.
     */
    public boolean updateStatus(int placementId, String newStatus) {
        String sql = "UPDATE placements SET status = ? WHERE placement_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, placementId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Prints aggregate recruitment metrics to the console.
     */
    public void printPlacementAnalytics() {
        String totalStudentsSql = "SELECT COUNT(*) FROM students";
        String totalPlacementsSql = "SELECT COUNT(*) FROM placements WHERE status = 'Selected'";
        String companyStatsSql = "SELECT c.company_name, COUNT(p.placement_id) AS total_selected " +
                "FROM placements p " +
                "JOIN companies c ON p.company_id = c.company_id " +
                "WHERE p.status = 'Selected' " +
                "GROUP BY c.company_name";

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("\n=========================================");
            System.out.println("      PLACEMENT ANALYTICS DASHBOARD      ");
            System.out.println("=========================================");

            // 1. Total Registered Students
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(totalStudentsSql)) {
                if (rs.next()) {
                    System.out.println("Total Students Registered: " + rs.getInt(1));
                }
            }

            // 2. Total Selected Students
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(totalPlacementsSql)) {
                if (rs.next()) {
                    System.out.println("Total Students Placed:   " + rs.getInt(1));
                }
            }

            System.out.println("-----------------------------------------");
            System.out.println("Selections Breakdown by Company:");

            // 3. Grouped breakdown per company
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(companyStatsSql)) {
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf(" - %-20s : %d student(s) selected%n",
                            rs.getString("company_name"),
                            rs.getInt("total_selected"));
                }
                if (!hasData) {
                    System.out.println("   No students currently selected by any company.");
                }
            }
            System.out.println("=========================================\n");

        } catch (SQLException e) {
            System.err.println("Error generating analytics: " + e.getMessage());
        }
    }
}