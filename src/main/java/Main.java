import dao.CompanyDAO;
import dao.PlacementDAO;
import dao.StudentDAO;
import model.Company;
import model.Placement;
import model.Student;
import util.ValidationUtil;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CompanyDAO companyDAO = new CompanyDAO();
    private static final PlacementDAO placementDAO = new PlacementDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=========================================");
            System.out.println("  STUDENT PLACEMENT MANAGEMENT SYSTEM");
            System.out.println("=========================================");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Add New Company");
            System.out.println("4. View All Companies");
            System.out.println("5. Check All Eligible Companies for Student");
            System.out.println("6. Check Eligibility for Specific Company"); // <--- ADD THIS
            System.out.println("7. Apply/Register Student for Company");
            System.out.println("8. View All Placements");
            System.out.println("9. Update Placement Status");
            System.out.println("10. Export Placement Report to CSV");
            System.out.println("11. View Placement Analytics Dashboard");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudents();
                case 3 -> addCompany();
                case 4 -> viewCompanies();
                case 5 -> viewEligibleCompaniesForStudent();
                case 6 -> checkSingleEligibility();
                case 7 -> registerPlacement();
                case 8 -> viewPlacements();
                case 9 -> updatePlacementStatus();
                case 10 -> exportCSVReport();
                case 11 -> placementDAO.printPlacementAnalytics();
                case 12 -> {
                    System.out.println("Exiting System. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

        }
    }

    private static int readIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private static void addStudent() {
        System.out.println("\n--- Add New Student ---");

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Branch (e.g. CSE, ECE, IT): ");
        String branch = scanner.nextLine().trim();

        System.out.print("Enter CGPA (0.0 - 10.0): ");
        double cgpa = ValidationUtil.readValidDouble(scanner, "", 0.0, 10.0);

        System.out.print("Enter Email: ");
        String email = ValidationUtil.readValidEmail(scanner, "");

        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine().trim();

        Student student = new Student(name, branch, cgpa, email, phone);
        if (studentDAO.addStudent(student)) {
            System.out.println("SUCCESS: Student registered successfully!");
        } else {
            System.out.println("ERROR: Failed to register student.");
        }
    }

    private static void viewStudents() {
        System.out.println("\n--- List of Students ---");
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student s : students) {
            System.out.printf("ID: %d | Name: %s | Branch: %s | CGPA: %.2f | Email: %s | Phone: %s%n",
                    s.getStudentId(), s.getName(), s.getBranch(), s.getCgpa(), s.getEmail(), s.getPhone());
        }
    }

    private static void addCompany() {
        System.out.println("\n--- Add New Company ---");

        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            name = scanner.nextLine().trim();
        }

        System.out.print("Enter Minimum CGPA Required: ");
        double minCgpa = readDoubleInput();
        if (minCgpa < 0 || minCgpa > 10.0) {
            System.out.println("ERROR: Invalid CGPA entered.");
            return;
        }

        System.out.print("Enter Required Branch (or ANY): ");
        String branch = scanner.nextLine().trim();

        System.out.print("Enter Job Role: ");
        String role = scanner.nextLine().trim();

        System.out.print("Enter Package (LPA): ");
        double pkg = readDoubleInput();

        Company company = new Company(name, minCgpa, branch, role, pkg);
        if (companyDAO.addCompany(company)) {
            System.out.println("SUCCESS: Company added successfully!");
        } else {
            System.out.println("ERROR: Failed to add company.");
        }
    }

    private static void viewCompanies() {
        System.out.println("\n--- List of Companies ---");
        List<Company> companies = companyDAO.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }
        for (Company c : companies) {
            System.out.printf("ID: %d | Company: %s | Min CGPA: %.2f | Branch: %s | Role: %s | Package: %.2f LPA%n",
                    c.getCompanyId(), c.getCompanyName(), c.getMinCgpa(), c.getRequiredBranch(), c.getJobRole(), c.getPackageLpa());
        }
    }

    private static void viewEligibleCompaniesForStudent() {
        System.out.println("\n--- View Eligible Companies ---");
        System.out.print("Enter Student ID: ");
        int studentId = readIntInput();

        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            System.out.println("ERROR: Student ID not found.");
            return;
        }

        System.out.printf("Checking eligibility for %s (Branch: %s | CGPA: %.2f)...%n",
                student.getName(), student.getBranch(), student.getCgpa());

        List<Company> eligible = companyDAO.getEligibleCompanies(student.getCgpa(), student.getBranch());

        if (eligible.isEmpty()) {
            System.out.println("No eligible companies found for this student.");
            return;
        }

        System.out.println("\n--- Eligible Companies List ---");
        for (Company c : eligible) {
            System.out.printf("ID: %d | Company: %s | Role: %s | Package: %.2f LPA | Min CGPA: %.2f | Required Branch: %s%n",
                    c.getCompanyId(), c.getCompanyName(), c.getJobRole(), c.getPackageLpa(), c.getMinCgpa(), c.getRequiredBranch());
        }
    }

    private static void registerPlacement() {
        System.out.println("\n--- Register Student for Company ---");
        System.out.print("Enter Student ID: ");
        int studentId = readIntInput();
        System.out.print("Enter Company ID: ");
        int companyId = readIntInput();

        Student student = studentDAO.getStudentById(studentId);
        Company company = companyDAO.getCompanyById(companyId);

        if (student == null || company == null) {
            System.out.println("ERROR: Invalid Student ID or Company ID.");
            return;
        }

        // Check Eligibility criteria
        if (student.getCgpa() < company.getMinCgpa()) {
            System.out.printf("ELIGIBILITY FAILED: Student CGPA (%.2f) is lower than Company Minimum CGPA requirement (%.2f).%n",
                    student.getCgpa(), company.getMinCgpa());
            return;
        }

        if (!company.getRequiredBranch().equalsIgnoreCase("ANY") &&
                !company.getRequiredBranch().equalsIgnoreCase(student.getBranch())) {
            System.out.printf("ELIGIBILITY FAILED: Student Branch (%s) does not match Required Branch (%s).%n",
                    student.getBranch(), company.getRequiredBranch());
            return;
        }

        if (placementDAO.registerPlacement(studentId, companyId)) {
            System.out.println("SUCCESS: Student successfully registered for " + company.getCompanyName() + "!");
        } else {
            System.out.println("ERROR: Registration failed (Student may already be registered for this company or company has no vacancies).");
        }
    }

    private static void viewPlacements() {
        System.out.println("\n--- All Registered Placements ---");
        List<Placement> placements = placementDAO.getAllPlacements();
        if (placements.isEmpty()) {
            System.out.println("No placement records found.");
            return;
        }
        for (Placement p : placements) {
            System.out.printf("Placement ID: %d | Student: %s | Company: %s | Role: %s | Status: %s%n",
                    p.getPlacementId(), p.getStudentName(), p.getCompanyName(), p.getJobRole(), p.getStatus());
        }
    }

    private static void updatePlacementStatus() {
        System.out.println("\n--- Update Placement Status ---");
        System.out.print("Enter Placement ID: ");
        int placementId = readIntInput();
        System.out.print("Enter New Status (e.g. Interviewed, Selected, Rejected): ");
        String status = scanner.nextLine().trim();

        if (placementDAO.updateStatus(placementId, status)) {
            System.out.println("SUCCESS: Placement status updated!");
        } else {
            System.out.println("ERROR: Failed to update status.");
        }
    }

    private static void exportCSVReport() {
        List<Placement> placements = placementDAO.getAllPlacements();
        if (placements.isEmpty()) {
            System.out.println("No placement records found to export.");
            return;
        }
        String fileName = "placement_report.csv";
        if (util.ReportUtil.exportPlacementsToCSV(placements, fileName)) {
            System.out.println("SUCCESS: Report successfully exported to " + fileName);
        } else {
            System.out.println("ERROR: Failed to export report.");
        }
    }
    private static void checkSingleEligibility() {
        System.out.println("\n--- Check Eligibility for a Company ---");
        System.out.print("Enter Student ID: ");
        int studentId = readIntInput();
        System.out.print("Enter Company ID: ");
        int companyId = readIntInput();

        Student student = studentDAO.getStudentById(studentId);
        Company company = companyDAO.getCompanyById(companyId);

        if (student == null || company == null) {
            System.out.println("ERROR: Invalid Student ID or Company ID.");
            return;
        }

        placementDAO.checkAndPrintEligibility(student, company);
    }
}