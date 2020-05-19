/* HeapSort.java
   CSC 225 - Spring 2017
   Assignment 2 - Template for HeapSort
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java HeapSort

   To conveniently test the algorithm with a large input, create a 
   text file containing space-separated integer values and run the program with
	java HeapSort file.txt
   where file.txt is replaced by the name of the text file.

   M. Simpson & B. Bird - 11/16/2015
*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;

//Student Name: Kevin San Gabriel
//Student ID  : V00853258
//Do not change the name of the HeapSort class
public class HeapSort{
	public static final int max_values = 1000000;

	/* HeapSort(A)
		Sort the array A using an array-based heap sort algorithm.
		You may add additional functions (or classes) if needed, but the entire program must be
		contained in this file. 

		Do not change the function signature (name/parameters).
	*/
	
	/*Sorts the array after it has been converted to a heap
	 */
	public static void HeapSort(int[] A){
		
		//Build heap on array A
		buildHeap(A);
		
		//Swap the last and first items in the array then heapify until the array is sorted.
		for (int i = A.length - 1; i >= 0; i--) {	
			swap(0,i,A);
			maxHeap(A, i, 0);
		}
	}	
	
	/*Swap method helper method.  Sorts two elements in an array
	 */
	private static void swap(int i, int k, int[] A) {
		int tmp = A[i];
		A[i] = A[k];
		A[k] = tmp;
	}
			
	/*Helper method which builds a heap by iterating through the array
	 *and calling the maxHeap method for every parent index.
	 */
	private static void buildHeap(int[] A) {
		for (int i = (A.length/2) - 1; i >= 0; i--) {
			maxHeap(A,A.length,i);
		}
	}
	
	/*Heapify helper method creating a maxHeap. 
	 *This method works by swapping the largest element between a parent node
	 *and its children.
	 */
	private static void maxHeap(int[] A, int length, int parent) {

		int child = 2 * parent + 1;
		int rightChild = 2 * parent + 2;
		int max = parent;
		
		//Compare left child to parent.  If it is greater, then left child is the max 
		//else the parent node is still the max.
		if (child < length && A[child] > A[max]) {
			max = child;
		}else{
			max = parent;
		}
		
		//Compare rightChild to parent.  If it is greater, then rightChild is the max 
		//else the parent node is still the max.
		if (rightChild < length && A[rightChild] > A[max]) {
			max = rightChild;
		}
		
		//If the parent is not the max node anymore then swap parent with the new max and recur.
		if (max != parent) {			
			swap (parent,max,A);
			maxHeap(A, length, max);
		}
	}
	

	/* main()
	   Contains code to test the HeapSort function. Nothing in this function 
	   will be marked. You are free to change the provided code to test your 
	   implementation, but only the contents of the HeapSort() function above 
	   will be considered during marking.
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

		HeapSort(array);

		long endTime = System.currentTimeMillis();

		double totalTimeSeconds = (endTime-startTime)/1000.0;

		//Don't print out the values if there are more than 100 of them
		if (array.length <= 100){
			System.out.println("Sorted values:");
			for (int i = 0; i < array.length; i++)
				System.out.printf("%d ",array[i]);
			System.out.println();
		}

		//Check whether the sort was successful
		boolean isSorted = true;
		for (int i = 0; i < array.length-1; i++)
			if (array[i] > array[i+1])
				isSorted = false;

		System.out.printf("Array %s sorted.\n",isSorted? "is":"is not");
		System.out.printf("Total Time (seconds): %.2f\n",totalTimeSeconds);
	}
}
