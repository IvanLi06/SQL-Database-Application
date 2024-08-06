// Class: ISTE-330
// Date: 11/20/2023
// Group: 3
// Project Part-3
// Professor Habermas

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Group3_Presentation extends JFrame {
    Group3_DataLayer dl = new Group3_DataLayer();
    public Font myFontForOutput = new Font("Courier", Font.PLAIN, 32);
    String databaseName = new String();
    String userName = new String();
    String password = new String();

    public Group3_Presentation() {

        JPanel Inputbox = new JPanel(new GridLayout(3, 2));
        JLabel lblUser = new JLabel("Username -> ");
        JLabel lblPassword = new JLabel("Password (default: student)-> ");
        JTextField tfUser = new JTextField("root");
        JTextField tfPassword = new JPasswordField("");
        JLabel lblDatabase = new JLabel("Database ->");
        JTextField tfDatabase = new JTextField("Group3_DB");

        Inputbox.add(lblUser);
        Inputbox.add(tfUser);
        Inputbox.add(lblPassword);
        Inputbox.add(tfPassword);
        Inputbox.add(lblDatabase);
        Inputbox.add(tfDatabase);

        lblUser.setFont(myFontForOutput);
        tfUser.setFont(myFontForOutput);
        tfUser.setForeground(Color.BLUE);
        lblPassword.setFont(myFontForOutput);
        tfPassword.setFont(myFontForOutput);
        tfPassword.setForeground(Color.BLUE);
        lblDatabase.setFont(myFontForOutput);
        tfDatabase.setFont(myFontForOutput);
        tfDatabase.setForeground(Color.BLUE);

        JOptionPane.showMessageDialog(null, Inputbox,
                "Input    Default password is \"student\"", JOptionPane.QUESTION_MESSAGE);

        String userName = tfUser.getText();
        String database = tfDatabase.getText();

        String password = new String();
        String passwordInput = new String();

        passwordInput = tfPassword.getText();

        // set the default password to "student"
        if (passwordInput.equalsIgnoreCase("")) {
            password = "student"; // CHANGE TO STUDENT
        } else {
            password = passwordInput;
        }

        dl.connect(userName, password, database); // Call DataLayer

        // call function to log in as user
        LoginScreen();

        setVisible(true);
    } // End of Constructor

    public void LoginScreen() {
        // reset the frame
        getContentPane().removeAll();
        repaint();

        setTitle("Login");
        setSize(630, 400);
        setLocation(650, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create components
        JComboBox<String> userTypeComboBox = new JComboBox<>(new String[] { "Faculty", "Student", "Other (Outside Org)" });
        JTextField usernameField = new JTextField("fac001", 20);
        JLabel passwordText = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField("123", 20);
        JTextArea outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);

        JButton loginButton = new JButton("Login");
        JButton createButton = new JButton("Create new user");
        JButton exitButton = new JButton("Exit");

        // Add components to panels
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("User Type:"));
        inputPanel.add(userTypeComboBox);
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(passwordText);
        inputPanel.add(passwordField);
        // show password only for faculty and students
        userTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userTypeComboBox.getSelectedIndex() == 2) {
                    passwordText.setVisible(false);
                    passwordField.setVisible(false);
                } else {
                    passwordText.setVisible(true);
                    passwordField.setVisible(true);
                }
            }
        });

        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.add(loginButton);
        actionPanel.add(createButton);

        JPanel createPanel = new JPanel(new GridLayout(3, 1));
        JButton facButton = new JButton("Faculty User Create");
        JButton stuButton = new JButton("Student User Create");
        JButton orgButton = new JButton("Outside User Create");

        createPanel.add(facButton);
        createPanel.add(stuButton);
        createPanel.add(orgButton);

        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setContentPane(createPanel);
        dialog.setLocationRelativeTo(null);
        dialog.pack();

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(true);
            }
        });

        // Add action listeners for buttons to open new screens
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (e.getSource() == facButton) {
                    createFaculty();
                } else if (e.getSource() == stuButton) {
                    createStudent();
                } else if (e.getSource() == orgButton) {
                    createOutsideOrganization();
                }
            }
        };

        facButton.addActionListener(buttonListener);
        stuButton.addActionListener(buttonListener);
        orgButton.addActionListener(buttonListener);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.CENTER);
        mainPanel.add(exitButton, BorderLayout.PAGE_END);

        // Add action listeners
        loginButton.addActionListener(e -> loginUser(usernameField.getText(), new String(passwordField.getPassword()),
                userTypeComboBox.getSelectedItem().toString()));
        exitButton.addActionListener(e -> exitProgram());

        // Set up the frame
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loginUser(String username, String password, String userType) {
        boolean loginSuccess = false;
        switch (userType) {
            case "Faculty":
                loginSuccess = dl.FacultyLogin(username, password);
                break;
            case "Student":
                loginSuccess = dl.StudentLogin(username, password);
                break;
            case "Other (Outside Org)":
                loginSuccess = dl.OutsideOrganizationLogin(username);
                break;
            default:
                System.out.println("Please enter in the correct user type.");
                break;
        }

        if (loginSuccess) {
            // close screen and open new screen
            this.dispose();
            getContentPane().removeAll();
            repaint();

            // show dialog with login success and username, userType
            JOptionPane.showMessageDialog(null,
                    "Login successful.\nUsername: " + username + "\nUser Type: " + userType);

            if (userType.equals("Faculty")) {
                FacultyScreen(username);
            } else if (userType.equals("Student")) {
                StudentScreen(username);
            } else if (userType.equals("Other (Outside Org)")) {
                OutsideOrganizationScreen(username);
            }
        } else {
            // show dialog with login failure
            JOptionPane.showMessageDialog(null, "Login failed.");
        }
    }

    // faculty screen that just shows username and a button to go back to login
    // screen
    private void FacultyScreen(String username) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel("Username: " + username);
        JButton backButton = new JButton("Logout");

        mainPanel.add(usernameLabel, BorderLayout.NORTH);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> LoginScreen());

        JPanel menuPanel = new JPanel(new GridLayout(3, 1));

        JButton addInterestButton = new JButton("Add Interest");
        addInterestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                facultyAddInterest(username);
            }
        });
        menuPanel.add(addInterestButton);

        JButton addAbstractButton = new JButton("Add Abstract");
        addAbstractButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                facultyAddAbstract(username);
            }
        });
        menuPanel.add(addAbstractButton);

        JButton searchButton = new JButton("Search for Student");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                facultySearch();
            }
        });
        menuPanel.add(searchButton);

        mainPanel.add(menuPanel, BorderLayout.CENTER);

        // Set up the frame
        setTitle("Faculty Screen");
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // student screen that shows username, a button to go back to login screen
    private void StudentScreen(String username) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel("Username: " + username);
        JButton backButton = new JButton("Back to Login Screen");

        mainPanel.add(usernameLabel, BorderLayout.NORTH);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> LoginScreen());

        JPanel menuPanel = new JPanel(new GridLayout(2, 1));
        JButton addInterestButton = new JButton("Add Interest");
        addInterestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentAddInterest(username);
            }
        });
        menuPanel.add(addInterestButton);

        JButton searchButton = new JButton("Search for Faculty");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentSearch();
            }
        });
        menuPanel.add(searchButton);

        mainPanel.add(menuPanel, BorderLayout.CENTER);

        // Set up the frame
        setTitle("Student Screen");
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // outside organization screen that shows username, a button to go back to login
    // screen
    private void OutsideOrganizationScreen(String username) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel("Username: " + username);
        JButton backButton = new JButton("Back to Login Screen");

        mainPanel.add(usernameLabel, BorderLayout.NORTH);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> LoginScreen());

        JPanel menuPanel = new JPanel(new GridLayout(2, 1));

        JButton facultySearchButton = new JButton("Search for Faculty");
        facultySearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {               
                // function searches for faculty 
                studentSearch();
            }
        });
        menuPanel.add(facultySearchButton);

        JButton studentSearchButton = new JButton("Search for Student");
        studentSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // function searches for student 
                facultySearch();
            }
        });
        menuPanel.add(studentSearchButton);

        mainPanel.add(menuPanel, BorderLayout.CENTER);

        // Set up the frame
        setTitle("Outside Organization Screen");
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createFaculty() {
        JPanel fCreatePanel = new JPanel(new GridLayout(4, 2));
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password");
        JTextField passwordField = new JTextField(20);
        JLabel fNameLabel = new JLabel("First Name");
        JTextField fNameField = new JTextField(20);
        JLabel lNameLabel = new JLabel("Last Name");
        JTextField lNameField = new JTextField(20);

        fCreatePanel.add(usernameLabel);
        fCreatePanel.add(usernameField);
        fCreatePanel.add(fNameLabel);
        fCreatePanel.add(fNameField);
        fCreatePanel.add(lNameLabel);
        fCreatePanel.add(lNameField);
        fCreatePanel.add(passwordLabel);
        fCreatePanel.add(passwordField);

        JOptionPane.showMessageDialog(null, fCreatePanel, "Faculty Create", JOptionPane.PLAIN_MESSAGE);

        JTextArea outputArea;

        boolean result = dl.insertFacultyRecord(usernameField.getText(), fNameField.getText(), lNameField.getText(),
                passwordField.getText());

        // if insert faculty record is successful, then add contact and interest
        if (result) {
            JPanel fContactPanel = new JPanel(new GridLayout(6, 2));
            JLabel firstContactLabel = new JLabel("First Contact");
            JTextField firstContactField = new JTextField(20);
            JLabel emailLabel = new JLabel("Email");
            JTextField emailField = new JTextField(20);
            JLabel officeNumLabel = new JLabel("Office Number");
            JTextField officeNumField = new JTextField(20);
            JLabel buildingNumLabel = new JLabel("Building Number");
            JTextField buildingNumField = new JTextField(20);
            JLabel officeHoursLabel = new JLabel("Office Hours");
            JTextField officeHoursField = new JTextField(20);
            JLabel altNumLabel = new JLabel("Alternate Number");
            JTextField altNumField = new JTextField(20);

            fContactPanel.add(firstContactLabel);
            fContactPanel.add(firstContactField);
            fContactPanel.add(emailLabel);
            fContactPanel.add(emailField);
            fContactPanel.add(officeNumLabel);
            fContactPanel.add(officeNumField);
            fContactPanel.add(buildingNumLabel);
            fContactPanel.add(buildingNumField);
            fContactPanel.add(officeHoursLabel);
            fContactPanel.add(officeHoursField);
            fContactPanel.add(altNumLabel);
            fContactPanel.add(altNumField);

            JOptionPane.showMessageDialog(null, fContactPanel, "Faculty Contact Add", JOptionPane.PLAIN_MESSAGE);

            boolean conResult = dl.addFacultyContact(usernameField.getText(), firstContactField.getText(),
                    emailField.getText(), officeNumField.getText(), buildingNumField.getText(),
                    officeHoursField.getText(), altNumField.getText());
            
            JPanel fInterestPanel = new JPanel(new GridLayout(6, 2));
            JLabel interestInfo = new JLabel("Enter Abstract Title and Abstract and/or 3 Interests (ALL THREE)");
            interestInfo.setFont(new Font("Serif", Font.BOLD, 16));
            JLabel abstractTitleLabel = new JLabel("Abstract Title");
            JTextField abstractTitleField = new JTextField(20);
            JLabel abstractLabel = new JLabel("Abstract Content");
            JTextArea abstractField = new JTextArea(5, 20);
            abstractField.setLineWrap(true);
            abstractField.setWrapStyleWord(true);
            JLabel interest1Label = new JLabel("Interest 1");
            JTextField interest1Field = new JTextField(20);
            JLabel interest2Label = new JLabel("Interest 2");
            JTextField interest2Field = new JTextField(20);
            JLabel interest3Label = new JLabel("Interest 3");
            JTextField interest3Field = new JTextField(20);

            fInterestPanel.add(interestInfo);
            fInterestPanel.add(new JLabel(""));
            fInterestPanel.add(abstractTitleLabel);
            fInterestPanel.add(abstractTitleField);
            fInterestPanel.add(abstractLabel);
            fInterestPanel.add(abstractField);
            fInterestPanel.add(interest1Label);
            fInterestPanel.add(interest1Field);
            fInterestPanel.add(interest2Label);
            fInterestPanel.add(interest2Field);
            fInterestPanel.add(interest3Label);
            fInterestPanel.add(interest3Field);
            boolean absResult = false;
            boolean intResult = false;

            JOptionPane.showMessageDialog(null, fInterestPanel, "Faculty Interest Add", JOptionPane.PLAIN_MESSAGE);

            // if abstract title and abstract are both entered, then add abstract
            if (abstractTitleField.getText().length() > 0 || abstractField.getText().length() > 0) {
                absResult = dl.addFacultyAbstract(usernameField.getText(), abstractTitleField.getText(),
                        abstractField.getText());
                if (!absResult) {
                    outputArea = new JTextArea("Faculty abstract add failed. See command line for the reason.");
                }
            }

            // if all three interests are entered, then add interests
            if (interest1Field.getText().length() > 0 && interest2Field.getText().length() > 0
                    && interest3Field.getText().length() > 0) {
                intResult = dl.addFacultyInterests(usernameField.getText(), interest1Field.getText() + "," +
                        interest2Field.getText() + "," + interest3Field.getText());
                if (!intResult) {
                    outputArea = new JTextArea("Faculty interest add failed. See command line for the reason.");
                }
                // if only one or two interests are entered, then show error message
            } else if (interest1Field.getText().length() > 0 || interest2Field.getText().length() > 0
                    || interest3Field.getText().length() > 0) {
                outputArea = new JTextArea("Faculty interest add failed. Please enter all three interests.");
            }

            // if neither abstract nor interests are entered, then show error message
            if (!absResult && !intResult) {
                outputArea = new JTextArea("Enter an abstract or three interests.");
            }

            // if contact, abstract, and interests are all added successfully, then show success message
            if (conResult && (absResult || intResult)) {
                outputArea = new JTextArea("Faculty user created success. Username: " + usernameField.getText()
                        + ". Please return to the main page to log in.");
            } else {
                outputArea = new JTextArea("Faculty information add failed. See command line for the reason.");
            }
        } else {
            outputArea = new JTextArea("Faculty create failed. See command line for the reason.");
        }
        JOptionPane.showMessageDialog(null, outputArea, "Faculty Create Result", JOptionPane.PLAIN_MESSAGE);
    }

    private void createStudent() {
        JPanel sCreatePanel = new JPanel(new GridLayout(4, 2));
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password");
        JTextField passwordField = new JTextField(20);
        JLabel fNameLabel = new JLabel("First Name");
        JTextField fNameField = new JTextField(20);
        JLabel lNameLabel = new JLabel("Last Name");
        JTextField lNameField = new JTextField(20);

        sCreatePanel.add(usernameLabel);
        sCreatePanel.add(usernameField);
        sCreatePanel.add(fNameLabel);
        sCreatePanel.add(fNameField);
        sCreatePanel.add(lNameLabel);
        sCreatePanel.add(lNameField);
        sCreatePanel.add(passwordLabel);
        sCreatePanel.add(passwordField);

        JOptionPane.showMessageDialog(null, sCreatePanel, "Student Create", JOptionPane.PLAIN_MESSAGE);

        JTextArea outputArea;

        boolean result = dl.insertStudentRecord(usernameField.getText(), fNameField.getText(), lNameField.getText(),
                passwordField.getText());
        // if insert student record is successful, then add contact and interest
        if (result) {
            JPanel sContactPanel = new JPanel(new GridLayout(2, 2));
            JLabel emailLabel = new JLabel("Email");
            JTextField emailField = new JTextField(20);
            JLabel portfolioLabel = new JLabel("Portfolio");
            JTextField portfolioField = new JTextField(20);

            sContactPanel.add(emailLabel);
            sContactPanel.add(emailField);
            sContactPanel.add(portfolioLabel);
            sContactPanel.add(portfolioField);

             JOptionPane.showMessageDialog(null, sContactPanel, "Student Contact Add", JOptionPane.PLAIN_MESSAGE);

            boolean conResult = dl.addStudentContact(usernameField.getText(), emailField.getText(),
                    portfolioField.getText());

            JPanel sInterestPanel = new JPanel(new GridLayout(4, 2));
            JLabel interestInfo = new JLabel("Enter 3 Interests");
            interestInfo.setFont(new Font("Serif", Font.BOLD, 16));
            JLabel interest1Label = new JLabel("Interest 1");
            JTextField interest1Field = new JTextField(20);
            JLabel interest2Label = new JLabel("Interest 2");
            JTextField interest2Field = new JTextField(20);
            JLabel interest3Label = new JLabel("Interest 3");
            JTextField interest3Field = new JTextField(20);

            sInterestPanel.add(interestInfo);
            sInterestPanel.add(new JLabel(""));
            sInterestPanel.add(interest1Label);
            sInterestPanel.add(interest1Field);
            sInterestPanel.add(interest2Label);
            sInterestPanel.add(interest2Field);
            sInterestPanel.add(interest3Label);
            sInterestPanel.add(interest3Field);

            JOptionPane.showMessageDialog(null, sInterestPanel, "Student Interest Add", JOptionPane.PLAIN_MESSAGE);

            boolean intResult = dl.addStudentInterests(usernameField.getText(), interest1Field.getText() + "," +
                    interest2Field.getText() + "," + interest3Field.getText());

            // if contact and interests are added successfully, then show success message
            if (conResult && intResult) {
                outputArea = new JTextArea("Student user created success. Username: " + usernameField.getText()
                        + ". Please return to the main page to log in.");
            } else {
                outputArea = new JTextArea("Student information add failed. See command line for the reason.");
            }

        } else {
            outputArea = new JTextArea("Student create failed. See command line for the reason.");
        }

        JOptionPane.showMessageDialog(null, outputArea, "Student Create Result", JOptionPane.PLAIN_MESSAGE);
    }

    private void createOutsideOrganization() {
        JPanel oCreatePanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField(20);
        JLabel nameLabel = new JLabel("Name");
        JTextField nameField = new JTextField(20);
        JLabel interestLabel = new JLabel("Interest");
        JTextField interestField = new JTextField(20);

        oCreatePanel.add(usernameLabel);
        oCreatePanel.add(usernameField);
        oCreatePanel.add(nameLabel);
        oCreatePanel.add(nameField);
        oCreatePanel.add(interestLabel);
        oCreatePanel.add(interestField);

        JOptionPane.showMessageDialog(null, oCreatePanel, "Outside Organization Create", JOptionPane.PLAIN_MESSAGE);

        JTextArea outputArea;

        boolean result = dl.insertOutsideOrganizationRecord(usernameField.getText(), nameField.getText(),
                interestField.getText());
        
        // if insert outside organization record is successful, then show success message
        if (result) {
            outputArea = new JTextArea("Outside Organization user created success. Username: " + usernameField.getText()
                    + ". Please return to the main page to log in.");
        } else {
            outputArea = new JTextArea("Outside Organization create failed. See command line for the reason.");
        }

        JOptionPane.showMessageDialog(null, outputArea, "Outside Organization Create Result", JOptionPane.PLAIN_MESSAGE);
    }

    // the function that adds interest for faculty
    private void facultyAddInterest(String username) {
        JPanel fAddIPanel = new JPanel();
        JLabel interestLabel = new JLabel("Interest");
        JTextField interestField = new JTextField(20);

        fAddIPanel.add(interestLabel);
        fAddIPanel.add(interestField);

        JOptionPane.showMessageDialog(null, fAddIPanel, "Faculty Add Interest", JOptionPane.PLAIN_MESSAGE);

        boolean result = dl.addFacultyInterests(username, interestField.getText());

        JTextArea outputArea;
        if (result) {
            outputArea = new JTextArea("Result: \n" + "Interest added succeed.");
        } else {
            outputArea = new JTextArea("Result: \n" + "Interest added fail. See command line for the reason.");
        }

        JOptionPane.showMessageDialog(null, outputArea, "Add Interest Result", JOptionPane.PLAIN_MESSAGE);
    }

    // the function that adds abstract for faculty
    private void facultyAddAbstract(String username) {
        JPanel fAddAPanel = new JPanel();
        JLabel titleLabel = new JLabel("Title");
        JTextField titleField = new JTextField(20);
        JLabel abstractLabel = new JLabel("Abstract");
        JTextField abstractField = new JTextField(40);

        fAddAPanel.add(titleLabel);
        fAddAPanel.add(titleField);
        fAddAPanel.add(abstractLabel);
        fAddAPanel.add(abstractField);

        JOptionPane.showMessageDialog(null, fAddAPanel, "Faculty Add Abstract", JOptionPane.PLAIN_MESSAGE);

        boolean result = dl.addFacultyAbstract(username, titleField.getText(), abstractField.getText());

        JTextArea outputArea;
        if (result) {
            outputArea = new JTextArea("Result: \n" + "Abstract add succeed.");
        } else {
            outputArea = new JTextArea("Result: \n" + "Abstract add failed. See command line for the reason.");
        }

        JOptionPane.showMessageDialog(null, outputArea, "Add Abstract Result", JOptionPane.PLAIN_MESSAGE);
    }

    // the function that searches students for faculty
    private void facultySearch() {
        JPanel fSearchPanel = new JPanel();
        JLabel interestLabel = new JLabel("Interest");
        JTextField interestField = new JTextField(20);

        fSearchPanel.add(interestLabel);
        fSearchPanel.add(interestField);

        JOptionPane.showMessageDialog(null, fSearchPanel, "Faculty Search Student", JOptionPane.PLAIN_MESSAGE);

        java.util.List<java.util.List<String>> result = dl.facultySearchStudent(interestField.getText());

        JTextArea outputArea;

        if (result.size() == 0) {
            outputArea = new JTextArea("Result: Empty set. Please search for a new word.");
        } else {
            String text = "Result: \n------------------------------------\n";
            for (int i = 0; i < result.size(); i++) {
                text += "Name: " + result.get(i).get(0) + "\n";
                text += "Interests: " + result.get(i).get(1) + "\n";
                text += "Email: " + result.get(i).get(2) + "\n";
                text += "Portfolio: " + result.get(i).get(3) + "\n";
                text += "------------------------------------\n";
            }
            outputArea = new JTextArea(text);
            outputArea.setEditable(false);
        }

        JOptionPane.showMessageDialog(null, outputArea, "Search Result", JOptionPane.PLAIN_MESSAGE);
    }

    // the function that adds interest for student
    private void studentAddInterest(String username) {
        JPanel sAddIPanel = new JPanel();
        JLabel interestLabel = new JLabel("Interest");
        JTextField interestField = new JTextField(20);

        sAddIPanel.add(interestLabel);
        sAddIPanel.add(interestField);

        JOptionPane.showMessageDialog(null, sAddIPanel, "Student Add Interest", JOptionPane.PLAIN_MESSAGE);

        boolean result = dl.addStudentInterests(username, interestField.getText());

        JTextArea outputArea;
        if (result) {
            outputArea = new JTextArea("Result: \n" + "Interest add succeed.");
        } else {
            outputArea = new JTextArea("Result: \n" + "Interest add failed. See command line for the reason.");
        }

        JOptionPane.showMessageDialog(null, outputArea, "Add Interest Result", JOptionPane.PLAIN_MESSAGE);
    }

    // the function that searches faculty members for student
    private void studentSearch() {
        JPanel sSearchPanel = new JPanel();
        JLabel interestLabel = new JLabel("Interest");
        JTextField interestField = new JTextField(20);

        sSearchPanel.add(interestLabel);
        sSearchPanel.add(interestField);

        JOptionPane.showMessageDialog(null, sSearchPanel, "Student Search Faculty", JOptionPane.PLAIN_MESSAGE);

        java.util.List<java.util.List<String>> intResult = dl.studentSearchFaculty(interestField.getText());
        java.util.List<java.util.List<String>> absResult = dl.studentSearchFacultyAbstract(interestField.getText());

        JTextArea outputArea;

        String text = "Result: \n------------------------------------\n";

        if (intResult.size() == 0) {
            text += "Matching Interest Results: Empty set.\n------------------------------------\n";
        } else {
            text += "Matching Interest Results: \n------------------------------------\n";
            for (int i = 0; i < intResult.size(); i++) {
                text += "Name: " + intResult.get(i).get(0) + "\n";
                text += "Interests: " + intResult.get(i).get(1) + "\n";
                text += "Email: " + intResult.get(i).get(2) + "\n";
                text += "Contact Number: " + intResult.get(i).get(3) + "\n";
                text += "Office Number: " + intResult.get(i).get(4) + "\n";
                text += "Building Number: " + intResult.get(i).get(5) + "\n";
                text += "Office Hours: " + intResult.get(i).get(6) + "\n";
                text += "Alternate Number: " + intResult.get(i).get(7) + "\n";
                text += "------------------------------------\n";
            }
        }

        if (absResult.size() == 0) {
            text += "Matching Abstract Results: Empty set.\n------------------------------------\n";
        } else {
            text += "Matching Abstract Results: \n------------------------------------\n";
            for (int i = 0; i < absResult.size(); i++) {
                text += "Name: " + absResult.get(i).get(0) + "\n";
                text += "Abstract Title: " + absResult.get(i).get(1) + "\n";
                text += "Abstract: " + absResult.get(i).get(2) + "\n";
                text += "Email: " + absResult.get(i).get(3) + "\n";
                text += "Contact Number: " + absResult.get(i).get(4) + "\n";
                text += "Office Number: " + absResult.get(i).get(5) + "\n";
                text += "Building Number: " + absResult.get(i).get(6) + "\n";
                text += "Office Hours: " + absResult.get(i).get(7) + "\n";
                text += "Alternate Number: " + absResult.get(i).get(8) + "\n";
                text += "------------------------------------\n";
            }
        }

        if (intResult.size() == 0 && absResult.size() == 0) {
            text += "Nothing found. Please search for a new word.";
        }

        outputArea = new JTextArea(text);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setColumns(100);

        JOptionPane.showMessageDialog(null, outputArea, "Search Result", JOptionPane.PLAIN_MESSAGE);
    }

    private void exitProgram() {
        System.exit(0);
    }

    public static void main(String[] args) {
        System.out.println("Created by Group 3");
        new Group3_Presentation(); // Create a new object. An Instantiation
    } // End of main method
}
