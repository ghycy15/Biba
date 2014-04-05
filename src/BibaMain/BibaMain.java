package BibaMain;

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
import java.io.*;

public class BibaMain {

	private static User currUser;

	public static void main(String args[]) throws SQLException, IOException {
		String user;

		Connection conn = DBconnection.DBconnection();

		// Create a statement
		Statement stmt = conn.createStatement();

		user = readEntry("user: ");

		currUser = new User(user, -1);
		ResultSet rset = stmt
				.executeQuery("select intLevel from users where userName ="
						+ "\'" + user + "\'");

		while (rset.next()) {
			currUser.intLevel = Integer.parseInt(rset.getString(1));
			System.out.println(Integer.parseInt(rset.getString(1)));
		}

		if (currUser.intLevel == -1) {
			System.out.println("invalid username\n Exiting...");
			rset.close();
			stmt.close();
			conn.close();
			System.exit(0);
		}

		Tables tables = new Tables();

		while (true) {
			String query = readEntry("input query: ");

			if (query.equals("quit")) {
				rset.close();
				stmt.close();
				conn.close();
				System.exit(0);
			}

			if (query.toLowerCase().contains("insert")) {
				if (query.toLowerCase().contains("users")) {

				} else {
					query = query.substring(0, query.lastIndexOf(')')) + ","
							+ currUser.intLevel + ")";
				}

				rset = stmt.executeQuery(query);

			} else {

				Iterator tableIter = tables.getIter();
				List<String> views = new LinkedList<String>();

				// stmt.executeQuery("DROP VIEW ordersadmin");

				while (tableIter.hasNext()) {
					String table = (String) tableIter.next();
					// System.out.print(table + " ");
					if (query.toLowerCase().contains(table)) {

						try {
							rset = stmt.executeQuery("CREATE VIEW " + table
									+ currUser.userName + " as select * from "
									+ table + " where intLevel <= "
									+ currUser.intLevel);

						} catch (Exception e) {
							e.printStackTrace();
						}
						query = query.toLowerCase()
								.replace(table, table + currUser.userName)
								.replace("\n", "");
						views.add(table);
					}
				}

				System.out.println(query);
				System.out.flush();

				try {
					rset = stmt.executeQuery(query);

					ResultSetMetaData rsmd = rset.getMetaData();

					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						System.out.print(rsmd.getColumnName(i) + " ");
					}
					System.out.println();
					while (rset.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							System.out.print(rset.getString(i) + " ");
						}
						System.out.println();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (String view : views) {
					rset = stmt.executeQuery("DROP VIEW " + view
							+ currUser.userName);
				}
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