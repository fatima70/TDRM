package com.certis.oil.filecrawler.windows;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.certis.oil.filecrawler.scanners.FileCSVScanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * File scanner window user interface, coded with JavaFX.
 * 
 * @author timppa
 *
 */
public class FileCSVScannerWindows extends Application implements CallBack, EventHandler<WindowEvent> {

	private String csvInputFileName = "";
	private String extRulesFileName = "";
	private String tagsFileName = "";
	private String wellsFileName = "";
	private ProgressBar pb;
	private Label statusLabel;
	private FileCSVScanner fcs;
	private Button scannerBtn;
	private Button clearBtn;
	private ButtonBase wellsBtn;
	private ButtonBase extRulesBtn;
	private ButtonBase tagsBtn;
	private Button csvInputBtn;
	private Button stopBtn;
	private Stage primaryStage;
	
	public static void launchApp(String args[]) {
		FileCSVScannerWindows.launch(args);
	}

	/**
	 * Create scanner UI and show it.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(this);
		final FileChooser fileChooser = new FileChooser();
		primaryStage.setTitle("Certis Inc - CSV File List Scanner");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Choose the following parameters");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		//CSV input file label, text field and file browse button.
		Label csvInputLabel = new Label("Select CSV input file to analyze:");
		grid.add(csvInputLabel, 0, 1);

		TextField csvInputField = new TextField();
		csvInputField.setMinSize(300, 20);
		csvInputField.setEditable(false);
		grid.add(csvInputField, 0, 2);

		csvInputBtn = new Button("Browse");
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

		//Extension rule file label, text field and browse button.
		Label extRulesLabel = new Label("Select extension rules file:");
		grid.add(extRulesLabel, 0, 3);

		TextField extRulesField = new TextField();
		extRulesField.setMinSize(300, 20);
		extRulesField.setEditable(false);
		grid.add(extRulesField, 0, 4);

		extRulesBtn = new Button("Browse");
		HBox extRulesBox = new HBox(50);
		extRulesBox.setAlignment(Pos.BOTTOM_RIGHT);
		extRulesBox.getChildren().add(extRulesBtn);
		grid.add(extRulesBox, 1, 4);
		
		//Tags file label, text field and browse button.
		Label tagsLabel = new Label("Select tags list file:");
		grid.add(tagsLabel, 0, 5);

		TextField tagsField = new TextField();
		tagsField.setMinSize(300, 20);
		tagsField.setEditable(false);
		grid.add(tagsField, 0, 6);

		tagsBtn = new Button("Browse");
		HBox tagsBox = new HBox(50);
		tagsBox.setAlignment(Pos.BOTTOM_RIGHT);
		tagsBox.getChildren().add(tagsBtn);
		grid.add(tagsBox, 1, 6);
		
		//Well names file label, text field and browse button.
		Label wellsLabel = new Label("Select wells names file:");
		grid.add(wellsLabel, 0, 7);

		TextField wellsField = new TextField();
		wellsField.setMinSize(300, 20);
		wellsField.setEditable(false);
		grid.add(wellsField, 0, 8);

		wellsBtn = new Button("Browse");
		HBox wellsBox = new HBox(50);
		wellsBox.setAlignment(Pos.BOTTOM_RIGHT);
		wellsBox.getChildren().add(wellsBtn);
		grid.add(wellsBox, 1, 8);
		
		extRulesBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					extRulesFileName = file.getAbsolutePath();
					String shortName = getFileNameForField(extRulesFileName);
					extRulesField.setText(shortName);	
					tagsFileName = wellsFileName = extRulesFileName;
					tagsField.setText(shortName);
					wellsField.setText(shortName);
				} else {
					//
				}
			}
		});
		
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
		
		Label priorityOneLabel = new Label("Enter optional priority #1 column name for tags:");
		grid.add(priorityOneLabel, 0, 9);
		
		TextField priorityOneField = new TextField();
		priorityOneField.setMaxSize(150, 20);
		grid.add(priorityOneField, 0, 10);
		
		Label priorityTwoLabel = new Label("Enter optional priority #2 column name for tags:");
		grid.add(priorityTwoLabel, 0, 11);
		
		TextField priorityTwoField = new TextField();
		priorityTwoField.setMaxSize(150, 20);
		grid.add(priorityTwoField, 0, 12);
		
		clearBtn = new Button("Clear all");
		HBox clearBox = new HBox(50);
		clearBox.setAlignment(Pos.BASELINE_RIGHT);
		clearBox.getChildren().add(clearBtn);
		grid.add(clearBox, 0, 14);
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				csvInputFileName = "";
				csvInputField.setText("");
				extRulesFileName = "";
				extRulesField.setText("");
				tagsFileName = "";
				tagsField.setText("");
				wellsFileName = "";
				wellsField.setText("");
				priorityOneField.setText("");
				priorityTwoField.setText("");
			}
		});
		
		scannerBtn = new Button("Run scanner");
		HBox scannerBox = new HBox(50);
		scannerBox.setAlignment(Pos.BASELINE_CENTER);
		scannerBox.getChildren().add(scannerBtn);
		grid.add(scannerBox, 1, 14);
		scannerBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				if(checkParameters()) {
					try {
						launchScanner();
					} catch (IOException e1) {
						showErrorDialog("Launching failed!", e1.getMessage());
					}
				}
			}
		});
		
		statusLabel = new Label("Status: Click run to start.");
		grid.add(statusLabel, 0, 15);
		
		pb = new ProgressBar(0);
		pb.setMinSize(200, 10);
		grid.add(pb, 0, 16);
		
		stopBtn = new Button("Stop scanning");
		HBox stopBox = new HBox(70);
		stopBox.setAlignment(Pos.BASELINE_CENTER);
		stopBox.getChildren().add(stopBtn);
		grid.add(stopBox, 1, 16);
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				if(fcs != null && fcs.isRunning()) {
					fcs.stop();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					setDisableButtons(false);					
				} else {
					showErrorDialog("Nothing is running", "Scanning not started.");
				}
			}
		});

		Scene scene = new Scene(grid, 500, 550);
		primaryStage.setScene(scene);
		primaryStage.show();
		//stopBox.setDisable(true);
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
		} else if(extRulesFileName == "") {
			showErrorDialog("Extension rules file", "Missing extension rules filename.");
			return false;
		} else if(tagsFileName == "") {
			showErrorDialog("Tags file", "Missing tags filename.");
			return false;
		} else if(wellsFileName == "") {
			showErrorDialog("Well names file", "Missing well names filename.");
			return false;
		} else if(!csvInputFileName.toLowerCase().endsWith("csv")) {
			showErrorDialog("CSV input file", "CSV input filename does not end with .csv");
			return false;
		} else if(!extRulesFileName.toLowerCase().endsWith("xlsx")) {
			showErrorDialog("Extension rule file", "Extension rule file must be an Excel XLSX file.");
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
	private void launchScanner() throws IOException {
		statusLabel.setText("Status: Starting scanner.");
		fcs = new FileCSVScanner(csvInputFileName, this, extRulesFileName, 
				tagsFileName, wellsFileName);
		fcs.start();
		setDisableButtons(true);
		pb.setProgress(-1d);//progress start spinning.
	}

	@Override
	public void progress(String message, double percentage) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	    		statusLabel.setText("Status: "+message);
	    		pb.setProgress(percentage);
	        }
	    });
	}

	@Override
	public void errorMessage(String errorMsg) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	    		statusLabel.setText("Status: error");
	    		pb.setProgress(0d);
	    		showErrorDialog("Error: ", errorMsg);
	        }
	    });
		setDisableButtons(false);
	}

	@Override
	public void summary(String[] summaryMsg) {
		StringBuffer sb = new StringBuffer();
		for(String msg : summaryMsg) {
			sb.append(msg);
			sb.append("\r\n");
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("Summary of results:");
		alert.setContentText(sb.toString());
		alert.showAndWait();	
		setDisableButtons(false);
	}
	
	public void setDisableButtons(boolean disable) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	    		csvInputBtn.setDisable(disable);
	    		extRulesBtn.setDisable(disable);
	    		tagsBtn.setDisable(disable);
	    		wellsBtn.setDisable(disable);
	    		clearBtn.setDisable(disable);
	    		scannerBtn.setDisable(disable);
	    		stopBtn.setDisable(!disable);
	        }
	    });
	}

	@Override
	public void handle(WindowEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Close Confirmation");
        alert.setHeaderText("Closing application");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        	if(fcs != null && fcs.isRunning()) {
        		fcs.stop();
        		try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
            primaryStage.close();
        }
        if(result.get()==ButtonType.CANCEL){
            event.consume();
            alert.close();
        }
	}
}
