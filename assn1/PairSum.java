/* PairSum.java
   CSC 225 - Spring 2017
   
   Template for the PairSum225 problem, which takes an array A and returns
	- true if there are two elements of A which add to 225
	- false otherwise
   
   The provided code for the problem (in the PairSum225 function below) implements
   a O(n^2) algorithm. A simple O( n*log(n) ) algorithm also exists, and the optimal
   algorithm is O(n).

   B. Bird & M. Simpson - 05/01/2014
*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;

public class PairSum{
	/* PairSum225()
		The input array A will contain non-negative integers. The function
		will search the array A for a pair of elements which sum to 225.
		If such a pair is found, return true. Otherwise, return false.
		The function may modify the array A.
		Do not change the function signature (name/parameters).
	*/
	public static boolean PairSum225(int[] A){
		
		
		int n = A.length;
			
		/*//Solution 1: running time is O(nlogn)
		
		//Sort the array A
		Arrays.sort(A);
		
		//For each element x of A, use binary search to check
		//whether A contains the value (225-x). If so, return true
			
			
		for (int i = 0; i < A.length; i++) {
			
			int x = A[i];
			int y = 225-x;
			//Get the index of y in ARG_IN
			int j = Arrays.binarySearch(A,y);
			
			//If j is negative, y was not found.
			//Otherwise, both x and y are in A.
			if (j >= 0) {
				return true;
			}
		}
		
		return false; */                                                    
			
		//Solution 2: running time is linear O(n) + O(n)
		
		
			//Since every element of A is non-negative, a pair(x,y))
			//which sums to 225 must have x <= 225 and y <= 225.
			
			//Create a table to keep track of which values between
			//are stored in A.
			
			//The value T[j] will be the number of times j appears
			
			//The contents of T are initialized to 0
			
			int[] T = new int [226];
			
			for(int i = 0; i < A.length; i++) {
				
				int j = A[i];
				if ((j >= 0 )&& j <= 225) 
					
					T[j]++;
	
			}
			
			//Iterate through the table T, testing for matching pairs
			
			for(int j = 0; j < n; j++) {
				
				int x = A[j];
				
				int y = 225 - A[j];
				
				if (x > 225 || y > 225) {
					continue;
				}
				
				if (x + y == 225 && T[x] > 0 && T[y] > 0) {
					System.out.printf("Pair: (%d,%d)\n", x,y);
					return true;
				}
			}
			
			return false;
			

	
		
	}

	/* main()
	   Contains code to test the PairSum225 function. 
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();
		
		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);
		
		int[] array = new int[inputVector.size()];
		
		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i);

		System.out.printf("Read %d values.\n",array.length);
		
		
		long startTime = System.currentTimeMillis();
		
		boolean pairExists = PairSum225(array);
		
		long endTime = System.currentTimeMillis();
		
		double totalTimeSeconds = (endTime-startTime)/1000.0;
		
		System.out.printf("Array %s a pair of values which add to 225.\n",pairExists? "contains":"does not contain");
		System.out.printf("Total Time (seconds): %.4f\n",totalTimeSeconds);
	}
}
