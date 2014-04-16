package BibaOperation;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import BibaMain.Tables;
import BibaMain.User;

public class BibaStrict {

	public static void select(User currUser, Tables tables, Statement stmt,
			String query) {
		Iterator tableIter = tables.getIter();
		List<String> views = new LinkedList<String>();

		ResultSet rset;

		while (tableIter.hasNext()) {

			String table = (String) tableIter.next();
			// System.out.print(table + " ");
			if (query.toUpperCase().contains(table)) {

				try {
					rset = stmt.executeQuery("CREATE VIEW " + table
							+ currUser.userName + " as select * from " + table
							+ " where intLevel >= " + currUser.intLevel);

				} catch (Exception e) {
					e.printStackTrace();
				}

				query = query.replaceAll("(?i)" + table,
						table + currUser.userName).replace("\n", "");

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
			try {
				rset = stmt.executeQuery("DROP VIEW " + view
						+ currUser.userName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void create(User currUser, Tables tables, Statement stmt,
			String query) {
		try {
			stmt.executeUpdate(query);
			String tableName = query.split(" ")[2];
			stmt.executeUpdate("ALTER TABLE " + tableName
					+ " ADD intlevel integer");
			stmt.executeUpdate("INSERT INTO tableInt values ('" + tableName
					+ "'," + currUser.intLevel + ")");
			tables.addTable(tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(User currUser, Tables tables, Statement stmt,
			String query) {
		ResultSet rset = null;
		if (query.toLowerCase().contains("select")) {
			String queryparts = query.substring(0, query.indexOf('='));
			String querypartsone = query.substring(query.indexOf('='),
					query.length());
			String subqueryzero = querypartsone.substring(
					querypartsone.indexOf('='), querypartsone.indexOf(')'));
			String subqueryone = querypartsone.substring(
					querypartsone.indexOf(')'), querypartsone.length());
			// first divide the query using the first occurence of =,
			// then divide the query using ')'
			// -----------------------------huyue-----------

			Iterator tableIter = tables.getIter();
			List<String> views = new LinkedList<String>();

			// stmt.executeQuery("DROP VIEW ordersadmin");

			while (tableIter.hasNext()) {

				String table = (String) tableIter.next();
				// System.out.print(table + " ");
				if (queryparts.toUpperCase().contains(table)) { //

					try {
						rset = stmt.executeQuery("CREATE VIEW " + table
								+ currUser.userName + "towrite"
								+ " as select * from " + table
								+ " where intLevel <= " + currUser.intLevel);

					} catch (Exception e) {
						e.printStackTrace();
					}

					queryparts = queryparts.replaceAll("(?i)" + table,
							table + currUser.userName + "towrite").replace(
							"\n", "");

					views.add(table);
				}
			}

			// for subqueryzero
			Iterator tableItertwo = tables.getIter();
			List<String> viewstwo = new LinkedList<String>();

			while (tableItertwo.hasNext()) {

				String tabletwo = (String) tableItertwo.next();
				// System.out.print(table + " ");
				if (subqueryzero.toUpperCase().contains(tabletwo)) { //

					try {
						rset = stmt.executeQuery("CREATE VIEW " + tabletwo
								+ currUser.userName + " as select * from "
								+ tabletwo + " where intLevel >= "
								+ currUser.intLevel);

					} catch (Exception e) {
						e.printStackTrace();
					}

					subqueryzero = subqueryzero.replaceAll("(?i)" + tabletwo, // queryparts[0]
																				// contains
																				// all
																				// the
																				// strings
																				// before
																				// set;
							tabletwo + currUser.userName).replace("\n", "");

					viewstwo.add(tabletwo);
				}
			}

			query = queryparts + subqueryzero + subqueryone;
			System.out.println(query);
			System.out.flush();

			try {
				stmt.executeUpdate(query);

			} catch (Exception e) {
				e.printStackTrace();
			}

			for (String view : views) {
				try {
					rset = stmt.executeQuery("DROP VIEW " + view // views
																	// list
																	// records
																	// table,
																	// drop
																	// views
							+ currUser.userName + "towrite");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (String view : viewstwo) {
				try {
					rset = stmt.executeQuery("DROP VIEW " + view // views
																	// list
																	// records
																	// table,
																	// drop
																	// views
							+ currUser.userName);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		else { // query without select----------------------------update
				// easy form

			String[] queryparts = query.replace("set", "SET").split("SET");

			Iterator tableIter = tables.getIter();
			List<String> views = new LinkedList<String>();

			// stmt.executeQuery("DROP VIEW ordersadmin");

			while (tableIter.hasNext()) {

				String table = (String) tableIter.next();
				// System.out.print(table + " ");
				if (queryparts[0].toUpperCase().contains(table)) { //

					try {
						rset = stmt.executeQuery("CREATE VIEW " + table
								+ currUser.userName + "towrite"
								+ " as select * from " + table
								+ " where intLevel <= " + currUser.intLevel);

					} catch (Exception e) {
						e.printStackTrace();
					}

					queryparts[0] = queryparts[0].replaceAll("(?i)" + table, // queryparts[0]
																				// contains
																				// all
																				// the
																				// strings
																				// before
																				// set;
							table + currUser.userName + "towrite").replace(
							"\n", "");

					views.add(table);
				}
			}

			query = queryparts[0] + " set " + queryparts[1];
			System.out.println(query);
			System.out.flush();

			try {
				stmt.executeUpdate(query);

			} catch (Exception e) {
				e.printStackTrace();
			}

			for (String view : views) {
				try {
					rset = stmt.executeQuery("DROP VIEW " + view // views
																	// list
																	// records
																	// table,
																	// drop
																	// views
							+ currUser.userName + "towrite");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} // end of easy update mode
	}

	public static void drop(User currUser, Tables tables, Statement stmt,
			String query) {
		ResultSet rset = null;
		String tableName = query.split(" ")[2];
		try {
			rset = stmt.executeQuery("select MAintlevel) from " + tableName);

			int maxInt = -1;
			if (rset.first()) {
				maxInt = rset.getInt(1);
			}

			if (currUser.intLevel > maxInt) {
				stmt.executeUpdate(query);
				tables.deleteTable(tableName);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insert(User currUser, Tables tables, Statement stmt,
			String query) {

		ResultSet rset = null;
		if (query.toLowerCase().contains("users")) {

		} else {

			Iterator tableIter = tables.getIter();

			while (tableIter.hasNext()) {

				String table = (String) tableIter.next();
				// System.out.println(table + " ");
				if (query.toUpperCase().contains(table)) {
					try {
						System.out.println("SELECT INTLEVEL " + "from TABLEINT"
								+ " where TABLENAME =" + "'"
								+ table.toUpperCase() + "'");
						rset = stmt.executeQuery("SELECT INTLEVEL "
								+ "from TABLEINT" + " where TABLENAME =" + "'"
								+ table.toUpperCase() + "'");

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			int tablelevel = -1;
			try {
				while (rset.next()) {
					tablelevel = Integer.parseInt(rset.getString(1));
					// System.out.println(rset.getString(1));
				}

				if (tablelevel <= currUser.intLevel && tablelevel != -1) {
					query = query.substring(0, query.lastIndexOf(')')) + ","
							+ currUser.intLevel + ")";
					// System.out.println(query);
					stmt.executeUpdate(query);
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
