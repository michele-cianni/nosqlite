package com.github.michele.cianni.nosqlite.core.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class NoSQLiteGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/NoSQLiteGUI.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("NoSQL Database GUI");
        stage.show();
    }

    public static void launchGUI() {
        launch(NoSQLiteGUI.class);
    }
}
