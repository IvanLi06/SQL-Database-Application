// Class: ISTE-330
// Date: 11/20/2023
// Project Part-2
// Group: 3
// Members: Angie Li, Ata Noor, Hritish Mahajan, Ivan Li, Kelly Wu, Tryder Kulbacki
// Professor Habermas

import java.util.*;
import java.sql.*;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Group3_DataLayer {
	private Connection conn;
	private ResultSet rs;
	private Statement stmt;
	final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
	
    public boolean connect(String user, String password, String database) {
        conn = null;

        String url = "jdbc:mysql://localhost/" + database;
        url = url + "?serverTimezone=UTC"; // added only required for Mac Users

        try {
            Class.forName(DEFAULT_DRIVER);
            System.out.println("CLASSPATH is set correctly!");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("\nCreated Connection!\n");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("ERROR, CAN NOT CONNECT!!");
            System.out.println("Class");
            System.out.println("ERROR MESSAGE-> " + cnfe);
            System.exit(0);
        } catch (SQLException sqle) {
            System.out.println("ERROR SQLExcepiton in connect()");
            System.out.println("ERROR MESSAGE -> " + sqle);
            System.exit(0);
        }
        return (conn != null); // returns true if connection is successful
    } // End of connect method

    public boolean close() {
        try {
            rs.close();
            stmt.close();
            conn.close();
            return true;
        } catch (SQLException sqle) {
            System.out.println("ERROR IN METHOD close()");
            System.out.println("ERROR MESSAGE -> " + sqle);
            return false;
        }
    } // End of method close

	// encrypt password using SHA-1
	public static String encrypt(String secret) {// Endcypt password
		String sha1 = "";
		String value = new String(secret);
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(value.getBytes("utf8"));
			sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		} // end of catch

		System.out.println("The sha1 of \"" + value + "\" is:");
		System.out.println("--->" + sha1);
		System.out.println();
		return sha1;
	}// end of encrypt

	public String readFacultyPassword(String factID) { // Username came fron the first GUI Input Box = FacultyID
		String DBpassword = new String();
		try {
			PreparedStatement stmt2;
			stmt2 = conn.prepareStatement("SELECT password FROM faculty WHERE username = ?");
			stmt2.setString(1, factID);
			rs = stmt2.executeQuery();
			while (rs.next()) {
				DBpassword = rs.getString(1);
			} // end of while loop
		} // endn of try
		catch (Exception e) {
			System.out.println("Error whlie getting password from database");
			System.out.println("Error message is --> " + e);
		} // end of catch
		return DBpassword;
	}// end of method

	public String readStudentPassword(String studID) {
		String DBpassword = new String();
		try {
			PreparedStatement stmt2;
			stmt2 = conn.prepareStatement("SELECT password FROM student WHERE username = ?");
			stmt2.setString(1, studID);
			rs = stmt2.executeQuery();
			while (rs.next()) {
				DBpassword = rs.getString(1);
			} // end of while loop
		} // endn of try
		catch (Exception e) {
			System.out.println("Error whlie getting password from database");
			System.out.println("Error message is --> " + e);
		} // end of catch
		return DBpassword;
	}// end of method

	// Login using faculty username and password and return true if successful
	public boolean FacultyLogin(String userName, String password) {
		String passwordEC = encrypt(password);
		System.out.println("\nPassword: " + password + "<-");

		String DBpassword = new String();
		DBpassword = readFacultyPassword(userName);
		System.out.println("In main,  Database password ->" + DBpassword + "<- ");

		if (DBpassword.equals(passwordEC)) {
			System.out.println("\n\tSUCCESS\nLet the user make changes to the informaton!");
			return true;
		} else {
			System.out.println("\n\tFAILED\nDo not let this user make changes to the Database!");
		}

		return false;
	}

	// Login using student username and password and return true if successful
	public boolean StudentLogin(String userName, String password) {
		String passwordEC = encrypt(password);
		System.out.println("\nPassword: " + password + "<-");

		String DBpassword = new String();
		DBpassword = readStudentPassword(userName);
		System.out.println("In main,  Database password ->" + DBpassword + "<- ");

		if (DBpassword.equals(passwordEC)) {
			System.out.println("\n\tSUCCESS\nLet the user make changes to the informaton!");
			return true;
		} else {
			System.out.println("\n\tFAILED\nDo not let this user make changes to the Database!");
		}

		return false;
	}

	// Login using outside organization username and return true if found
	public boolean OutsideOrganizationLogin(String username) {
		// check if usrname exists in outside organization table
		boolean result = false;
		try {
			String sql = "SELECT COUNT(*) FROM Outside_Organization WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 1) {
					System.out.println("Outside Organization user found.");
					result = true;
				} else {
					System.out.println("Outside Organization user not found.");
					result = false;
				}
			}
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to get the interest count.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			result = false;
		}
		return result;
	}

	// Insert a new record into the Faculty table
	public boolean insertFacultyRecord(String username, String fname, String lname, String password) {
		boolean result = true;
		int x = 0;
		password = encrypt(password);
		try {
			PreparedStatement stmt3;
			stmt3 = conn.prepareStatement("INSERT INTO Faculty VALUES(?,?,?,?)");
			stmt3.setString(1, username);
			stmt3.setString(2, fname);
			stmt3.setString(3, lname);
			stmt3.setString(4, password);
			x = stmt3.executeUpdate(); // Performs the update command
			if (x > 0)
				result = true;
			else
				result = false;
		} // endn of try
		catch (Exception e) {
			System.out.println("Error whlie trying to insert a record or close.");
			System.out.println("Error message is --> " + e);
			result = false;
		} // end of catch

		return result;
	}// end of method

	// Insert a new record into the Student table
	public boolean insertStudentRecord(String username, String fname, String lname, String password) {
		boolean result = true;
		int x = 0;
		password = encrypt(password);
		try {
			PreparedStatement stmt3;
			stmt3 = conn.prepareStatement("INSERT INTO Student VALUES(?,?,?,?)");
			stmt3.setString(1, username);
			stmt3.setString(2, fname);
			stmt3.setString(3, lname);
			stmt3.setString(4, password);
			x = stmt3.executeUpdate(); // Performs the update command
			if (x > 0)
				result = true;
			else
				result = false;
		} // endn of try
		catch (Exception e) {
			System.out.println("Error whlie trying to insert a record or close.");
			System.out.println("Error message is --> " + e);
			result = false;
		} // end of catch

		return result;
	}// end of method

	// Add Contact info for faculty
	public boolean addFacultyContact(String username, String firstContact, String email,
			String officeNumber, String buildingNumber, String officeHours, String alternativeNumber) {
		try {
			System.out.println("--- add Faculty_Contact started ---");
			String sql = "INSERT INTO Faculty_Contact VALUES(?,?,?,?,?,?,?)";
			return executeContactInsert(sql, username, firstContact, email, officeNumber, buildingNumber, officeHours,
					alternativeNumber);
		} catch (SQLException sqle) {
			handleSQLException("Faculty_Contact", sqle);
			return false;
		}
	}

	// Add Contact info for student
	public boolean addStudentContact(String username, String email, String portfolio) {
		try {
			System.out.println("--- add Student_Contact started ---");
			String sql = "INSERT INTO Student_Contact VALUES(?,?,?)";
			return executeContactInsert(sql, username, email, portfolio);
		} catch (SQLException sqle) {
			handleSQLException("Student_Contact", sqle);
			return false;
		}
	}

	// Helper method to execute contact insert queries
	private boolean executeContactInsert(String sql, String... params) throws SQLException {
		boolean result = false;
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				ps.setString(i + 1, params[i]);
			}
			int rows = ps.executeUpdate();
			System.out.println("You added " + rows + " rows.");
			result = rows > 0;
		}
		return result;
	}

	// Helper method to handle SQL exceptions
	private void handleSQLException(String tableName, SQLException sqle) {
		System.out.println("Error while trying to insert a record into " + tableName);
		System.out.println("Error message is --> " + sqle);
		sqle.printStackTrace();
	}

	// Search for a student interest and return a list of students (with contact info) with that interest
	public List<List<String>> facultySearchStudent(String interest) {
		List<List<String>> result = new ArrayList<>(); // List of Students [Name, Interests, Email, Portfolio]

		System.out.println("--- search Student_Interest and Student_Interests started ---");

		int interest_ID = interestCheckStudent(interest);

		if (interest_ID == -1) {
			System.out.println("No student has interest: " + interest + ". Please try to search again.");
		}

		try {
			String sql = "SELECT CONCAT(student.firstname, ' ', student.lastname) AS 'Student Name', " +
					"GROUP_CONCAT(student_interest.keyword) AS 'Student Interests', " +
					"student_contact.email AS 'Email', " +
					"student_contact.portfolio AS 'Portfolio' " +
					"FROM student " +
					"INNER JOIN student_interests USING (username) " +
					"INNER JOIN student_interest USING (interest_id) " +
					"LEFT JOIN student_contact USING (username) " + // Use LEFT JOIN to include students without contact
																	// info
					"WHERE student.username IN (" +
					"SELECT DISTINCT student_interests.username " +
					"FROM student_interests " +
					"INNER JOIN student_interest USING (interest_id) " +
					"WHERE student_interest.keyword = ?) " +
					"GROUP BY student.username, student.firstname, student.lastname, student_contact.email, student_contact.portfolio;";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, interest);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String studentName = rs.getString("Student Name");
				String studentInterests = rs.getString("Student Interests");
				String email = rs.getString("Email");
				String portfolio = rs.getString("Portfolio");

				System.out.println("Student Name: " + studentName);
				System.out.println("Student Interests: " + studentInterests);
				System.out.println("Email: " + email);
				System.out.println("Portfolio: " + portfolio);
				System.out.println("-------------------------");

				result.add(Arrays.asList(studentName, studentInterests, email, portfolio));

			}
		} catch (SQLException sqle) {
			System.out.println("Error while trying to search for a student interest.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
		}

		return result;
	}

	// Search for a faculty interest and return a list of faculty (with contact info) with that interest
	public List<List<String>> studentSearchFaculty(String interest) {
		List<List<String>> result = new ArrayList<>(); // List of Faculty [Name, Interests, Email, Contact Number,
														// Office Number, Building Number, Office Hours, Alternative
														// Contact]

		System.out.println("--- search Faculty_Interest and Faculty_Interests started ---");

		int interest_ID = interestCheckFaculty(interest);

		if (interest_ID == -1) {
			System.out.println("No faculty has interest: " + interest + ". Please try to search again.");
		}

		try {
			String sql = "SELECT CONCAT(faculty.firstname, ' ', faculty.lastname) AS 'Faculty Name', " +
					"GROUP_CONCAT(faculty_interest.keyword) AS 'Faculty Interests', " +
					"faculty_contact.email AS 'Email', " +
					"faculty_contact.firstContact AS 'Contact Number', " +
					"faculty_contact.officeNumber AS 'Office Number', " +
					"faculty_contact.buildingNumber AS 'Building Number', " +
					"faculty_contact.officeHours AS 'Office Hours', " +
					"faculty_contact.alternativeNumber AS 'Alternative Contact' " +
					"FROM faculty " +
					"INNER JOIN faculty_interests USING (username) " +
					"INNER JOIN faculty_interest USING (interest_id) " +
					"LEFT JOIN faculty_contact USING (username) " +
					"WHERE faculty.username IN (" +
					"SELECT DISTINCT faculty_interests.username " +
					"FROM faculty_interests " +
					"INNER JOIN faculty_interest USING (interest_id) " +
					"WHERE faculty_interest.keyword = ?) " +
					"GROUP BY faculty.username, faculty.firstname, faculty.lastname;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, interest);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String facultyName = rs.getString("Faculty Name");
				String facultyInterests = rs.getString("Faculty Interests");
				String email = rs.getString("Email");
				String contactNumber = rs.getString("Contact Number");
				String officeNumber = rs.getString("Office Number");
				String buildingNumber = rs.getString("Building Number");
				String officeHours = rs.getString("Office Hours");
				String alternativeContact = rs.getString("Alternative Contact");

				System.out.println("Faculty Name: " + facultyName);
				System.out.println("Faculty Interests: " + facultyInterests);
				System.out.println("Email: " + email);
				System.out.println("Contact Number: " + contactNumber);
				System.out.println("Office Number: " + officeNumber);
				System.out.println("Building Number: " + buildingNumber);
				System.out.println("Office Hours: " + officeHours);
				System.out.println("Alternative Contact: " + alternativeContact);

				result.add(Arrays.asList(facultyName, facultyInterests, email, contactNumber, officeNumber,
						buildingNumber, officeHours, alternativeContact));
			}
		} catch (SQLException sqle) {
			System.out.println("Error while trying to search for a faculty interest.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
		}

		return result;
	}

	// Search for a keyword in faculty abstract and return a list of faculty (with contact info) with that keyword in their abstract
	public List<List<String>> studentSearchFacultyAbstract(String keyword) {
	// Checks if the keyword is empty, if so, return an empty result set
    	if (keyword == null || keyword.trim().isEmpty()) {
        	System.out.println("Empty keyword. Please provide a valid keyword.");
        	return new ArrayList<>();
    	}
        List<List<String>> result = new ArrayList<>();
        boolean found = false;
        try {
            String sql = "SELECT CONCAT(Faculty.firstName, ' ', Faculty.lastName) AS 'Faculty Name', " +
                    "Abstract.abstractTitle, Abstract.abstract, " +
                    "Faculty_Contact.email AS 'Email', " +
                    "Faculty_Contact.firstContact AS 'Contact Number', " +
                    "Faculty_Contact.officeNumber AS 'Office Number', " +
                    "Faculty_Contact.buildingNumber AS 'Building Number', " +
                    "Faculty_Contact.officeHours AS 'Office Hours', " +
                    "Faculty_Contact.alternativeNumber AS 'Alternative Contact' " +
                    "FROM Abstract " +
                    "INNER JOIN Faculty_Abstracts USING (abstractID) " +
                    "INNER JOIN Faculty USING (username) " +
                    "LEFT JOIN Faculty_Contact USING (username) " +
                    "WHERE abstract LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            keyword = keyword.toLowerCase();
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                found = true;
                String name = rs.getString("Faculty Name");
                String title = rs.getString("abstractTitle");
                String content = rs.getString("abstract");
                String email = rs.getString("Email");
                String contactNumber = rs.getString("Contact Number");
                String officeNumber = rs.getString("Office Number");
                String buildingNumber = rs.getString("Building Number");
                String officeHours = rs.getString("Office Hours");
                String alternativeContact = rs.getString("Alternative Contact");

                System.out.println("Faculty Name: " + name);
                System.out.println("Abstract Title: " + title);
                System.out.println("Abstract Content: " + content);
                System.out.println("Email: " + email);
                System.out.println("Contact Number: " + contactNumber);
                System.out.println("Office Number: " + officeNumber);
                System.out.println("Building Number: " + buildingNumber);
                System.out.println("Office Hours: " + officeHours);
                System.out.println("Alternative Contact: " + alternativeContact);
                System.out.println("------------------------");

                result.add(Arrays.asList(name, title, content, email, contactNumber, officeNumber, buildingNumber,
                        officeHours, alternativeContact));
            } 
                
            if(!found) {
                System.out.println("No faculty has an abstract that contains " + keyword + ".");
            }
        } catch (SQLException sqle) {
            System.out.println("Error while trying to search for a faculty interest.");
            System.out.println("Error message is --> " + sqle);
            sqle.printStackTrace();
        }

        return result;
    }

	// Function for outside organization to search for student or faculty interests or abstracts
	public List<List<String>> outsideOrganizationSearch(int type, String interest) {
		List<List<String>> result = new ArrayList<>();

		switch (type) {
			case 1:
				result = facultySearchStudent(interest);
				break;
			case 2:
				result = studentSearchFaculty(interest);
				break;
			case 3:
				result = studentSearchFacultyAbstract(interest);
				break;
			default:
				System.out.println("Please enter in the correct type number.");
				break;
		}
		return result;
	}

	// check how many interests student has
	public boolean studentIntNumberCheck(String username) {
		boolean result = false;
		try {
			String sql = "SELECT COUNT(*) FROM Student_Interests WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count >= 3) {
					System.out.println("Student already have 3 interests.");
					return result;
				} else if (count < 3) {
					System.out.println("Only " + (3 - count) + " interest(s) can be added. You already have " + count
							+ " interest(s).");
					result = true;
				}
			}
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to get the interest count.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			result = false;
		}
		return result;
	}

	// check how many interests faculty has
	public boolean facultyIntNumberCheck(String username) {
		boolean result = false;
		try {
			String sql = "SELECT COUNT(*) FROM Faculty_Interests WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count >= 3) {
					System.out.println("Faculty already have 3 interests.");
					return result;
				} else if (count < 3) {
					System.out.println("Only " + (3 - count) + " interest(s) can be added. You already have " + count
							+ " interest(s).");
					result = true;
				}
			}
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to get the interest count.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			result = false;
		}
		return result;
	}

	// Add upto 3 comma seperated interests to the Student_Interest table
	public boolean addStudentInterests(String username, String input_interests) {
		boolean result = false;
		String[] interests = input_interests.split(",");

		if (interests.length > 3) {
			System.out.println("Too many interests, up to 3. Please enter again.");
		} else
		for (String interest : interests) {

			System.out.println("--- add Student_Interest and Student_Interests started ---");

			int interest_ID = interestCheckStudent(interest);

			if (interest_ID == -1) {

				try {
					String sql1 = "INSERT INTO Student_Interest(keyword) VALUES (?)";
					PreparedStatement ps1 = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
					ps1.setString(1, interest);
					int rows = ps1.executeUpdate();

					try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							interest_ID = generatedKeys.getInt(1);
						} else {
							throw new RuntimeException("Can't get the interest ID.");
						}
					}
					System.out.println("You added " + rows + " row(s) to Student_Interest.");
				} catch (SQLException sqle) {
					System.out.println("Error whlie trying to insert a record into Student_Interest.");
					System.out.println("Error message is --> " + sqle);
					sqle.printStackTrace();
					result = false;
				}
			}

			try {
				String sql2 = "INSERT INTO Student_Interests(username, interest_id) VALUES (?, ?)";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setString(1, username);
				ps2.setInt(2, interest_ID);
				int rows = ps2.executeUpdate();
				System.out.println("You added " + rows + " row(s) to Student_Interests.");
				result = true;
			} catch (SQLException sqle) {
				System.out.println("Error whlie trying to insert a record into Student_Interests.");
				System.out.println("Error message is --> " + sqle);
				sqle.printStackTrace();
				result = false;
			}

		}
		return result;
	}

	// Add upto 3 comma seperated interests to the Faculty_Interest table 
	public boolean addFacultyInterests(String username, String input_interests) {
		boolean result = false;
		String[] interests = input_interests.split(",");

		if (interests.length > 3) {
			System.out.println("Too many interests, up to 3. Please enter again.");
			return false;
		}

		for (String interest : interests) {

			System.out.println("--- add Faculty_Interest and Faculty_Interests started ---");

			int interest_ID = interestCheckFaculty(interest);

			if (interest_ID == -1) {

				try {
					String sql1 = "INSERT INTO Faculty_Interest(keyword) VALUES (?)";
					PreparedStatement ps1 = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
					ps1.setString(1, interest);
					int rows = ps1.executeUpdate();

					try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							interest_ID = generatedKeys.getInt(1);
						} else {
							throw new RuntimeException("Can't get the interest ID.");
						}
					}
					System.out.println("You added " + rows + " row(s) to Faculty_Interest.");

				} catch (SQLException sqle) {
					System.out.println("Error whlie trying to insert a record into Faculty_Interest.");
					System.out.println("Error message is --> " + sqle);
					sqle.printStackTrace();
					result = false;
				}
			}

			try {
				String sql2 = "INSERT INTO Faculty_Interests(username, interest_id) VALUES (?, ?)";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setString(1, username);
				ps2.setInt(2, interest_ID);
				int rows = ps2.executeUpdate();
				System.out.println("You added " + rows + " row(s) to Faculty_Interests.");

				result = true;
			} catch (SQLException sqle) {
				System.out.println("Error whlie trying to insert a record into Faculty_Interests.");
				System.out.println("Error message is --> " + sqle);
				sqle.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	// Check if abstract already exists in the database
	private int abstractCheckFaculty(String abstractText) {
		int rows = -1;
		String sql = "SELECT abstractID FROM Abstract WHERE abstract = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, abstractText);

			try {
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					int id = rs.getInt("abstractID");
					return id;
				} else {
					return rows;
				}
			} catch (SQLException sqle) {
				System.out.println("Error whlie trying to check the abstractID.");
				System.out.println("Error message is --> " + sqle);
				sqle.printStackTrace();
				return rows;
			}
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to check the abstractID.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			return rows;
		}
	}

	// Add a faculty abstract to the database
	public boolean addFacultyAbstract(String username, String title, String abstractText) { // add abstract. search
																							// function will return both
		boolean result = false;

		System.out.println("--- add Abstract and Faculty_Abstract started ---");

		int abstractID = abstractCheckFaculty(abstractText);

		if (abstractID == -1) {

			try {
				String sql1 = "INSERT INTO Abstract(abstractTitle, abstract) VALUES (?,?)";
				PreparedStatement ps1 = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
				ps1.setString(1, title);
				ps1.setString(2, abstractText);
				int rows = ps1.executeUpdate();

				try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						abstractID = generatedKeys.getInt(1);
					} else {
						throw new RuntimeException("Can't get the abstract ID.");
					}
				}
				System.out.println("You added " + rows + " row(s) to Abstract.");
			} catch (SQLException sqle) {
				System.out.println("Error whlie trying to insert a record into Abstract.");
				System.out.println("Error message is --> " + sqle);
				sqle.printStackTrace();
				result = false;
			}
		}

		try {
			String sql2 = "INSERT INTO Faculty_Abstracts(username, abstractID) VALUES (?, ?)";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setString(1, username);
			ps2.setInt(2, abstractID);
			int rows = ps2.executeUpdate();
			System.out.println("You added " + rows + " row(s) to Faculty_Abstracts.");
			result = true;
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to insert a record into Faculty_Abstracts.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			result = false;
		}

		return result;
	}

	// Check if student interest already exists in the database
	private int interestCheckStudent(String interest) {
		int rows = -1;
		String sql = "SELECT interest_id FROM Student_Interest WHERE keyword = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, interest);

			try {
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					int id = rs.getInt("interest_id");
					return id;
				} else {
					return rows;
				}
			} catch (SQLException sqle) {
				System.out.println("Error whlie trying to check the interest ID.");
				System.out.println("Error message is --> " + sqle);
				sqle.printStackTrace();
				return rows;
			}
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to check the interest ID.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			return rows;
		}
	}

	// Check if faculty interest already exists in the database
	private int interestCheckFaculty(String interest) {
		int rows = -1;
		String sql = "SELECT interest_id FROM Faculty_Interest WHERE keyword = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, interest);

			try {
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					int id = rs.getInt("interest_id");
					return id;
				} else {
					return rows;
				}
			} catch (SQLException sqle) {
				System.out.println("Error whlie trying to check the interest ID.");
				System.out.println("Error message is --> " + sqle);
				sqle.printStackTrace();
				return rows;
			}
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to check the interest ID.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			return rows;
		}
	}

	// Insert a new record into the Outside_Organization table
	public boolean insertOutsideOrganizationRecord(String username, String name, String interest) {
		boolean result = false;
		try {
			System.out.println("--- add Outside_Organization started ---");
			String sql = "INSERT INTO Outside_Organization VALUES(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, name);
			ps.setString(3, interest);
			int rows = ps.executeUpdate();
			System.out.println("You added " + rows + " rows to Outside_Organization.");
			if (rows > 0)
				result = true;
			else
				result = false;
		} catch (SQLException sqle) {
			System.out.println("Error whlie trying to insert a record into Outside_Organization.");
			System.out.println("Error message is --> " + sqle);
			sqle.printStackTrace();
			result = false;
		}
		return result;
	}

}// end of class
