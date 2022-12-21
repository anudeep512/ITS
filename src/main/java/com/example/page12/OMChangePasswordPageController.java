package com.example.page12;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

public class OMChangePasswordPageController implements Initializable {
    @FXML
    private TextField tfOldPassword;
    @FXML
    private TextField tfNewPassword;
    @FXML
    private TextField tfConfirmPassword;
    @FXML
    private Label hiddenLabel;

    private Stage stage;

    private Scene scene;
    private Parent root;

    public static String iithId ;
    public void setIithId(String iithId) {
        OMChangePasswordPageController.iithId = iithId;
    }

    public void initialize(URL arg0, ResourceBundle arg1){
        hiddenLabel.setVisible(false);
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/project_its";
        String username = "root";
        String password = "krmrhzb12";
        try{
            Connection con = DriverManager.getConnection(jdbcURL,username,password) ;
            System.out.println("Connected successfully returning the connection object");
            return con ;
        }catch(SQLException e){
            System.out.println("Unable to generate connection with the database : "+e.getMessage());
            return null ;
        }
    }

    public void savePassword(ActionEvent event){
        hiddenLabel.setVisible(false);
        String id = iithId ;
        String oldPassword = tfOldPassword.getText().trim();
        String newPassword = tfNewPassword.getText().trim() ;
        String confirmNewPassword = tfConfirmPassword.getText().trim() ;
        if((!newPassword.equals(confirmNewPassword))){
            System.out.println(id.indexOf("OM"));
            hiddenLabel.setVisible(true);
        } else if(id.trim().length() == 0 || oldPassword.trim().length()==0 || newPassword.trim().length()==0 || confirmNewPassword.trim().length()==0) {
            hiddenLabel.setVisible(true);
        }else {
                hiddenLabel.setVisible(false);
                String query=null ;
            try {
                Connection con = getConnection();
                if (id.contains("OMLR")) {
                    query = "update libraryoperationsmanagertable set password=? Where iith_id=? and password=? ";
                }else if (id.contains("OMCL")) {
                     query = "update classoperationsmanagertable set password=? Where iith_id=? and password=? ";
                }else if(id.contains("OMOF")) {
                    query = "update officeoperationsmanagertable set password=? Where iith_id=? and password=? ";
                }else{
                    query = "update laboperationsmanagertable set password=? Where iith_id=? and password=? ";
                }

                    PreparedStatement st = con.prepareStatement(query);
                    st.setString(1, confirmNewPassword);
                    st.setString(2, id);
                    st.setString(3, oldPassword);
                    int roo = st.executeUpdate();
                    if (roo > 0) {
                        tfOldPassword.setText("");
                        tfConfirmPassword.setText("");
                        tfNewPassword.setText("");
                        root = FXMLLoader.load(requireNonNull(getClass().getResource("LoginPage.fxml")));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        st.close();
                        con.close();
                    } else {
                        hiddenLabel.setVisible(true);
                        System.out.println("User doesn't exist Please check the entered credentials");
                        st.close();
                        con.close();
                    }
                }catch(SQLException e){
                System.out.println(e.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void back(ActionEvent event) throws IOException {
        hiddenLabel.setVisible(false);
        String sceneStr=null ;
        if (iithId.contains("OMLR")){
            sceneStr = "OMSLibraryMode.fxml" ;
        }else if (iithId.contains("OMLB")){
            sceneStr = "OMSLabMode.fxml" ;
        }else if (iithId.contains("OMCL")){
            sceneStr = "OMSClassroomMode.fxml" ;
        }else{
            sceneStr = "OMSOfficeMode.fxml" ;
        }
            root = FXMLLoader.load(requireNonNull(getClass().getResource(sceneStr)));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }
}





