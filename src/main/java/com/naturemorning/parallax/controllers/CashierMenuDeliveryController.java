/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naturemorning.parallax.controllers;

import com.naturemorning.parallax.config.StageManager;
import com.naturemorning.parallax.models.Delivery;
import com.naturemorning.parallax.models.Reservation;
import com.naturemorning.parallax.views.FxmlView;
import com.naturemorning.parallax.services.impl.DeliveryService;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class CashierMenuDeliveryController implements Initializable {

    @FXML
    private Label customerId;
    @FXML
    private TextField customerName;
    @FXML
    private TextField customerAddress;
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private TableView<Delivery> deliveryTable;
    @FXML
    private TableColumn<Delivery, Long> colID;
    @FXML
    private TableColumn<Delivery, String> colName;
    @FXML
    private TableColumn<Delivery, String> colAddress;

    @FXML
    private TableColumn<Delivery, Boolean> colEdit;
    @FXML
    private Button reset;
    @FXML
    private Button delete;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private DeliveryService deliveryService;

    private ObservableList<Delivery> deliveryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deliveryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();
        loadUserDetails();
    }

    @FXML
    private void cancel(ActionEvent event) {
        stageManager.switchScene(FxmlView.CASHIER);
    }

    @FXML
    private void save(ActionEvent event) {

        if (validate("Customer Name", getCustomerName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Customer Address", getCustomerAddress(), "([a-zA-Z]{3,30}\\s*)+")) {
            if (customerId.getText() == null || "".equals(customerId.getText())) {
                if (true) {

                    Delivery user = new Delivery();
                    user.setCustomerName(getCustomerName());
                    user.setCustomerAddress(getCustomerAddress());

                    Delivery newUser = deliveryService.save(user);

                    saveAlert(newUser);
                }

            } else {
                Delivery user = deliveryService.find(Long.parseLong(customerId.getText()));
                user.setCustomerName(getCustomerName());
                user.setCustomerAddress(getCustomerAddress());

                Delivery updatedUser = deliveryService.update(user);
                updateAlert(updatedUser);
            }

            clearFields();
            loadUserDetails();
        }

    }

    private void saveAlert(Delivery user) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user " + user.getCustomerName() + " " + user.getCustomerAddress() + " has been created and \n" + " id is " + user.getId() + ".");
        alert.showAndWait();
        loadUserDetails();
    }

    private void clearFields() {
        customerName.clear();
        customerAddress.clear();

    }

    private void updateAlert(Delivery user) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user " + user.getCustomerName() + " " + user.getCustomerAddress() + " has been updated.");
        alert.showAndWait();
    }

    @FXML
    private void reset(ActionEvent event) {
        customerName.clear();
        customerAddress.clear();
    }

    @FXML
    void delete(ActionEvent event) {
        List<Delivery> users = deliveryTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected user?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            deliveryService.deleteInBatch(users);
        }

        loadUserDetails();
    }

    public String getCustomerName() {
        return customerName.getText();
    }

    public String getCustomerAddress() {
        return customerAddress.getText();
    }

    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please Select " + field);
        } else {
            if (empty) {
                alert.setContentText("Please Enter " + field);
            } else {
                alert.setContentText("Please Enter Valid " + field);
            }
        }
        alert.showAndWait();
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void setColumnProperties() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colEdit.setCellFactory(cellFactory);

    }
    Callback<TableColumn<Delivery, Boolean>, TableCell<Delivery, Boolean>> cellFactory
            = new Callback<TableColumn<Delivery, Boolean>, TableCell<Delivery, Boolean>>() {
        @Override
        public TableCell<Delivery, Boolean> call(final TableColumn<Delivery, Boolean> param) {
            final TableCell<Delivery, Boolean> cell = new TableCell<Delivery, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            Delivery user = getTableView().getItems().get(getIndex());
                            updateUser(user);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }
                  private void updateUser(Delivery user) {
                    customerId.setText(Long.toString(user.getId()));
                    customerName.setText(user.getCustomerName());
                    customerAddress.setText(user.getCustomerAddress());
                    
                }
            };
            return cell;
        }
    };

    private void loadUserDetails() {
        deliveryList.clear();
        deliveryList.addAll(deliveryService.findAll());
        deliveryTable.setItems(deliveryList);
    }
}
