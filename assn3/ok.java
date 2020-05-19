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

public class ok{

     //The size of the hash table.
     int TableSize;

     //The current number of elements in the hash table.
     int NumElements;

  //The variable T represents the array used for the table.
  // DECLARE YOUR TABLE DATA STRUCTURE HERE
    HashNode[] T;

  public ok(){
       NumElements = 0;
       TableSize = 997;
      // TableSize = 5;
       // INITIALZE YOUR TABLE DATA STRUCTURE HERE

        T = new HashNode[TableSize];
  }

    // method for testing
    public void pr(Object obj) {
        System.out.println(obj);
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
    int hashVal = 0;
    for (int i = 0; i < s.length(); i++) {
      hashVal += powerOf3(i) * s.charAt(i);
    }
    return hashVal % TableSize;
  }

    /*  powerOf3(power, divisor)
        Return a power of 3 iteratively computed using modular arithmetic to prevent overflows
    */
    private int powerOf3(int power) {
      double value = 3.0;
      if (power % 2 != 0) {
        value = value * 3.0;
        power--;
      }
      for (int i = 1; i <= power; i++) {
        value = (Math.pow(value, 2) % TableSize);
      }
      return (int) value;
    }

  /* insert(s)
     Insert the value s into the hash table and return the index at
     which it was stored.
  */
  public int insert(String s){
        int tmp = hash(s);
        int i = tmp, h = 1;
        double newLoadValue = ((double) NumElements + 1) / TableSize;
        // if table is too large, resize to be 5x larger
        while (newLoadValue > 0.75) {
            resizeHandler(5*TableSize);
            newLoadValue = ((double) NumElements + 1) / TableSize;
        }
        do {
            if (T[i] == null || T[i].getDeletedStatus() == true) {
                T[i] = new HashNode(s);
                NumElements++;
                return i;
            }
            if (T[i].equals(s)) {
                return i;
            }
            i = (i + h * h++) % TableSize;
        } while (i != tmp);
        pr("ERR: ARRAY FULL");
        return -1; // Shouldn't EVER get here!
  }

  /* find(s)
     Search for the string s in the hash table. If s is found, return
     the index at which it was found. If s is not found, return -1.
  */
  public int find(String s){
        int tmp = hash(s);
        int i = tmp, h = 1;
        do {
          if (T[i] == null) {
            return -1;
          }
          if (T[i].getValue().equals(s)) {
            return i;
          }
          i = (i + h * h++) % TableSize;
        } while (i != tmp);
        // else
        return -1;
  }

  /* remove(s)
     Remove the value s from the hash table if it is present. If s was removed,
     return the index at which it was removed from. If s was not removed, return -1.
  */
  public int remove(String s){
    int x = find(s);
     if (((double) NumElements - 1) / TableSize <= 0.25) {
    	resizeHandler(TableSize / 2);
    }
    if (x > -1) {
      T[x].delString();
      NumElements--;
      return x;
    } else {
      return -1;
    }

  }

  public void resizeHandler(int size) {
      // generate prime number greater than size
      int temp = size;
      boolean notPrime = true;
      // prime test adapted from http://stackoverflow.com/questions/14650360/very-simple-prime-number-test-i-think-im-not-understanding-the-for-loop

      prime: while (notPrime) {
          // fast even test
          if (temp > 2 && (temp & 1) == 0) {
              temp += 1;
              continue;
          }
          // only odd factors need to be tested up to n^0.5
          for (int i = 3; i * i <= temp; i += 2) {
            if (temp % i == 0) {
              temp += 1;
              continue prime;
            }
            notPrime = false;
          }
      }
      resize(temp);
      return;
  }

  /* resize()
     Resize the hash table to be a prime within the load factor requirements.
  */
  public void resize(int newSize){
    // Get copy of existing data
    HashNode[] old_table = T.clone();

    // delete existing data from T
    for (int i = 0; i < TableSize; i++) {
      T[i] = null;
    }
    // resize array
    TableSize = newSize;
    T = new HashNode[TableSize];
    // copy data
    for (int i = 0; i < old_table.length; i++) {
    	// entry exists and is not deleted
      if (old_table[i] != null && old_table[i].getDeletedStatus() != true) {
        insert(old_table[i].getValue());
      }
    }
    return;
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


class HashNode {

  private boolean wasDeleted;
  private String value;

  public HashNode(String givenValue) {
    wasDeleted = false;
    value = givenValue;
  }

  public String getValue() {
        if (!wasDeleted) return value;
        else return null;
  }

  public boolean getDeletedStatus() {
    return wasDeleted;
  }

    public void delString(){
        if (!wasDeleted) wasDeleted = true;
    }

    public boolean equals(String s) {
        return value == s;
    }
}