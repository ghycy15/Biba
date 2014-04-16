package BibaMain;

import java.sql.*;
import java.io.*;

import BibaOperation.BibaStrict;

public class BibaMain {

	private static User currUser;

	public static void main(String args[]) throws SQLException, IOException {
		String user;

		Connection conn = DBconnection.DBconnection();

		// Create a statement
		Statement stmt = conn.createStatement();

		user = readEntry("user: ");

		currUser = new User(user, -1, "Untrusted");
		ResultSet rset = stmt
				.executeQuery("select intLevel,trusted from users where userName ="
						+ "\'" + user + "\'");

		while (rset.next()) {
			currUser.intLevel = Integer.parseInt(rset.getString(1));
			currUser.trusted = rset.getString(2);
			System.out.println("Your integrity level is: "+Integer.parseInt(rset.getString(1)));
			System.out.println("Subject catalog: "+currUser.trusted);
		}

		if (currUser.intLevel == -1) {
			System.out.println("invalid username\n Exiting...");
			rset.close();
			stmt.close();
			conn.close();
			System.exit(0);
		}

		Tables tables = new Tables();
		rset = stmt.executeQuery("SELECT table_name FROM user_tables");

		while (rset.next()) {
			tables.addTable(rset.getString(1));
		}

		while (true) {
			String query = readEntry("input query: ");

			if (query.equals("quit")) {
				rset.close();
				stmt.close();
				conn.close();
				System.exit(0);
			}
			
			if (query.toLowerCase().contains("insert")) {
				BibaStrict.insert(currUser, tables, stmt, query);
			} else if (query.toLowerCase().startsWith("drop table")) {
				BibaStrict.drop(currUser, tables, stmt, query);
			} else if (query.toLowerCase().startsWith("update")) {
				BibaStrict.update(currUser, tables, stmt, query);
			} else if (query.toLowerCase().startsWith("select")) {
				BibaStrict.select(currUser, tables, stmt, query);
			} else if (query.toLowerCase().startsWith("create table")) {
				BibaStrict.create(currUser, tables, stmt, query);
			}
		}
	}

	// Utility function to read a line from standard input
	static String readEntry(String prompt) {
		try {
			StringBuffer buffer = new StringBuffer();
			System.out.print(prompt);
			System.out.flush();
			int c = System.in.read();
			while (c != '\n' && c != -1) {
				buffer.append((char) c);
				c = System.in.read();
			}
			return buffer.toString().trim();
		} catch (IOException e) {
			return "";
		}
	}
}