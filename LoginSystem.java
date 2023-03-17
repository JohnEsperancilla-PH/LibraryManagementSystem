import javax.swing.*;

public class LoginSystem {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";
    private static final int MAX_ATTEMPTS = 3;

    public static boolean login() {
        int attemptCount = 0;
        while (attemptCount < MAX_ATTEMPTS) {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            Object[] inputFields = {"Enter username:", usernameField, "Enter password:", passwordField};
            int option = JOptionPane.showConfirmDialog(null, inputFields, "Login", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                    return true;
                } else {
                    attemptCount++;
                    int remainingAttempts = MAX_ATTEMPTS - attemptCount;
                    JOptionPane.showMessageDialog(null, "Incorrect username or password. You have " + remainingAttempts + " attempts remaining.");
                }
            } else {
                return false;
            }
        }
        JOptionPane.showMessageDialog(null, "Too many login attempts. Please try again later.");
        return false;
    }
}
