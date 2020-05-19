/* HashTable.java
   CSC 225 - Spring 2017
   Template for string hashing
   
   =================
   
   Modify the code below to use quadratic probing to resolve collisions.
   
   Your task is implement the hash, insert, find, remove, and resize methods for the hash table.
   
   The load factor should always remain in the range [0.25,0.75] and the tableSize should always be prime.
   
   =================
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java HashTable
	
   Input data should consist of a list of strings to insert into the hash table, one per line,
   followed by the token "###" on a line by itself, followed by a list of strings to search for,
   one per line, followed by the token "###" on a line by itself, followed by a list of strings to remove,
   one per line.
	
   To conveniently test the algorithm with a large input, create
   a text file containing the input data and run the program with
	java HashTable file.txt
   where file.txt is replaced by the name of the text file.

   B. Bird - 07/04/2015
   M. Simpson - 21/02/2016
*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.lang.Math;

public class HashTable{

     //The size of the hash table.
     int TableSize;
     
     //The current number of elements in the hash table.
     int NumElements;
	
	//The variable T represents the array used for the table.
	// DECLARE YOUR TABLE DATA STRUCTURE HERE
	 HashEntry[] T;
	
	
	public HashTable(){
     	NumElements = 0;
     	TableSize = 997;
		T = new HashEntry[TableSize];
     	// INITIALZE YOUR TABLE DATA STRUCTURE HERE
	}
	
	/* hash(s) = ((3^0)*s[0] + (3^1)*s[1] + (3^2)*s[2] + ... + (3^(k-1))*s[k-1]) mod TableSize
	   (where s is a string, k is the length of s and s[i] is the i^th character of s)
	   Return the hash code for the provided string.
	   The returned value is in the range [0,TableSize-1].

	   NOTE: do NOT use Java's built in pow function to compute powers of 3. To avoid integer
	   overflows, write a method that iteratively computes powers and uses modular arithmetic
	   at each step.
	*/
	public int hash(String s){
		return computePowerOf(3,s);
	}
	
	
	private int computePowerOf(int powersOf, String s) {
	//hashCode is the hash code of the string
	int hashCode = 0;
	//computedPower is a power of an integer raised to the length of the string
	int computedPower = 0;
	int temp = powersOf;
		
		
	for(int i = 0; i < s.length(); i++) {
			
		computedPower = computedPower * temp;
			
		if (i == 0) {
			computedPower = 1;
		}		
			
		hashCode = hashCode +  ((computedPower%TableSize * (s.charAt(i)%TableSize))) % TableSize;
	}
	return hashCode % TableSize;
	}
	
	/* insert(s)
	   Insert the value s into the hash table and return the index at 
	   which it was stored.
	*/
public int insert(String s){
		int i = hash(s);
		int j = 0;
		double loadFactor = NumElements/ TableSize;
		
		while(loadFactor > 0.75) {
			resize2(2*TableSize);
			loadFactor = NumElements / TableSize;
		}
		
		while(T[i] != null) {
			i = (i + (j*j)) % TableSize;
			if(i >= TableSize) {
				i = i % TableSize;
			}
			if(T[i].getStatus() == true || T[i] == null) {
				T[i] = new HashEntry(s);
				NumElements++;
				return i;
			}
			j++;
		}
		return 0;
		
	}
	
	/* find(s)
	   Search for the string s in the hash table. If s is found, return
	   the index at which it was found. If s is not found, return -1.
	*/
	public int find(String s){
		int i = hash(s);
		int j = 0;
		while(true) {

			if(T[i] == null) {
				return -1;
			}
			if(T[i].getValue() == s) {
				return i;
			}
			i = i +(j*j);
			if(i >= TableSize) {
				i = i % TableSize;
			}
			j++;
		}
	}
	
	/* remove(s)
	   Remove the value s from the hash table if it is present. If s was removed, 
	   return the index at which it was removed from. If s was not removed, return -1.
	*/
	public int remove(String s){
		int removeIndex = find(s);
		if (((NumElements -1) / TableSize) < 0.25) {
			resize2(TableSize/2);
		}
		
		//If removeIndex has an item, mark the location as deleted and delete the value.
		if(removeIndex != (-1)) {
			T[removeIndex].setDeleted();
			NumElements--;
			return removeIndex;
		}
     	return -1;
	}
	
	/* resize()
	   Resize the hash table to be a prime within the load factor requirements.
	*/
	public void resize(int newSize){
		
		HashEntry[] oldTable = T;
		
		TableSize = newSize;
		
		T = new HashEntry[newSize];

		for(int i = 0; i < oldTable.length; i++) {
			if(oldTable[i] != null && (oldTable[i].getStatus() != true)) {
				insert(oldTable[i].getValue());
			}
		}
		return;
	}
	
	public boolean checkPrime(int tableSize) {
		for(int i = 2; i < tableSize / 2; i++) {
			if(tableSize % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public void resize2(int isPrimeSize) {
		
		while(true) {
			if(checkPrime(isPrimeSize) == false) {
				isPrimeSize++;
			}
			if(checkPrime(isPrimeSize) == true) {
				break;
			}
		}
		
		resize(isPrimeSize);
	
	}
	/*HashEntry class will help to keep track maintain a status and value for each location
	 *in the table.
	 */
	public static class HashEntry {
		
		private String value;
		
		//Status is false if entry was not deleted and true if deleted.  Default is false.
		private boolean status;
		
		public HashEntry(String s) {
			value = s;
			status = false;
			
		}
		
		public boolean getStatus() {
			return status;
		}
		
		public void toggleStatus() {
			if(status == false) {
				status = true;
			}else if(status == true) {
				status = false;
			}
		}
		
		public boolean setDeleted() {
			return status = true;
		}
		
		public String getValue() {
			if(status == false) {
				return value;
			}else{
				return null;
			}
		}
		
		
	}
	
	
	/* **************************************************** */
	
	/* main()
	   Contains code to test the hash table methods. 
	*/
	public static void main(String[] args){
		Scanner s;
		boolean interactiveMode = false;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			interactiveMode = true;
			s = new Scanner(System.in);
		}
		s.useDelimiter("\n");
		if (interactiveMode){
			System.out.printf("Enter a list of strings to store in the hash table, one per line.\n");
			System.out.printf("To end the list, enter '###'.\n");
		}else{
			System.out.printf("Reading table values from %s.\n",args[0]);
		}
		
		Vector<String> tableValues = new Vector<String>();
		Vector<String> searchValues = new Vector<String>();
		Vector<String> removeValues = new Vector<String>();
		
		String nextWord;
		
		while(s.hasNext() && !(nextWord = s.next().trim()).equals("###"))
			tableValues.add(nextWord);
		System.out.printf("Read %d strings.\n",tableValues.size());
		
		if (interactiveMode){
			System.out.printf("Enter a list of strings to search for in the hash table, one per line.\n");
			System.out.printf("To end the list, enter '###'.\n");
		}else{
			System.out.printf("Reading search values from %s.\n",args[0]);
		}	
		
		while(s.hasNext() && !(nextWord = s.next().trim()).equals("###"))
			searchValues.add(nextWord);
		System.out.printf("Read %d strings.\n",searchValues.size());
		
		if (interactiveMode){
			System.out.printf("Enter a list of strings to remove from the hash table, one per line.\n");
			System.out.printf("To end the list, enter '###'.\n");
		}else{
			System.out.printf("Reading remove values from %s.\n",args[0]);
		}
		
		while(s.hasNext() && !(nextWord = s.next().trim()).equals("###"))
			removeValues.add(nextWord);
		System.out.printf("Read %d strings.\n",removeValues.size());
		
		HashTable H = new HashTable();
		long startTime, endTime;
		double totalTimeSeconds;
		
		startTime = System.currentTimeMillis();

		for(int i = 0; i < tableValues.size(); i++){
			String tableElement = tableValues.get(i);
			long index = H.insert(tableElement);
		}
		endTime = System.currentTimeMillis();
		totalTimeSeconds = (endTime-startTime)/1000.0;
		
		System.out.printf("Inserted %d elements.\n Total Time (seconds): %.2f\n",tableValues.size(),totalTimeSeconds);
		
		int foundCount = 0;
		int notFoundCount = 0;
		startTime = System.currentTimeMillis();

		for(int i = 0; i < searchValues.size(); i++){
			String searchElement = searchValues.get(i);
			long index = H.find(searchElement);
			if (index == -1)
				notFoundCount++;
			else
				foundCount++;
		}
		endTime = System.currentTimeMillis();
		totalTimeSeconds = (endTime-startTime)/1000.0;
		
		System.out.printf("Searched for %d items (%d found, %d not found).\n Total Time (seconds): %.2f\n",
							searchValues.size(),foundCount,notFoundCount,totalTimeSeconds);
							
		int removedCount = 0;
		int notRemovedCount = 0;
		startTime = System.currentTimeMillis();

		for(int i = 0; i < removeValues.size(); i++){
			String removeElement = removeValues.get(i);
			long index = H.remove(removeElement);
			if (index == -1)
				notRemovedCount++;
			else
				removedCount++;
		}
		endTime = System.currentTimeMillis();
		totalTimeSeconds = (endTime-startTime)/1000.0;
		
		System.out.printf("Tried to remove %d items (%d removed, %d not removed).\n Total Time (seconds): %.2f\n",
							removeValues.size(),removedCount,notRemovedCount,totalTimeSeconds);
	}
}
