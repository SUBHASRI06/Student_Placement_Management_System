package util;

import model.Placement;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReportUtil {

    public static boolean exportPlacementsToCSV(List<Placement> placements, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write CSV Header
            writer.println("Placement ID,Student Name,Company Name,Job Role,Status");

            // Write Data Rows
            for (Placement p : placements) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        p.getPlacementId(),
                        p.getStudentName(),
                        p.getCompanyName(),
                        p.getJobRole(),
                        p.getStatus());
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error generating CSV report: " + e.getMessage());
            return false;
        }
    }
}