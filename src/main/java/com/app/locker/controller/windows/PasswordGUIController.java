package com.app.locker.controller.windows;

import com.app.locker.controller.popups.AddItemPopupController;
import com.app.locker.controller.popups.DeleteItemPopupController;
import com.app.locker.model.Entry;
import com.app.locker.utils.classes.DBConnector;
import com.app.locker.utils.interfaces.TableEventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Objects;

public class PasswordGUIController implements TableEventListener {

    @FXML AnchorPane anchorPane;
    @FXML TableView<Entry> table;
    @FXML TableColumn<Entry, String> colService;
    @FXML TableColumn<Entry, String> colUsername;
    @FXML TableColumn<Entry, String> colPassword;
    @FXML TableColumn<Entry, String> colEmail;
    @FXML TableColumn<Entry, String> colCreated;
    @FXML Button btnHidden;

    public static ObservableList<Entry> entries;
    private DBConnector dbConnector;

    @FXML
    public void initialize(){
        dbConnector = new DBConnector();
        try{
            dbConnector.setConnectionWithoutCreate();
        }catch (SQLException e){
            System.out.println("Error accessing database!");
            new Alert(Alert.AlertType.ERROR, "Database corrupted!").showAndWait();
            System.exit(1);
        }
        entries = FXCollections.observableArrayList();
        colService.setCellValueFactory(new PropertyValueFactory<Entry, String>("service"));
        colUsername.setCellValueFactory(new PropertyValueFactory<Entry, String>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Entry, String>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Entry, String>("email"));
        colCreated.setCellValueFactory(new PropertyValueFactory<Entry, String>("created"));
        table.setItems(entries);
    }

    @FXML
    public void onAddClicked(){
        anchorPane.setDisable(true);
        Parent root = null;
        try{
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/layouts/popups/AddItemPopup.fxml")));
        }catch (IOException e){
            System.out.println("Resource missing: AddItemPopup.fxml");
            System.exit(1);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add item");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            anchorPane.setDisable(false);
        });
        stage.show();
        AddItemPopupController.addTableEventListener(this);
    }

    @FXML
    public void onEditClicked(){
    }

    @FXML
    public void onDeleteClicked(){
        anchorPane.setDisable(true);
        Parent root = null;
        try{
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/layouts/popups/DeleteItemPopup.fxml")));
        }catch (IOException e){
            System.out.println("Resource missing: DeleteItemPopup.fxml");
            System.exit(1);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Delete item");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            anchorPane.setDisable(false);
        });
        stage.show();
        DeleteItemPopupController.addTableEventListener(this);
    }

    @FXML
    public void onDeleteAllClicked(){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? This can't be undone!", ButtonType.YES, ButtonType.CANCEL);
        a.showAndWait();
        if (a.getResult() == ButtonType.YES) {
            try {
                dbConnector.deleteAllEntries();
            } catch (SQLException e) {
                System.out.println("Error accessing/writing to database!");
            }
        }
        entries.clear();
    }

    @FXML
    public void onHiddenClicked(){
        entries.clear();
        ArrayList<Entry> existingEntries = null;
        try {
            existingEntries = dbConnector.getExistingData();
        } catch (SQLException e) {
            System.out.println("Error accessing/writing to database!");
            new Alert(Alert.AlertType.ERROR, "Database corrupted!").showAndWait();
            System.exit(1);
        }
        if (existingEntries == null) {
            new Alert(Alert.AlertType.ERROR, "Database corrupted!").showAndWait();
            System.exit(1);
            return;
        }
        entries.addAll(existingEntries);
    }

    @Override
    public void onItemAdded(Entry entry) {
        try{
            dbConnector.addData(entry);
            entries.add(entry);
        }
        catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Duplicate field value detected!");
            new Alert(Alert.AlertType.ERROR, "An entry with this service name already exists!").showAndWait();
        }
        catch (SQLException e){
            System.out.println("Error accessing/writing to database");
            new Alert(Alert.AlertType.ERROR, "Database corrupted!").showAndWait();
            System.exit(1);
        }
    }

    @Override
    public void onItemEdited(Entry entry) {
        System.out.println(entry.toString());
    }

    @Override
    public void onItemDeleted(Entry entry) {
        try{
            dbConnector.deleteEntry(entry);
            entries.remove(entry);
        }catch (SQLException e){
            System.out.println("Error accessing/writing to database!");
            new Alert(Alert.AlertType.ERROR, "Database corrupted!").showAndWait();
            System.exit(1);
        }
    }
}