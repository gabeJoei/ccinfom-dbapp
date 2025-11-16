package com.grp5.controller_gui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuItem;

/**
 * Controller for Main Dashboard Sidebar (included via fx:include)
 */

public class mainDashboardAdminControl {

    @FXML private Button btnBranch;
    @FXML private Button btnBike;
    @FXML private Button btnReservation;
    @FXML private Button btnTransaction;
    @FXML private Button btnUsers;
    @FXML private Button btnProfile;
    @FXML private Button btnLogOut;
    @FXML private AnchorPane contentArea;
    @FXML private SplitMenuButton smbViewReports;
    @FXML private MenuItem iCustomer,iBike,iSales,iBranch;
    @FXML
    public void initialize() {
        setupSidebarButtons();
    }

    private void setupSidebarButtons() {

        iBike.setText("Rental Bike Trend");
        iCustomer.setText("Customer Engagement Report");
        iBranch.setText("Branch Performance");
        iSales.setText("Rental Sales");
        smbViewReports.getItems().addAll(iBike,iCustomer,iBranch,iSales);

        btnProfile.setOnAction(e -> handleProfile());
        btnBranch.setOnAction(e -> handleBranch());
        btnTransaction.setOnAction(e -> handleReservations());
        btnTransaction.setOnAction(e -> handleTransactions());
        btnUsers.setOnAction(e->handleUsers());
        btnLogOut.setOnAction(e->handleLogout());
        btnBike.setOnAction(e->handleBike());
        iBike.setOnAction(e->handleBikeReport());
        iCustomer.setOnAction(e->handleCustomerReport());
        iBranch.setOnAction(e->handleBranchReport());
        iSales.setOnAction(e->handleSalesReport());
    }
    
    private void loadUI(String fxmlPath) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void handleBikeReport(){
     loadUI("/com/grp5/view/adminRentalBikeTrendReport.fxml");
   }

   public void handleBranchReport(){
     loadUI("/com/grp5/view/adminReportBranch.fxml");
   }
   public void handleCustomerReport(){
     loadUI("/com/grp5/view/adminCustomerReport.fxml");
   }
   public void handleSalesReport(){
     loadUI("/com/grp5/view/adminRentalSalesReport.fxml");
   }
    

    private void handleBike(){
        loadUI("/com/grp5/view/adminBike.fxml");
    }
    private void handleUsers(){
        loadUI("/com/grp5/view/adminCustomers.fxml");
    }
    private void handleProfile() {
        loadUI("/com/grp5/view/Settings.fxml");
    }

    private void handleReservations() {
        loadUI("/com/grp5/view/adminReservation.fxml");
    }

    private void handleTransactions() {
        loadUI("/com/grp5/view/adminTransaction.fxml");
    }

    private void handleBranch() {
        loadUI("/com/grp5/view/adminBranch.fxml");
    }

    private void handleLogout() {
        System.out.println("Log Out clicked");
    }
    public Button getBtnUsers(){
        return btnUsers;
    }
    public Button getBtnBike(){
        return btnBike;
    }
    public Button getBtnBranch(){
        return btnBranch;
    }
    public Button getBtnProfile() {
        return btnProfile;
    }
    public Button getBtnReservations() {
        return btnReservation;
    }
    public Button getBtnTransaction() {
        return btnTransaction;
    }
    public Button getBtnLogout() {
        return btnLogOut;
    }


}
  