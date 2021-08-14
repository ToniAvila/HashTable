package project3;

/* Toni Avila
 * 
 * Description: Implementation of a linear probing hash table in java
 * using generic types. Provided are insert, delete, find, rehash, getHashValue,
 * getLocation, and toString methods. The getHashValue method returns the hash value
 * of the given key before probing, while getLocation returns the location of the key after
 * probing. Default size of table is 10. 
 * 
 */

public class LinearProbingHashTable<K, V> {
	
	private int occupied; // keeps track of all entries (even deleted)
	private static final int DEFAULT_SIZE = 10;
	
	// get num of occupied
	public int getOccupied() {
		return this.occupied;
	}
	
	// default constructor, table size 10
	public LinearProbingHashTable() {
		this(DEFAULT_SIZE);
	}
	
	// constructor to create table of specific size
	public LinearProbingHashTable(int size) {
		allocateArray(size);
		clear();
	}
	
	// hashes key and returns value
	private int hash(K key) {
		int hashValue = key.hashCode();
		hashValue %= table.length;
		
		if(hashValue < 0)
			hashValue += table.length;
		
		return hashValue;
	}
	
	// inserts entry rehashes if half full, can re-use deleted
	// entries, throws exception if null key, returns true if inserted,
	// returns false if duplicate
	public boolean insert(K key, V value) {
		if(key == null)
			throw new IllegalArgumentException("The key cannot be null.");
		
		 int tmp = hash(key); 
	     int i = tmp; 
	  
	     // Do-while loop 
	     // Do part for performing actions 
	     do { 
	    	 	// insert if null at wanted position 
	    	 	if (table[i] == null) { 
	                table[i] = new Entry<K, V>(key, value, true);
	                occupied++; // table has +1 occupied entries
	                break; // inserted
	            } 
	    	 	
	    	    // insert if deleted element at wanted position (we can reuse)
	    	 	if (!table[i].active) { 
	                table[i] = new Entry<K, V>(key, value, true);
	                break; // done inserting
	            } 
	  
	    	 	// duplicate, return false
	            if (table[i].key.equals(key)) {
	                return false; 
	            } 
	  
	            // moving forward 1, making sure to stay within bounds of table using mod
	            i = (i + 1) % table.length; 
	  
	        }while (i != tmp); 
	        
	        
	   if(occupied > table.length / 2)
		   rehash();
	   
	   return true;
	}
	
	// returns value for key, null if not found
	public V find(K key) {
		// null arg not accepted, will end program
		if(key == null)
			throw new IllegalArgumentException("The key cannot be null.");
		
		// traversing, return value if active and found
		int i = hash(key);
		while(table[i] != null) {
			if(table[i].key.equals(key) && table[i].active)
				return table[i].value;
			
			i = (i + 1) % table.length;
		}
        
		// not found
        return null; 
	}
	
	// method to check if key is within table
	public boolean contains(K key) {
		return find(key) != null;
	}
	
	
	
	// marks entry deleted but leaves it, true if deleted,
	// false if not found
	public boolean delete(K key) {
		// null arg not accepted, will end program
		if(key == null)
			throw new IllegalArgumentException("The key cannot be null.");
		
		if(!contains(key))
			return false;
		
		// note: if key passed is already deleted, contains(key) handles this
		// and false is returned
		int i = hash(key);
		while(!table[i].key.equals(key))
			i = (i + 1) % table.length;
		
		// note that value of occupied will not change
		// as entry is still in table
		table[i].active = false;
		return true;
		
	}
	
	
	// doubles table size, hashes everything to new table,
	// omitting items marked deleted
	private void rehash() {
		// holding old table values, creating new table
		Entry<K, V> [] oldTable = table;
		allocateArray(oldTable.length * 2);
		occupied = 0;
		// theSize = 0;
		
		// enhanced for loop for traversing, will insert in new table if entry
		// was not null and was active
		for(Entry<K, V> entry: oldTable)
			if(entry != null && entry.active)
				insert(entry.key, entry.value);
	}
	
	// returns hash value for key, (value before probing occurs)
	public int getHashValue(K key) {
		// will not accept null argument
		if(key == null)
			throw new IllegalArgumentException("The key cannot be null.");
		
		if(contains(key))
			return hash(key);
		
		return -1;
		
	}
	
	// returns location for key, -1 if not found (value after probing occurs)
	public int getLocation(K key) {
		// will not except null argument
		if(key == null)
			throw new IllegalArgumentException("The key cannot be null.");
		
		if(contains(key)) {
		int i = hash(key);
		while(table[i] != null) {
			if(table[i].key.equals(key) && table[i].active)
				return i;
			
			i = (i + 1) % table.length;
			}
		}
		
		// not found
		return -1;
	}
	
	// returns formatted string of hash table, where k, v is key and value
	// at this location
	public String toString() {
		
		String output = "";
		
		for(int i = 0; i < table.length; i++) {
			if(table[i] == null) 
				output += String.format("%s\n", i);
			else {
				if(!table[i].active)
					output += String.format("%s\t%s, %s\tdeleted\n", i, table[i].key, table[i].value);
				else 
					output += String.format("%s\t%s, %s\n", i, table[i].key, table[i].value);
			}
		}
		
		return output;
			
	}
	
	private static class Entry<K, V>{
		public K key;
		public V value;
		public boolean active;
		
		public Entry(K k, V v, boolean act) {
			this.key = k;
			this.value = v;
			this.active = act;
		}
		
	}
	
	public Entry<K,V>[] table;
	
	private void allocateArray(int size) {
		table = new Entry[size];
	}
	
	private void clear() {
		occupied = 0;
		for(int i = 0; i < table.length; i++) {
			table[i] = null;
		}
	}
	
	
	public static void main(String[] args) {
		// testing a int key, string value table. we use the default constructor so size
		// will initially be 10 (index 0-9)
		LinearProbingHashTable<Integer, String> t = new LinearProbingHashTable<>();
		
		// test cases for null key arguments
		//t.insert(null,  "nullkey"); // null key, exception thrown
		//t.delete(null); // null key, exception thrown
		//t.getHashValue(null); // null key, exception thrown
		//t.getLocation(null); // null key, exception thrown
		
		// inserting 4
		t.insert(4, "12");
		
	    // testing insert
		System.out.println("Testing insert method: ");
		if(t.insert(5, "Henry"))
			System.out.println("5 inserted");
		
		// testing duplicate insert, should not insert as it is a duplicate
		if(t.insert(5, "Thomas"))
			System.out.println("5 inserted again");
		else
			System.out.println("5 not inserted, duplicate");
		
		System.out.println("-------------"); // divider
		
		System.out.println("Testing find method:");
		// testing find method, 2 is not in table so message should be printed below
		String check = t.find(2);
		if(check == null)
			System.out.println("2 not found");
		
		// testing find method, 5 is in table, should return "24"
		String check2 = t.find(5);
		if(check2 != null)
			System.out.println("5 was found, its value is: " + check2);
		
		System.out.println("-------------");
		
		// testing delete with valid key
		System.out.println("Testing delete method (valid key): ");
		if(t.delete(4))
			System.out.println("4 was deleted.");
		
		System.out.println("Testing delete method (key not in table): ");
		if(!t.delete(45))
			System.out.println("key 45 was not found, not possible to delete");
		
		System.out.print(t.toString()); // will show 4 as deleted
		
		System.out.println("-------------");
	
		// reusing a deleted key's position
		System.out.println("Reusing deleted entry 4's position: ");
		t.insert(4, "Reusing4");
		System.out.print(t.toString());
		
		
		// note: table will still be of size 10, as occupied is not greater than 5
		
		// now we insert in order to test rehash
		t.insert(24, "James");
		t.insert(20, "Theodore");
		t.insert(13,  "Jake");
		// printing before our rehash occurs, as our occupied is at 5 which is
		// not greater than 10/2. Also testing getHashValue and getLocation before rehash 
		System.out.println("-------------");
		System.out.println("Before rehash: ");
		// hashVal should be 13 % 10 = 3, location should be 3 as well
		System.out.println("getHashValue(13) = " + t.getHashValue(13));
		System.out.println("getLocation(13) = " + t.getLocation(13));
		// hashVal should be 24 % 10 = 4, location should be 6 after probing
		System.out.println("getHashValue(24) = " + t.getHashValue(24));
		System.out.println("getLocation(24) = " + t.getLocation(24));
		System.out.print(t.toString());
				
		
		// this insert will cause a rehash, making our table size now 20 (2*10)
		t.insert(2, "Ullr");
		
		System.out.println("-------------");
		System.out.println("After rehash: ");
		
		// hashVal should now be 13 % 20 = 13, location should be 13 as well after probing now
		System.out.println("getHashValue(13) = " + t.getHashValue(13));
		System.out.println("getLocation(13) = " + t.getLocation(13));
		
		// hashVal should be 24 % 10 = 4, location should still be 6 after probing
	    System.out.println("getHashValue(24) = " + t.getHashValue(24));
	    System.out.println("getLocation(24) = " + t.getLocation(24));
		System.out.println("-------------");
		
		// testing for key not in table, should return -1 for both
		System.out.println("Testing getHashValue and getLocation for key not in table:");
		System.out.println("getHashValue(67) = " + t.getHashValue(67));
	    System.out.println("getLocation(67) = " + t.getLocation(67));
		System.out.println("-------------");
		System.out.print(t.toString());
		
		System.out.println("-------------");
		
		/*  segment of code to further test probing if needed, key 22 should be inserted at position 7
		t.insert(3, "hui");
		t.insert(22, "should be at 7");
		System.out.println(t.getHashValue(22));
		System.out.println(t.getLocation(22));
		System.out.print(t.toString());
		*/
		
		
	}
	

}
