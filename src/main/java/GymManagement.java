import java.sql.*;
import java.util.Scanner;

public class GymManagement {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/BORZ/Labb3Webutv.db";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean quit = false;
        printMenu();

        while (!quit) {
            System.out.println("\nChoose an option (6 to show menu):");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0:
                    System.out.println("Shutting down...");
                    quit = true;
                    break;
                case 1:
                    memberSelectAll();
                    break;
                case 2:
                    addNewMember();
                    break;
                case 3:
                    updateMember();
                    break;
                case 4:
                    deleteMember();
                    break;
                case 5:
                    showMemberActivities();
                    break;
                case 6:
                    printMenu();
                    break;
                case 7:
                    activitySelectAll();
                    break;
                case 8:
                    addNewActivity();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu Options:");
        System.out.println("0 - Exit");
        System.out.println("1 - View all members");
        System.out.println("2 - Add a new member");
        System.out.println("3 - Update a member");
        System.out.println("4 - Delete a member");
        System.out.println("5 - View members with their activities");
        System.out.println("6 - Show menu options");
        System.out.println("7 - View all activities");
        System.out.println("8 - Add a new activity");
    }

    private static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }

    // CRUD Methods for Member Table
    private static void memberSelectAll() {
        String sql = "SELECT * FROM Member";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("memberId") +
                        ", Name: " + rs.getString("name") +
                        ", Membership: " + rs.getString("membershipType") +
                        ", Age: " + rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving members: " + e.getMessage());
        }
    }

    private static void addNewMember() {
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter membership type:");
        String membershipType = scanner.nextLine();
        System.out.println("Enter age:");
        int age = scanner.nextInt();

        String sql = "INSERT INTO Member (name, membershipType, age) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, membershipType);
            pstmt.setInt(3, age);
            pstmt.executeUpdate();
            System.out.println("Member added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    private static void updateMember() {
        System.out.println("Enter the ID of the member to update:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter new name:");
        String name = scanner.nextLine();
        System.out.println("Enter new membership type:");
        String membershipType = scanner.nextLine();
        System.out.println("Enter new age:");
        int age = scanner.nextInt();

        String sql = "UPDATE Member SET name = ?, membershipType = ?, age = ? WHERE memberId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, membershipType);
            pstmt.setInt(3, age);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Member updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
        }
    }

    private static void deleteMember() {
        System.out.println("Enter the ID of the member to delete:");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Member WHERE memberId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Member deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
        }
    }

    // Display Members and Their Activities
    private static void showMemberActivities() {
        String sql = "SELECT Member.name, Activity.activityName, Activity.duration " +
                "FROM Member " +
                "JOIN Activity ON Member.memberId = Activity.memberId";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("Member: " + rs.getString("name") +
                        ", Activity: " + rs.getString("activityName") +
                        ", Duration: " + rs.getInt("duration") + " mins");
            }
        } catch (SQLException e) {
            System.out.println("Error displaying activities: " + e.getMessage());
        }
    }

    // CRUD Methods for Activity Table
    private static void activitySelectAll() {
        String sql = "SELECT * FROM Activity";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("activityId") +
                        ", Name: " + rs.getString("activityName") +
                        ", Duration: " + rs.getInt("duration") +
                        ", Member ID: " + rs.getInt("memberId"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving activities: " + e.getMessage());
        }
    }

    private static void addNewActivity() {
        System.out.println("Enter activity name:");
        String activityName = scanner.nextLine();
        System.out.println("Enter duration (in minutes):");
        int duration = scanner.nextInt();
        System.out.println("Enter the member ID this activity belongs to:");
        int memberId = scanner.nextInt();

        String sql = "INSERT INTO Activity (activityName, duration, memberId) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, activityName);
            pstmt.setInt(2, duration);
            pstmt.setInt(3, memberId);
            pstmt.executeUpdate();
            System.out.println("Activity added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding activity: " + e.getMessage());
        }
    }
}
