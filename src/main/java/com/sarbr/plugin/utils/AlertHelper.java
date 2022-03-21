package com.sarbr.plugin.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class AlertHelper {

    public static void defAlter(String contentText) {
        showAlert(Alert.AlertType.INFORMATION ,"通知", contentText);
    }

    public static void defAlter(String title, String contentText) {
        showAlert(Alert.AlertType.INFORMATION ,title, contentText);
    }

    public static void errAlter(String contentText) {
        showAlert(Alert.AlertType.ERROR ,"错误", contentText);
    }

    public static void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        // 点 x 退出
        Window window = alert.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> window.hide());
        alert.getDialogPane().setGraphic(new ImageView());
        alert.initStyle(StageStyle.UTILITY);
        alert.setGraphic(new ImageView());
        alert.show();
    }

    public static void openWindow(String title, int width, int height, Parent root) {
        Stage secondStage = new Stage();
        secondStage.setOnCloseRequest(event -> secondStage.close());
        secondStage.setTitle(title);
        secondStage.setWidth(width);
        secondStage.setHeight(height);
        secondStage.setScene(new Scene(root));
        secondStage.show();
    }
}
