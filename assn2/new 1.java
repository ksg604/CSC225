public static void HeapSort(int[] A){

		// build the heap in an array A so that the largest value is at the root

		int len = A.length;



		// build heap by rearranging

		for (int i = (len / 2) - 1; i >= 0; i--) {

			heapify(A, len, i);

		}



		// begin sorting heap

		for (int i = len - 1; i >= 0; i--) {

			// move last element to front

			int x = A[0];

			A[0] = A[i];

			A[i] = x;

			// heapify the full array

			heapify(A, i, 0);

		}

	}
	
	public static void heapify(int[] A, int len, int root) {

		int largest = root;

		int left = 2*root + 1;

		int right = 2*root + 2;



		// if left > root and left within length

		if (left < len && A[left] > A[largest]) {

			largest = left;

		}

		// if right > largest and right within length

		if (right < len && A[right] > A[largest]) {

			largest = right;

		}



		// if largest is not the root, must sift down

		if (largest != root) {

			// swap root and largest element

			int x = A[root];

			A[root] = A[largest];

			A[largest] = x;



			// then recursively heapify starting at the largest node

			heapify(A, len, largest);

		}

	}