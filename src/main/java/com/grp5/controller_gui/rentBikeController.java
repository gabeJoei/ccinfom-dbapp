package com.grp5.controller_gui;

import com.grp5.dao.bikeRecordDAO;
import com.grp5.dao.branchRecordDAO;
import com.grp5.model.bikeRecordModel;
import com.grp5.model.branchRecordModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Rent Bike Controller with Sidebar Integration
 */
public class rentBikeController {

    // Sidebar
    @FXML
    private AnchorPane sidebar;
    private MainDashBoardUserController sidebarController;

    // Branch Selection
    @FXML
    private ChoiceBox<String> cmbBranch;

    // Mountain Bike
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

    @FXML
    public void initialize() {
        bikeDAO = new bikeRecordDAO();
        branchDAO = new branchRecordDAO();

        loadBranches();
    }

    /** Load all branches into the ChoiceBox */
    private void loadBranches() {
        // Assuming branchDAO and branchRecordModel are functional
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

        int branchID = branch.getBranchID();
        ArrayList<bikeRecordModel> bikes = bikeDAO.getBikesByBranch(branchID);

        int mountainCount = 0, roadCount = 0, eAssistsCount = 0;
        int tandemCount = 0, eBikeCount = 0, bmxCount = 0;

        bikeRecordModel mountainBike = null, roadBike = null, eAssistsBike = null;
        bikeRecordModel tandemBike = null, eBike = null, bmxBike = null;

        for (bikeRecordModel bike : bikes) {
            String model = bike.getBikeModel().toLowerCase();

            if (bike.getBikeAvailability()) {
                if (model.contains("mountain")) {
                    mountainCount++;
                    if (mountainBike == null)
                        mountainBike = bike;
                } else if (model.contains("road")) {
                    roadCount++;
                    if (roadBike == null)
                        roadBike = bike;
                } else if (model.contains("e-assist")) {
                    eAssistsCount++;
                    if (eAssistsBike == null)
                        eAssistsBike = bike;
                } else if (model.contains("tandem")) {
                    tandemCount++;
                    if (tandemBike == null)
                        tandemBike = bike;
                } else if (model.contains("e-bike") || model.contains("electric")) {
                    eBikeCount++;
                    if (eBike == null)
                        eBike = bike;
                } else if (model.contains("bmx")) {
                    bmxCount++;
                    if (bmxBike == null)
                        bmxBike = bike;
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
}