package com.app.locker.controller.popups;

import com.app.locker.controller.windows.PasswordGUIController;
import com.app.locker.model.Entry;
import com.app.locker.utils.classes.core.AppProperties;
import com.app.locker.utils.classes.logic.View;
import com.app.locker.utils.classes.ui.animation.ButtonHoverAnimation;
import com.app.locker.utils.classes.ui.animation.TextFieldAnimation;
import com.app.locker.utils.interfaces.EventListener;
import com.app.locker.utils.interfaces.TableEventListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Date;

public class AddItemPopupController {

    @FXML TextField txtService;
    @FXML TextField txtUsername;
    @FXML TextField txtPassword;
    @FXML TextField txtEmail;
    @FXML Button btnAdd;

    private static TableEventListener tableEventListener = null;

    public static void addTableEventListener(TableEventListener tableEventListener){
        AddItemPopupController.tableEventListener = tableEventListener;
    }

    @FXML
    public void initialize(){
        new TextFieldAnimation(txtEmail);
        new TextFieldAnimation(txtUsername);
        new TextFieldAnimation(txtPassword);
        new TextFieldAnimation(txtService);
        new ButtonHoverAnimation(btnAdd);
    }

    @FXML
    public void onAddClicked(){
        String service = txtService.getText().trim();
        String password = txtPassword.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        if(service.equals("")){
            new Alert(Alert.AlertType.ERROR, "The service can't be empty!").showAndWait();
            return;
        }
        if(password.equals("")){
            new Alert(Alert.AlertType.ERROR, "Password can't be empty!").showAndWait();
            return;
        }
        if(email.equals("")){
            new Alert(Alert.AlertType.ERROR, "The email can't be empty!").showAndWait();
            return;
        }
        if (username.equals(""))
            username = "None";
        String created = new Date().toString();
        Entry entry = new Entry(service, username, password, email, created);
        tableEventListener.onItemAdded(entry);
        txtEmail.setText("");
        txtPassword.setText("");
        txtService.setText("");
        txtUsername.setText("");
    }

    @FXML
    public void onEscapePressed(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ESCAPE)
            tableEventListener.onPopupCloseRequested();
    }

    @FXML
    public void onEnterClicked(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ENTER)
            onAddClicked();
    }

}
