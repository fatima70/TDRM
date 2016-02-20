package com.certis.oil.filecrawler;

import java.io.File;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class FileCrawlerWindows extends Application {

	private String csvInputFileName = "";
	private String networkDirectory = "";
	private String titlesFileName = "";
	private String tagsFileName = "";
	private String wellsFileName = "";
	
	public static void launchApp(String args[]) {
		FileCrawlerWindows.launch(args);
	}

	/**
	 * Create scanner UI and show it.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		final FileChooser fileChooser = new FileChooser();
		primaryStage.setTitle("Certis Inc - Oil Well Document Crawler");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Choose the following parameters");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		//CSV input file label, text field and file browse button.
		Label csvInputLabel = new Label("Select CSV input file:");
		grid.add(csvInputLabel, 0, 1);

		TextField csvInputField = new TextField();
		csvInputField.setMinSize(300, 20);
		csvInputField.setEditable(false);
		grid.add(csvInputField, 0, 2);

		Button csvInputBtn = new Button("Browse");
		HBox csvInputBox = new HBox(50);
		csvInputBox.setAlignment(Pos.BOTTOM_RIGHT);
		csvInputBox.getChildren().add(csvInputBtn);
		grid.add(csvInputBox, 1, 2);

		csvInputBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					csvInputFileName = file.getAbsolutePath();
					csvInputField.setText(getFileNameForField(csvInputFileName));
				} else {
					//
				}
			}
		});

		//Network location
		Label networkLabel = new Label("Select network location of files:");
		grid.add(networkLabel, 0, 3);

		TextField networkField = new TextField();
		networkField.setMinSize(300, 20);
		networkField.setEditable(false);
		grid.add(networkField, 0, 4);

		Button networkBtn = new Button("Browse");
		HBox networkBox = new HBox(50);
		networkBox.setAlignment(Pos.BOTTOM_RIGHT);
		networkBox.getChildren().add(networkBtn);
		grid.add(networkBox, 1, 4);
		
		networkBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				final DirectoryChooser directoryChooser =
		                new DirectoryChooser();
		        final File selectedDirectory =
		                    directoryChooser.showDialog(primaryStage);
				if (selectedDirectory  != null) {
					networkDirectory = selectedDirectory .getAbsolutePath();
					networkField.setText(getFileNameForField(networkDirectory));						
				} else {
					//
				}
			}
		});
		
		//Title list file label, text field and browse button.
		Label titlesLabel = new Label("Select title list file:");
		grid.add(titlesLabel, 0, 5);

		TextField titlesField = new TextField();
		titlesField.setMinSize(300, 20);
		titlesField.setEditable(false);
		grid.add(titlesField, 0, 6);

		Button titlesBtn = new Button("Browse");
		HBox titlesBox = new HBox(50);
		titlesBox.setAlignment(Pos.BOTTOM_RIGHT);
		titlesBox.getChildren().add(titlesBtn);
		grid.add(titlesBox, 1, 6);
		
		titlesBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					titlesFileName = file.getAbsolutePath();
					titlesField.setText(getFileNameForField(titlesFileName));	
				} else {
					//
				}
			}
		});

		//Tags file label, text field and browse button.
		Label tagsLabel = new Label("Select tags list file:");
		grid.add(tagsLabel, 0, 7);

		TextField tagsField = new TextField();
		tagsField.setMinSize(300, 20);
		tagsField.setEditable(false);
		grid.add(tagsField, 0, 8);

		Button tagsBtn = new Button("Browse");
		HBox tagsBox = new HBox(50);
		tagsBox.setAlignment(Pos.BOTTOM_RIGHT);
		tagsBox.getChildren().add(tagsBtn);
		grid.add(tagsBox, 1, 8);
		
		tagsBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					tagsFileName = file.getAbsolutePath();
					tagsField.setText(getFileNameForField(tagsFileName));	
				} else {
					//
				}
			}
		});

		//Well names file label, text field and browse button.
		Label wellsLabel = new Label("Select wells names file:");
		grid.add(wellsLabel, 0, 9);

		TextField wellsField = new TextField();
		wellsField.setMinSize(300, 20);
		wellsField.setEditable(false);
		grid.add(wellsField, 0, 10);

		Button wellsBtn = new Button("Browse");
		HBox wellsBox = new HBox(50);
		wellsBox.setAlignment(Pos.BOTTOM_RIGHT);
		wellsBox.getChildren().add(wellsBtn);
		grid.add(wellsBox, 1, 10);

		wellsBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					wellsFileName = file.getAbsolutePath();
					wellsField.setText(getFileNameForField(wellsFileName));	
				} else {
					//
				}
			}
		});
		
		Button clearBtn = new Button("Clear all");
		HBox clearBox = new HBox(50);
		clearBox.setAlignment(Pos.BASELINE_RIGHT);
		clearBox.getChildren().add(clearBtn);
		grid.add(clearBox, 0, 12);
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				csvInputFileName = "";
				csvInputField.setText("");
				networkDirectory = "";
				networkField.setText("");
				titlesFileName = "";
				titlesField.setText("");
				tagsFileName = "";
				tagsField.setText("");
				wellsFileName = "";
				wellsField.setText("");
			}
		});
		
		Button scannerBtn = new Button("Run scanner");
		HBox scannerBox = new HBox(50);
		scannerBox.setAlignment(Pos.BASELINE_CENTER);
		scannerBox.getChildren().add(scannerBtn);
		grid.add(scannerBox, 1, 12);
		scannerBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				if(checkParameters()) {
					launchScanner();
				}
			}
		});
		
		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	/**
	 * Show only last 35 characters from the file path.
	 * 
	 * @param fileName
	 * @return
	 */
	private static String getFileNameForField(String fileName) {
		if(fileName.length()>35) {
			return "..."+fileName.substring(fileName.length()-35);			
		} else {
			return fileName;
		}
	}
	
	/**
	 * Check parameters before launching scanner process.
	 * @return
	 */
	private boolean checkParameters() {
		if(csvInputFileName == "") {
			showErrorDialog("CSV input file", "Missing CSV input filename.");
			return false;
		} else if(networkDirectory == "") {
			showErrorDialog("Network location", "Missing network location for files.");
			return false;
		} 		
		return true;
	}
	
	private void showErrorDialog(String headerMsg, String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Missing Parameter!");
		alert.setHeaderText(headerMsg);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	/**
	 * Launch scanner thread.
	 */
	private void launchScanner() {
		
	}

}
