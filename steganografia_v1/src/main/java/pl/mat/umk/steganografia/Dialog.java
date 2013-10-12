package pl.mat.umk.steganografia;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import javax.swing.ImageIcon;

public class Dialog extends Stage {

    protected String stacktrace;
    protected double originalWidth, originalHeight;
    
    protected Scene scene;
    protected BorderPane borderPanel;
    protected ImageView icon;
    
    protected VBox messageBox;
    protected Label messageLabel;
    
    protected boolean stacktraceVisible;
    protected HBox stacktraceButtonsPanel;
    protected ToggleButton viewStacktraceButton;
    protected Button copyStacktraceButton;
    protected ScrollPane scrollPane;
    protected Label stackTraceLabel;
    
    protected HBox buttonsPanel;
    protected Button okButton;
    protected ProgressBar progBar;
    protected ImageView imageIcon;
    
    /**
* Extracts stack trace from Throwable
*/
    protected static class StacktraceExtractor {

        public String extract(Throwable t) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            return sw.toString();
        }
    }
    
    /**
* Dialog builder
*/
    public static class Builder {
        protected static final int STACKTRACE_LABEL_MAXHEIGHT = 240;
        protected static final int MESSAGE_MIN_WIDTH = 180;
        protected static final int MESSAGE_MAX_WIDTH = 800;
        protected static final int BUTTON_WIDTH = 60;
        protected static final double MARGIN = 10;
        protected static final String ICON_PATH = " /home/obi/git/steganografia_v1/steganografia_v1/src/main/resources/images/";
        
        protected Dialog stage;
        
        public Builder create() {
            stage = new Dialog();
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setIconified(false);
            stage.centerOnScreen();
            stage.borderPanel = new BorderPane();

            // icon
            stage.icon = new ImageView();
            stage.borderPanel.setLeft(stage.icon);
            BorderPane.setMargin(stage.icon, new Insets(MARGIN));
            
            // message
            stage.messageBox = new VBox();
            stage.messageBox.setAlignment(Pos.CENTER_LEFT);
            
            stage.messageLabel = new Label();
            stage.messageLabel.setWrapText(true);
            stage.messageLabel.setMinWidth(MESSAGE_MIN_WIDTH);
            stage.messageLabel.setMaxWidth(MESSAGE_MAX_WIDTH);
            
            stage.messageBox.getChildren().add(stage.messageLabel);
            stage.borderPanel.setCenter(stage.messageBox);
            BorderPane.setAlignment(stage.messageBox, Pos.CENTER);
            BorderPane.setMargin(stage.messageBox, new Insets(MARGIN, MARGIN, MARGIN, 2 * MARGIN));
            
            // buttons
            stage.buttonsPanel = new HBox();
            stage.buttonsPanel.setSpacing(MARGIN);
            stage.buttonsPanel.setAlignment(Pos.BOTTOM_CENTER);
            BorderPane.setMargin(stage.buttonsPanel, new Insets(0, 0, 1.5 * MARGIN, 0));
            stage.borderPanel.setBottom(stage.buttonsPanel);
            stage.borderPanel.widthProperty().addListener(new ChangeListener<Number> () {

                public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                    stage.buttonsPanel.layout();
                }
                
            });
            
            stage.scene = new Scene(stage.borderPanel);
            stage.setScene(stage.scene);
            return this;
        }
        
        public Builder setOwner(Window owner) {
            if (owner != null) {
                stage.initOwner(owner);
                stage.borderPanel.setMaxWidth(owner.getWidth());
                stage.borderPanel.setMaxHeight(owner.getHeight());
            }
            return this;
        }
        
        public Builder setTitle(String title) {
            stage.setTitle(title);
            return this;
        }
        
        public Builder setMessage(String message) {
            stage.messageLabel.setText(message);
            return this;
        }
        
        private void alignScrollPane() {
            stage.setWidth(
                stage.icon.getImage().getWidth()
                + Math.max(
                    stage.messageLabel.getWidth(),
                    (stage.stacktraceVisible
                        ? Math.max(
                            stage.stacktraceButtonsPanel.getWidth(),
                            stage.stackTraceLabel.getWidth())
                        : stage.stacktraceButtonsPanel.getWidth()))
                + 5 * MARGIN);
            
            stage.setHeight(
                    Math.max(
                        stage.icon.getImage().getHeight(),
                        stage.messageLabel.getHeight()
                            + stage.stacktraceButtonsPanel.getHeight()
                            + (stage.stacktraceVisible
                                ? Math.min(
                                    stage.stackTraceLabel.getHeight(),
                                    STACKTRACE_LABEL_MAXHEIGHT)
                                : 0))
                    
                    + stage.buttonsPanel.getHeight()
                    + 3 * MARGIN);
            if (stage.stacktraceVisible) {
                stage.scrollPane.setPrefHeight(
                    stage.getHeight()
                    - stage.messageLabel.getHeight()
                    - stage.stacktraceButtonsPanel.getHeight()
                    - 2 * MARGIN);
            }
            
            stage.centerOnScreen();
        }
        
        
        protected void setIconFromResource(String resourceName) {

      	  final Image image = new Image(getClass().getResourceAsStream(resourceName));
      	  stage.icon.setImage(image);
        }
        
        Task<Void> task = new Task<Void>(){
           @Override
           public Void call(){
               for (int i = 1; i <= 100; i++)    {
                   try {
                       Thread.sleep(10);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   System.out.println(i);
                   updateProgress(i , 100);
               }
               stage.close();
           return null;                
           }
       };
        
        protected Builder setWarningIcon() {
            setIconFromResource("/images/warningIcon.png");
            return this;
        }
        
        protected Builder setErrorIcon() {
            setIconFromResource(ICON_PATH + "errorIcon.png");
            return this;
        }
        
        
        protected Builder setInfoIcon() {
            setIconFromResource("/images/infoIcon.png");
            return this;
        }
        
        protected Builder setConfirmationIcon() {
            setIconFromResource("/images/confirm1.png");
            return this;
        }
                
        protected Builder addOkButton() {
            stage.okButton = new Button("OK");
            stage.okButton.setPrefWidth(BUTTON_WIDTH);
            stage.okButton.setOnAction(new EventHandler<ActionEvent> () {

                public void handle(ActionEvent t) {
                    stage.close();
                }
                
            });
            stage.buttonsPanel.getChildren().add(stage.okButton);
            return this;
        }
        protected Builder addProgress() {
           stage.progBar = new ProgressBar();
//           stage.progBar.setStyle("-fx-accent: red");
      	  stage.progBar.progressProperty().bind(task.progressProperty());

      	  stage.progBar.progressProperty().addListener(new ChangeListener<Number>() {
     	      public void changed(ObservableValue<? extends Number> ov, Number t, Number newValue) {
     	          if (newValue.doubleValue() >= 1) {
     	         	 stage.close();
     	          }
     	      }
     	  });
      	  
           Thread th = new Thread(task);
           th.setDaemon(true);
           th.start();
           

           stage.buttonsPanel.getChildren().add(stage.progBar);
           return this;
       }
        
        
        
        /**
* Build dialog
*
* @return dialog instance
*/
        public Dialog build() {
            if (stage.buttonsPanel.getChildren().size() == 0)
                throw new RuntimeException("Add one dialog button at least");
            
            stage.buttonsPanel.getChildren().get(0).requestFocus();
            return stage;
        }
        
    }
    
    /**
* Show information dialog box as parentWindow child
*
* @param title dialog title
* @param message dialog message
* @param owner parent window
*/
    public static void showInfo(String title, String message, Window owner) {
        new Builder()
            .create()
            .setOwner(owner)
            .setTitle(title)
            .setInfoIcon()
            .setMessage(message)
            .addOkButton()
                .build()
                    .show();
    }
    
    /**
* Show information dialog box as parentStage child
*
* @param title dialog title
* @param message dialog message
*/
    public static void showInfo(String title, String message) {
        showInfo(title, message, null);
    }

    /**
* Show warning dialog box as parentStage child
*
* @param title dialog title
* @param message dialog message
* @param owner parent window
*/
    public static void showWarning(String title, String message, Window owner) {
        new Builder()
            .create()
            .setOwner(owner)
            .setTitle(title)
            .setWarningIcon()
            .setMessage(message)
            .addOkButton()
                .build()
                    .show();
    }
    
    public static void showResults(String title, String message, Window owner) {
       new Builder()
           .create()
           .setOwner(owner)
           .setTitle(title)
           .setConfirmationIcon()
           .setMessage(message)
           .addOkButton()
               .build()
                   .show();
   }
    
    
    public static void showProgress(String title, String message, Window owner) {
       new Builder()
           .create()
           .setOwner(owner)
           .setTitle(title)
           .setMessage(message)
           .setWarningIcon()
           .addProgress()
               .build().show();
    }
    
    /**
* Show warning dialog box
*
* @param title dialog title
* @param message dialog message
*/
    public static void showWarning(String title, String message) {
        showWarning(title, message, null);
    }
    public static void showResults(String title, String message) {
   	 showResults(title, message, null);
   }
    

    public static void showProgress(String title, String message) {
       showProgress(title, message, null);
   }
    
    /**
* Show error dialog box
*
* @param title dialog title
* @param message dialog message
* @param owner parent window
*/
    public static void showError(String title, String message, Window owner) {
        new Builder()
            .create()
            .setOwner(owner)
            .setTitle(title)
            .setErrorIcon()
            .setMessage(message)
            .addOkButton()
                .build()
                    .show();
    }
    
    /**
* Show error dialog box
*
* @param title dialog title
* @param message dialog message
*/
    public static void showError(String title, String message) {
        showError(title, message, null);
    }
    
    
}