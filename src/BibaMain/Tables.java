package BibaMain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Tables {
	
	private Set<String> tables;
	
	public Tables(){
		tables = new HashSet<String>();
		tables.add("orders");
		tables.add("zipcodes");
		tables.add("employees");
		tables.add("parts");
		tables.add("customers");
		tables.add("odetails");
	}
	
	public void addTable(String table){	
		tables.add(table);
	}
	
	public Iterator getIter(){
		return tables.iterator();
	}

}
