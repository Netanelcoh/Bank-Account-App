package Application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//LoginForm.fxml controller - Login page
public class LoginController implements FindController {
	@FXML private Text jSigninError; //for error msg to user
	@FXML private TextField jUseridText;
	@FXML private PasswordField jUserPassText;
	private FxmlController fc = new FxmlController();
	private UserPageController userCon; //for passing user details after user is created. then load UserPage.fxml with correct data

	//When sign in button is pressed
	public void SignInAction(ActionEvent event) {
		//collect values from fields
		String id = jUseridText.getText();
		String password = jUserPassText.getText();
		
		//create a select query to check if user id and password are exist.
		PreparedStatement st;
        ResultSet rs;
		String query = "SELECT * FROM `users` WHERE `user_id` = ? AND `password` = ?";
		
		//check if user id and password are empty
		if(id.equals("")) {
			jSigninError.setText("user id is empty");
			jSigninError.setVisible(true);
		}
		else if(password.equals("")) {
				jSigninError.setText("Please enter your password");
				jSigninError.setVisible(true);
			}
		//check if user id and password are exist in db
		else {
			    try {
		            st = sqlConnection.getConnection().prepareStatement(query);
		            st.setString(1, id);
		            st.setString(2, password);
		            rs = st.executeQuery();
		            if(rs.next()) {
		            	//get Stage and load user menu page
		            	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		            	MoveToUserPage(primaryStage, rs.getString(1));
		            }else{
		                // error message
		            	jSigninError.setText("User is not exist");
		    			jSigninError.setVisible(true);
		            }
		        } catch (SQLException ex) {
		            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
		        }
			}
	}
	
	public void MoveToSignUpform(ActionEvent event) {
		Stage st = (Stage)((Node)event.getSource()).getScene().getWindow();
		fc.LoadAnotherFxml(st, "SignUpForm.fxml", new SignupController());
	}

	public void MoveToUserPage(Stage primaryStage, String id) {
		userCon = new UserPageController(id);
		fc.LoadAnotherFxml(primaryStage, "UserPage.fxml", userCon);
		userCon.handleHeaders();
	}

	@Override
	public LoginController getController() {
		return this;
	}
}
