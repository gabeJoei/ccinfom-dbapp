/**
 * Data Access Object (DAO) for managing admin records.
 * 
 * This class handles all CRUD operations for the admin database table,
 * including inserting, retrieving, updating, deleting customer records.
 *
 */
package com.grp5.dao;

import com.grp5.model.adminModel;
import com.grp5.utils.databaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class adminDAO {

    /**
     * Authenticate admin by username and password
     */
    public adminModel authenticateAdmin(String username, String password) { // FIX: Changed return type
        String sql = "SELECT * FROM admin WHERE adminUsername = ? AND adminPassword = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("admin authentication error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get admin by ID
     */
    public adminModel getAdmin(int adminID) { // FIX: Changed return type
        String sql = "SELECT * FROM admin WHERE adminID = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adminID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all admins
     */
    public ArrayList<adminModel> getAllAdmins() { // FIX: Changed return type
        ArrayList<adminModel> admins = new ArrayList<>(); // FIX: Changed ArrayList type
        String sql = "SELECT * FROM admin ORDER BY adminLastName";

        try (Connection conn = databaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                admins.add(extractAdminFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    /**
     * Add new admin
     */
    public boolean addAdmin(adminModel admin) {
        String sql = "INSERT INTO admin (adminUsername, adminPassword, adminEmail, " +
                "adminFirstName, adminLastName) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admin.getAdminUsername());
            pstmt.setString(2, admin.getAdminPassword());
            pstmt.setString(3, admin.getAdminEmail());
            pstmt.setString(4, admin.getAdminFirstName());
            pstmt.setString(5, admin.getAdminLastName());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update admin
     */
    public boolean updateAdmin(adminModel admin) {
        String sql = "UPDATE admin SET adminUsername = ?, adminPassword = ?, " +
                "adminEmail = ?, adminFirstName = ?, adminLastName = ? " +
                "WHERE adminID = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admin.getAdminUsername());
            pstmt.setString(2, admin.getAdminPassword());
            pstmt.setString(3, admin.getAdminEmail());
            pstmt.setString(4, admin.getAdminFirstName());
            pstmt.setString(5, admin.getAdminLastName());
            pstmt.setInt(6, admin.getAdminID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete admin
     */
    public boolean deleteAdmin(int adminID) {
        String sql = "DELETE FROM admin WHERE adminID = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adminID);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update admin password (not really a secure one, but oh well)
    public boolean updateAdminPassword(int adminID, String newPass) {
        String sql = "UPDATE admin SET adminPassword=? WHERE adminID=?";
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPass);
            pstmt.setInt(2, adminID);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract admin object from ResultSet
     */
    private adminModel extractAdminFromResultSet(ResultSet rs) throws SQLException { // FIX: Changed return type
        adminModel admin = new adminModel(); // FIX: Variable was declared
        admin.setAdminID(rs.getInt("adminID"));
        admin.setAdminUsername(rs.getString("adminUsername"));
        admin.setAdminPassword(rs.getString("adminPassword"));
        admin.setAdminEmail(rs.getString("adminEmail"));
        admin.setAdminFirstName(rs.getString("adminFirstName"));
        admin.setAdminLastName(rs.getString("adminLastName"));
        return admin;
    }
}