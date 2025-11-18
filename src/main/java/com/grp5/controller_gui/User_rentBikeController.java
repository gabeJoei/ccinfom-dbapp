package com.grp5.controller_gui;

import com.grp5.dao.bikeRecordDAO;
import com.grp5.dao.bikeReservationDAO;
import com.grp5.dao.branchRecordDAO;
import com.grp5.model.bikeRecordModel;
import com.grp5.model.branchRecordModel;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.grp5.dao.bikeReservationDAO;
import com.grp5.utils.generalUtilities;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the user's bike rental interface (User_rentBike.fxml).
 * This class manages displaying available bikes based on the selected branch,
 * checking their real-time availability against the reservation table,
 * and handling navigation to the bike reservation page.
 */
public class User_rentBikeController {

    // Branch Selection
    @FXML
    private ChoiceBox<String> cmbBranch;

    // Mountain Bike
    @FXML
    private ImageView imgMountainBike,imgTandemBike,imgRoadBike,imgEBike,imgEAssistBike,imgBMXBike;
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
    private bikeReservationDAO reservationDAO;

    private bikeRecordModel mountainBike;
    private bikeRecordModel roadBike;
    private bikeRecordModel eAssistsBike;
    private bikeRecordModel tandemBike;
    private bikeRecordModel eBike;
    private bikeRecordModel bmxBike;

    /**
     * Initializes the controller. This method is called automatically by the FXML loader. 
     * It sets up DAO instances, populates the branch selector, and applies hover/animation 
     * effects to the bike images.
     */
    @FXML
    public void initialize() {
        bikeDAO = new bikeRecordDAO();
        branchDAO = new branchRecordDAO();
        reservationDAO = new bikeReservationDAO();

        loadBranches();
        highlightEffect();
    }

    /**
     * Fetches all branches from the database via {@link branchRecordDAO} and populates the
     * branch {@code ChoiceBox}. It sets the first branch in the list as the
     * default selection and adds a listener to reload bikes when the selection changes.
     */
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

    /**
     * Loads and displays the bikes available for the currently selected branch.
     * This method performs the following steps:
     * 1. Resets all bike-type models (e.g., {@code mountainBike}) to null.
     * 2. Fetches all bikes for the selected branch from {@link bikeRecordDAO}.
     * 3. Checks the real-time availability of each bike by querying {@link bikeReservationDAO}
     * for any overlapping reservations within the next hour.
     * 4. Counts available bikes by model type (Mountain, Road, etc.) and stores a sample
     * of each type.
     * 5. Calls {@link #updateBikeDisplay} to update the FXML {@code Text} fields for each bike type.
     */
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

        // Get bikes
        ArrayList<bikeRecordModel> bikes = bikeDAO.getBikesByBranch(branchID);
        
        // Get the current time and time one hour from current time
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp timeAfterOneHour = Timestamp.valueOf(LocalDateTime.now().plusHours(1));

        int mountainCount = 0, roadCount = 0, eAssistsCount = 0;
        int tandemCount = 0, eBikeCount = 0, bmxCount = 0;

        for (bikeRecordModel bike : bikes) {
            String model = bike.getBikeModel().toLowerCase();

            // Check if bike is being used
            boolean isCurrentlyReserved = reservationDAO.hasOverlappingReservation(
                bike.getBikeID(), now, timeAfterOneHour
            );

            if (!(isCurrentlyReserved)&&bike.getBikeAvailability()==true) {
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

    /**
     * Helper method to update the FXML {@code Text} nodes for a single bike display card.
     * @param bike        The {@link bikeRecordModel} for the bike type (e.g., Mountain bike).
     * @param count       The number of available bikes of this type.
     * @param modelText   The {@code Text} node to display the bike's model.
     * @param availableText The {@code Text} node to display the available count.
     * @param costText    The {@code Text} node to display the rental cost.
     */
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
                generalUtilities.showAlert(Alert.AlertType.ERROR, "Error", "Error in loading reservation page.");
            }

        } else {
            System.out.println("No bike data available for the selected image.");
            generalUtilities.showAlert(Alert.AlertType.WARNING, "No bike data available for the selected model.", "Select a different bike.");
        }
    }
    
    private void highlightEffect(){
        ImageView[] bImages={imgMountainBike,imgTandemBike,imgRoadBike,imgEBike,imgEAssistBike,imgBMXBike};
        for(ImageView b:bImages){
            ScaleTransition scaleTrans=new ScaleTransition(Duration.millis(500),b);
            FadeTransition fadeTrans=new FadeTransition(Duration.millis(500),b);
            b.setOnMouseEntered(e->{
                scaleTrans.stop();
                scaleTrans.setToX(1.2);
                scaleTrans.setToY(1.2);
                scaleTrans.play();

                fadeTrans.stop();
                fadeTrans.setToValue(0.8);
                fadeTrans.play();
            });

            b.setOnMouseExited(e->{
                scaleTrans.stop();
                scaleTrans.setToX(1.0);
                scaleTrans.setToY(1.0);
                scaleTrans.play();

                fadeTrans.stop();
                fadeTrans.setToValue(1.0);
                fadeTrans.play();
            });
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
            reserveController.initData(userID, bikeToLoad);

            // Replace the anchorPane (content) of dashboard
            dashboardContentArea.getChildren().setAll(newPane);

        } else {
            System.err.println("Error in finding contentArea");
        }
    }
}