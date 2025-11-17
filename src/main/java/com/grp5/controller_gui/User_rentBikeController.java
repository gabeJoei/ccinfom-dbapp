package com.grp5.controller_gui;

import com.grp5.dao.bikeRecordDAO;
import com.grp5.dao.branchRecordDAO;
import com.grp5.model.bikeRecordModel;
import com.grp5.model.branchRecordModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Rent Bike Controller with Sidebar Integration
 */
public class User_rentBikeController {

    // Branch Selection
    @FXML
    private ChoiceBox<String> cmbBranch;

    // Mountain Bike
    @FXML
    private ImageView imgMountainBike;
    @FXML
    private Text txtMountainModel;
    @FXML
    private Text txtMountainAvailable;
    @FXML
    private Text txtMountainCost;

    // Road Bike
    @FXML
    private Text txtRoadModel;
    @FXML
    private Text txtRoadAvailable;
    @FXML
    private Text txtRoadCost;

    // E-Assists Bike
    @FXML
    private Text txtEAssistsModel;
    @FXML
    private Text txtEAssistsAvailable;
    @FXML
    private Text txtEAssistsCost;

    // Tandem Bike
    @FXML
    private Text txtTandemModel;
    @FXML
    private Text txtTandemAvailable;
    @FXML
    private Text txtTandemCost;

    // E-Bike
    @FXML
    private Text txtEBikeModel;
    @FXML
    private Text txtEBikeAvailable;
    @FXML
    private Text txtEBikeCost;

    // BMX Bike
    @FXML
    private Text txtBMXModel;
    @FXML
    private Text txtBMXAvailable;
    @FXML
    private Text txtBMXCost;

    private bikeRecordDAO bikeDAO;
    private branchRecordDAO branchDAO;

    private bikeRecordModel mountainBike;
    private bikeRecordModel roadBike;
    private bikeRecordModel eAssistsBike;
    private bikeRecordModel tandemBike;
    private bikeRecordModel eBike;
    private bikeRecordModel bmxBike;

    @FXML
    public void initialize() {
        bikeDAO = new bikeRecordDAO();
        branchDAO = new branchRecordDAO();

        loadBranches();
    }

    /** Load all branches into the ChoiceBox */
    private void loadBranches() {
        ArrayList<branchRecordModel> branches = branchDAO.getAllBranch();

        for (branchRecordModel branch : branches) {
            cmbBranch.getItems().add(branch.getBranchName());
        }

        if (!branches.isEmpty()) {
            cmbBranch.setValue(branches.get(0).getBranchName());
            loadBikesForBranch();
        }

        cmbBranch.setOnAction(e -> loadBikesForBranch());
    }

    // Load bikes for the selected branch
    private void loadBikesForBranch() {
        String selectedBranch = cmbBranch.getValue();
        if (selectedBranch == null)
            return;

        branchRecordModel branch = branchDAO.getBranch(selectedBranch);
        if (branch == null)
            return;

        // Reset all bike references when loading a new branch
        this.mountainBike = null;
        this.roadBike = null;
        this.eAssistsBike = null;
        this.tandemBike = null;
        this.eBike = null;
        this.bmxBike = null;

        int branchID = branch.getBranchID();
        ArrayList<bikeRecordModel> bikes = bikeDAO.getBikesByBranch(branchID);

        int mountainCount = 0, roadCount = 0, eAssistsCount = 0;
        int tandemCount = 0, eBikeCount = 0, bmxCount = 0;

        for (bikeRecordModel bike : bikes) {
            String model = bike.getBikeModel().toLowerCase();

            if (bike.getBikeAvailability()) {
                if (model.contains("mountain")) {
                    mountainCount++;
                    if (mountainBike == null)
                        this.mountainBike = bike;
                } else if (model.contains("road")) {
                    roadCount++;
                    if (roadBike == null)
                        this.roadBike = bike;
                } else if (model.contains("e-assist")) {
                    eAssistsCount++;
                    if (eAssistsBike == null)
                        this.eAssistsBike = bike;
                } else if (model.contains("tandem")) {
                    tandemCount++;
                    if (tandemBike == null)
                        this.tandemBike = bike;
                } else if (model.contains("e-bike") || model.contains("electric")) {
                    eBikeCount++;
                    if (eBike == null)
                        this.eBike = bike;
                } else if (model.contains("bmx")) {
                    bmxCount++;
                    if (bmxBike == null)
                        this.bmxBike = bike;
                }
            }
        }

        updateBikeDisplay(mountainBike, mountainCount, txtMountainModel, txtMountainAvailable, txtMountainCost);
        updateBikeDisplay(roadBike, roadCount, txtRoadModel, txtRoadAvailable, txtRoadCost);
        updateBikeDisplay(eAssistsBike, eAssistsCount, txtEAssistsModel, txtEAssistsAvailable, txtEAssistsCost);
        updateBikeDisplay(tandemBike, tandemCount, txtTandemModel, txtTandemAvailable, txtTandemCost);
        updateBikeDisplay(eBike, eBikeCount, txtEBikeModel, txtEBikeAvailable, txtEBikeCost);
        updateBikeDisplay(bmxBike, bmxCount, txtBMXModel, txtBMXAvailable, txtBMXCost);
    }

    /** Update individual bike display */
    private void updateBikeDisplay(bikeRecordModel bike, int count,
            Text modelText, Text availableText, Text costText) {
        if (bike != null) {
            modelText.setText("Bike Model: " + bike.getBikeModel());
            availableText.setText("Amount Of Available Bikes: " + count);
            costText.setText(String.format("Rental Cost: ₱%.2f/day", bike.getDailyRate()));
        } else {
            modelText.setText("Bike Model: Not Available");
            availableText.setText("Amount Of Available Bikes: 0");
            costText.setText("Rental Cost: N/A");
        }
    }

    @FXML
    protected void onBikeImageClick(MouseEvent event) {
        
        // Get User ID
        Text userIdText = (Text) imgMountainBike.getScene().lookup("#txtUserID");
        String userID = userIdText.getText();

        // Get the ImageView 
        ImageView clickedImage = (ImageView) event.getSource();
        
        // Get the fx:id of image
        String imageId = clickedImage.getId();
        
        // Find Bike details
        bikeRecordModel selectedBike = null;
        switch (imageId) {
            case "imgMountainBike" -> selectedBike = this.mountainBike;
            case "imgRoadBike" -> selectedBike = this.roadBike;
            case "imgEAssistBike" -> selectedBike = this.eAssistsBike;
            case "imgTandemBike" -> selectedBike = this.tandemBike;
            case "imgEBike" -> selectedBike = this.eBike;
            case "imgBMXBike" -> selectedBike = this.bmxBike;
            default -> {
                System.err.println("Clicked on an unhandled image: " + imageId);
                return; // Exit if user clicked on different image
            }
        }

        // Check if bike exists and load the page
        if (selectedBike != null) {
            System.out.println(selectedBike.getBikeModel() + " was clicked.");

            try {
                loadBikeReservationPage(userID, selectedBike);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error in loading reservation page.");
                // alert
            }

        } else {
            System.out.println("No bike data available for the selected image.");
            // alert
        }
    }
    
    private void loadBikeReservationPage(String userID, bikeRecordModel bikeToLoad) throws IOException {
        // Use any FXML element (imgMountainBike) to get the scene
        AnchorPane dashboardContentArea = (AnchorPane) imgMountainBike.getScene().lookup("#contentArea");
        if (dashboardContentArea != null) {
            
            // Load the new FXML file and controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/User_reserveBike.fxml"));
            AnchorPane newPane = loader.load();
            User_reserveBikeController reserveController = loader.getController();

            // Pass bike data to the new controller
            reserveController.initializePage();
            reserveController.initData(userID, bikeToLoad);

            // Replace the anchorPane (content) of dashboard
            dashboardContentArea.getChildren().setAll(newPane);

        } else {
            System.err.println("Error in finding contentArea");
        }
    }
}