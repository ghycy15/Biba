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

public class BibaRing {
	public static void select(User currUser, Tables tables, Statement stmt,
			String query) {
		ResultSet rset = null;
		
		if ((currUser.trusted).contains("Untrusted")) {
			Iterator tableIter = tables.getIter();
			List<String> views = new LinkedList<String>();

			// stmt.executeQuery("DROP VIEW ordersadmin");

			while (tableIter.hasNext()) {

				String table = (String) tableIter.next();
				// System.out.print(table + " ");
				if (query.toUpperCase().contains(table)) {

					try {
						rset = stmt.executeQuery("CREATE VIEW " + table
								+ currUser.userName
								+ " as select * from " + table
								+ " where intLevel >= "
								+ currUser.intLevel);

					} catch (Exception e) {
						e.printStackTrace();
					}

					query = query.replaceAll("(?i)" + table,
							table + currUser.userName)
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
				try {
					rset = stmt.executeQuery("DROP VIEW " + view
							+ currUser.userName);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
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
		}
	}
	
}
