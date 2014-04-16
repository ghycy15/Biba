package BibaMain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Tables {
    
    private Set<String> tables;
    
    public Tables(){
        tables = new HashSet<String>();
    }
    
    public void addTable(String table){    
        tables.add(table);
    }
    
    public Iterator<String> getIter(){
        return tables.iterator();
    }
    
    public void deleteTable(String table){
        tables.remove(table);
    }

}
