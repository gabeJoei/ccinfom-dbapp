package com.grp5.controller_gui;

import com.grp5.dao.branchRecordDAO;
import com.grp5.model.bikeRecordModel;
import com.grp5.model.branchRecordModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class BranchController {

    @FXML
    private TextField branchIDTextField;

    @FXML
    private Button enterButton;

    @FXML
    private TableView<branchRecordModel> branchTableView;

    @FXML
    private TableColumn<branchRecordModel, Integer> branchIDColumn;

    @FXML
    private TableColumn<branchRecordModel, String> branchNameColumn;

    @FXML
    private TableColumn<branchRecordModel, String> branchAddressColumn;

    @FXML
    private Button updateButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button deleteButton;

    private branchRecordDAO branchDAO = new branchRecordDAO();
    private ObservableList<branchRecordModel> branchList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the cell value factories for the TableView columns
        branchIDColumn.setCellValueFactory(new PropertyValueFactory<>("branchID"));
        branchNameColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        branchAddressColumn.setCellValueFactory(new PropertyValueFactory<>("branchAddress"));

        loadBranchData();
    }

    private void loadBranchData() {
        ArrayList<branchRecordModel> allBranches = branchDAO.getAllBranch();

        branchList.clear();
        branchList.addAll(allBranches);

        branchTableView.setItems(branchList);
    }

    @FXML
    void handleAddButtonAction(ActionEvent event) {
        Dialog<branchRecordModel> dialog = new Dialog<>();
        dialog.setTitle("Add New Branch");
        dialog.setHeaderText("Enter details for the new branch record");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField branchName = new TextField();
        branchName.setPromptText("Branch Name");

        TextField branchAddress = new TextField();
        branchAddress.setPromptText("Branch Address");

        grid.add(new Label("Branch:"), 0, 0);
        grid.add(branchName, 1, 0);
        grid.add(new Label("Branch Address:"), 0, 1);
        grid.add(branchAddress, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Button btnAdd = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        btnAdd.addEventFilter(ActionEvent.ACTION, ae -> {
            String nameString = branchName.getText().trim();
            String addressString = branchAddress.getText().trim();

            // Check for empty fields
            if (nameString.isEmpty() || addressString.isEmpty()) {
                ae.consume(); // Stop the dialog from closing
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields before adding.");
                return;
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new branchRecordModel(branchName.getText().trim(), branchAddress.getText().trim());
            }
            return null;
        });

        Optional<branchRecordModel> result = dialog.showAndWait();

        result.ifPresent(newBranch -> {
            int rowsInserted = branchDAO.addBranchRecordData(newBranch);
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "New branch added successfully!");
                loadBranchData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add branch record.");
            }
        });
    }

    @FXML
    void handleEnterButtonAction(ActionEvent event) {
        try {
            String idText = branchIDTextField.getText().trim();
            if (idText.isEmpty()) {
                loadBranchData();
                return;
            }

            int branchID = Integer.parseInt(idText);
            branchRecordModel branch = branchDAO.getBranchRecordData(branchID);

            branchList.clear();
            if (branch != null) {
                branchList.add(branch);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Search Result",
                        "No branch found with ID: " + branchID);
                loadBranchData();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric Branch ID.");
        }
        branchIDTextField.clear();
    }

    @FXML
    void handleUpdateButtonAction(ActionEvent event) {
        branchRecordModel selectedBranch = branchTableView.getSelectionModel().getSelectedItem();

        if (selectedBranch == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a branch record to update.");
            return;
        }

        Dialog<branchRecordModel> dialog = new Dialog<>();
        dialog.setTitle("Update Branch Record");
        dialog.setHeaderText("Update Branch ID: " + selectedBranch.getBranchID());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtName = new TextField(selectedBranch.getBranchName());
        TextField txtAddress = new TextField(selectedBranch.getBranchAddress());

        grid.add(new Label("Branch Name:"), 0, 0);
        grid.add(txtName, 1, 0);
        grid.add(new Label("Branch Address:"), 0, 1);
        grid.add(txtAddress, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType btnUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnUpdate, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnUpdate) {
                selectedBranch.setBranchName(txtName.getText().trim());
                selectedBranch.setBranchAddress(txtAddress.getText().trim());

                return selectedBranch;
            }
            return null;
        });

        Optional<branchRecordModel> result = dialog.showAndWait();
        result.ifPresent(branch -> {
            if (!branch.getBranchName().isEmpty() && !branch.getBranchAddress().isEmpty()
                    && branchDAO.updateBranchRecord(branch)) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Branch record updated successfully!");
                loadBranchData(); // Reload data to show the changes in the TableView
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update branch record. Please ensure all fields are valid.");
            }
        });
    }

    @FXML
    void handleViewDetailsButtonAction(ActionEvent event) {
        branchRecordModel selectedBranch = branchTableView.getSelectionModel().getSelectedItem();
        if (selectedBranch != null) {
            String details = String.format(
                    "ID: %d\nName: %s\nAddress: %s",
                    selectedBranch.getBranchID(),
                    selectedBranch.getBranchName(),
                    selectedBranch.getBranchAddress());
            showAlert(Alert.AlertType.INFORMATION, "Branch Details", details);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a branch record to view details.");
        }
    }

    @FXML
    void handleDeleteButtonAction(ActionEvent event) {
        branchRecordModel selectedBranch = branchTableView.getSelectionModel().getSelectedItem();
        if (selectedBranch != null) {
            int result = branchDAO.delBranchRecordData(selectedBranch.getBranchID());
            if (result == 1) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Branch record deleted successfully (including all dependent data).");
                loadBranchData();
            } else {

                showAlert(Alert.AlertType.ERROR, "Failure",
                        "Failed to delete branch record. See console output for detailed database error.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a branch record to delete.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}