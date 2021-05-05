package Application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

//UserPage.fxml controller - user page
public class UserPageController implements FindController {
	private String id; //present user_id in order to retrieve all user details;
	@FXML private Text usernameText;
	@FXML private Text accountText;
	@FXML TextArea textArea;
	TextInputDialog td; //dialog for deposit/withdraw actions 
	private String username;
	private String bankaccount;
	private int balance;
	
	public UserPageController(String id) {
		this.id = id;
		getUserData();
		//handleHeaders();
	}
	
	private void getUserData() {
		PreparedStatement st;
		ResultSet rs;
		String query = "SELECT * FROM `users` WHERE `user_id` = ?";
		try {
			st = sqlConnection.getConnection().prepareStatement(query);
			st.setString(1, id);
			rs = st.executeQuery();
			if(rs.next())
			{
				//get username, bank account and balance from db
				bankaccount = rs.getString(3);
				username = rs.getString(4);
				balance = rs.getInt(5);
			}
		} catch(SQLException ex) {}
	}
	
	//when check balance button is pressed
	public void CheckBalance() {
		textArea.setText("Your Balance: " + balance);
	}
	
	//Action deposit/withdraw button is pressed
	//Using binding technique to avoid invalid user input
	public void handleWithdrawOrDeposit(ActionEvent mainEvent) {
		td = new TextInputDialog();
		Button okButton = (Button) td.getDialogPane().lookupButton(ButtonType.OK);
		TextField inputField = td.getEditor();
		Binding<Boolean> isInvalid = Bindings.createBooleanBinding(() -> isInvalid(inputField.getText()), inputField.textProperty());
		okButton.disableProperty().bind(isInvalid);
		td.show();
		
		//Ok button in text dialog is pressed
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent e) {
				String source = ((Button)mainEvent.getSource()).getText();
				int amount = Integer.parseInt(inputField.getText());
				if(source.equals("Deposit"))
					balance = balance + amount;
				else
					balance = balance - amount;
				CheckBalance();
				WriteToDb();
			}
		});
	}
	
	//parsing String to Int and check if legal or not.
	@SuppressWarnings("finally")
	private boolean isInvalid(String text) {
		boolean isValid = false;
		try {
			int amount = Integer.parseInt(text);
			if(amount <= 0)
				isValid = true;
			
		}catch (NumberFormatException e) {
			isValid = true;
		}
		finally {
			return isValid;
		}	
	}
	
	//After every successful deposit/withdraw action
	public void WriteToDb() {
		PreparedStatement st;
		String registerUserQuery =
				"UPDATE `users` SET `balance` = ? WHERE `user_id` = ? ";

		try { 
			st = sqlConnection.getConnection().prepareStatement(registerUserQuery);
			st.setInt(1, balance);
			st.setString(2, id);
			st.executeUpdate();
			
		} catch (SQLException ex) {
			Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//Present user name and bank account when user page is loaded
	public void handleHeaders() {
		usernameText.setText("Hi " + username);
		accountText.setText("Bank Account: " + bankaccount);
	}
	
	@Override
	public UserPageController getController() {
		return this;
	}
	
}
