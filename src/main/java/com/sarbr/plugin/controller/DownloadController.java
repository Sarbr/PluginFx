package com.sarbr.plugin.controller;

import com.sarbr.plugin.app.fastDownload.download.Download;
import com.sarbr.plugin.app.fastDownload.download.MultiDownload;
import com.sarbr.plugin.utils.AlertHelper;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

@FXMLController
public class DownloadController {

    public TextField downUrl;
    public TextField threads;
    public Label loading;

    public void chooseDirectory(ActionEvent actionEvent) {
        final Button source = (Button)actionEvent.getSource();
        DirectoryChooser file=new DirectoryChooser();
        file.setTitle("下载路径");
        Window window = source.getScene().getWindow();
        File newFolder = file.showDialog(window);//这个file就是选择的文件夹了
        if(this.validate()){
            return;
        }
        this.down(newFolder.getAbsolutePath());
    }

    private boolean validate(){
        if(threads.getText().isEmpty()) {
            AlertHelper.errAlter("请输入线程数");
            return true;
        }
        if(downUrl.getText().isEmpty()) {
            AlertHelper.errAlter("请输入下载链接");
            return true;
        }
        return false;
    }

    private void down(String fileUrl) {
        Download downloader = new MultiDownload(Integer.parseInt(threads.getText()));
        try {
            loading.setTextFill(Color.rgb( 160, 255, 145));
            loading.setText(downloader.download(downUrl.getText(), fileUrl));
        } catch (IOException e) {
            AlertHelper.errAlter(e.getMessage());
        }
    }
}