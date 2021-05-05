package Application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

//SignUpForm.fxml controller - sign up page
public class SignupController implements FindController {
	
	@FXML private TextField jFullName;
	@FXML private TextField jId;
	@FXML private PasswordField jPassword;
	@FXML private PasswordField jConfirmPass;
	@FXML private Text jMsgError;
	private FxmlController fc = new FxmlController();
	private UserPageController userCon; //for passing user details after user is created. then load UserPage.fxml with correct data
	private Alert alr = new Alert(AlertType.NONE); // if account is created successfully
	
	public void SignupAction(ActionEvent event) {
		//collect values from fields
		String fname = jFullName.getText();
		String id = jId.getText();
		String password = jPassword.getText();
		String bank_account;
		
		if(verifyFields()) {
			
			if(!checkID(id)) {
				bank_account = generateAccount(); 
				//prepare statement 
				PreparedStatement st;
				String registerUserQuery =
						"INSERT INTO `users`(`user_id`, `password`, `bank_account`, `full_name`, `balance`) VALUES (?,?,?,?,?)";

				try { 
					st = sqlConnection.getConnection().prepareStatement(registerUserQuery);
					st.setString(1, id);
					st.setString(2, password);
					st.setString(3, bank_account);
					st.setString(4, fname);
					st.setInt(5, 0);
					if(st.executeUpdate() != 0) {
						alr.setAlertType(AlertType.INFORMATION);
						alr.setContentText("Account is created successfully!");
						alr.setHeaderText("Successfull");
						alr.showAndWait();
						Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
						MoveToUserPage(primaryStage, id);
					}
					else {
						jMsgError.setText("Error, please try again");
						jMsgError.setVisible(true);
					}	

				} catch(SQLException ex) {
					Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
				} 
			}
		} 
	}
	
	public boolean verifyFields() {
		String fname = jFullName.getText();
		String id = jId.getText();
		String password = jPassword.getText();
		String confirmPass = jConfirmPass.getText();
		
		if(fname.equals("") || id.equals("") || password.equals("") || confirmPass.equals("")) {
			jMsgError.setVisible(true);
			return false;
		}
		//check if password and confirm password fields are the same
		if(!password.equals(confirmPass)) {
			jMsgError.setText("Password don't match");
			jMsgError.setVisible(true);
			return false;
		}
		return true;
	}
	
	//generate unique 8 digit number for bank account. First digit is 9
	private String generateAccount() {
		String bank_account = "9";
		for(int i=0; i<8; i++) 
			bank_account += String.valueOf((int)(Math.random() * 9));
		return bank_account;
	}
	
	//check if id is already exist - if yes return true, else return false
	@SuppressWarnings("finally")
	public boolean checkID(String id) {
		 PreparedStatement st;
	     ResultSet rs;
	     String query = "SELECT * FROM `users` WHERE `user_id` = ?";
	     boolean id_exist = false;
	   
	     try {
	            st = sqlConnection.getConnection().prepareStatement(query);
	            st.setString(1, id);
	            rs = st.executeQuery();
	            if(rs.next())
	            {
	            	id_exist = true;
	            	jMsgError.setText("User ID is already exist");
	            	jMsgError.setVisible(true);
	            }
	            
	        } catch (SQLException ex) {
	            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	        	return id_exist;
	        }
	    }
	
	public void MoveToLoginform(ActionEvent event) {
		Stage st = (Stage)((Node)event.getSource()).getScene().getWindow();
		fc.LoadAnotherFxml(st, "LoginForm.fxml", new LoginController());
	}
	
	public void MoveToUserPage(Stage primaryStage, String id) {
		userCon = new UserPageController(id);
		fc.LoadAnotherFxml(primaryStage, "UserPage.fxml", userCon);
		userCon.handleHeaders();
	}

	@Override
	public SignupController getController() {
		return this;
	}
}
