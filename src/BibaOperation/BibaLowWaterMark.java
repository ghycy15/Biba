package BibaOperation;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import BibaMain.Tables;
import BibaMain.User;

public class BibaLowWaterMark {
	public static void select(User currUser, Tables tables, Statement stmt,
			String query) {

		ResultSet rset = null;
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

			int minInt = -1;

			String newQuery = "select * "
					+ query.substring(query.toLowerCase().indexOf("from"),
							query.length());

			if (newQuery.toLowerCase().contains("group by")) {
				newQuery = newQuery.substring(0, newQuery.toLowerCase()
						.indexOf("group by"));
			}

			if (newQuery.toLowerCase().contains("order by")) {
				newQuery = newQuery.substring(0, newQuery.toLowerCase()
						.indexOf("order by"));
			}

			if (newQuery.contains("(")) {
				String subQuery = query.substring(
						query.toLowerCase().indexOf("("), query.toLowerCase()
								.indexOf(")"));
				if (subQuery.toLowerCase().contains("select")) {
					subQuery = "select * "
							+ query.substring(
									query.toLowerCase().indexOf("from"),
									query.length());

					rset = stmt.executeQuery(subQuery);

					rsmd = rset.getMetaData();

					Set<Integer> intlevels = new HashSet<Integer>();

					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						if (rsmd.getColumnName(i).equals("INTLEVEL")) {
							intlevels.add(i);
						}
						// System.out.print(rsmd.getColumnName(i) + " ");
					}

					while (rset.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							// System.out.print(rset.getString(i) + " ");
							if (intlevels.contains(i)) {
								if (Integer.parseInt(rset.getString(i)) < minInt
										|| minInt == -1) {
									minInt = Integer
											.parseInt(rset.getString(i));
								}
							}
						}

					}

				}
			}

			System.out.println(newQuery);
			rset = stmt.executeQuery(newQuery);

			rsmd = rset.getMetaData();

			Set<Integer> intlevels = new HashSet<Integer>();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				if (rsmd.getColumnName(i).equals("INTLEVEL")) {
					intlevels.add(i);
				}
				// System.out.print(rsmd.getColumnName(i) + " ");
			}

			while (rset.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// System.out.print(rset.getString(i) + " ");
					if (intlevels.contains(i)) {
						if (Integer.parseInt(rset.getString(i)) < minInt
								|| minInt == -1) {
							minInt = Integer.parseInt(rset.getString(i));
						}
					}
				}

			}

			System.out.println("minInt:" + minInt + currUser.intLevel);

			if (minInt < currUser.intLevel) {
				currUser.intLevel = minInt;
				System.out.println("UPDATE USERS SET INTLEVEL = " + minInt
						+ " WHERE USERNAME = '" + currUser.userName + "'");
				stmt.executeUpdate("UPDATE USERS SET INTLEVEL = " + minInt
						+ " WHERE USERNAME = '" + currUser.userName + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
