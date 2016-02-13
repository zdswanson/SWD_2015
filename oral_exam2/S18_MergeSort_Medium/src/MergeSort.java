import java.util.Arrays;
import java.util.Random;

/**
 * Implements a recursive merge sort
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class MergeSort {
	/**
	 * Fills an array with 100 randomly generated integers between 1-1000 and merge sorts them
	 * 
	 * @param args	no command line args expected
	 */
	public static void main(String[] args){
		int[] array=new int[100];
		Random rand=new Random();
		for(int i=0;i<100;i++){
			array[i]=rand.nextInt(1001);
		}
		System.out.println(Arrays.toString(array));
		System.out.println("\nSorted: "+Arrays.toString(mergeSort(array,100)));
	}
	
	/**
	 * Merge sort function recursively sorts a given array
	 * 
	 * @param arr	array to sort
	 * @param size	size of arr
	 * @return		sorted version of arr
	 */
	public static int[] mergeSort(int[] arr, int size){
		if(size==1){			//base case
			return arr;
		}
		int m=size/2;			//split in half
		
		int[] a=mergeSort(Arrays.copyOfRange(arr, 0, m),m);				//call function to sort left half
		int[] b=mergeSort(Arrays.copyOfRange(arr, m, size),size-m);		//and right half
		
		int i=0;
		int j=0;
		int k=0;
		
		while(i<m&&j<size-m){		//while there are still items in both left and right arrays
			if(a[i]<=b[j]){			//copy next lowest value of sorted left and right halves to next space in original array
				arr[k]=a[i];
				i++;				//increment index of array value was taken from
			}
			else{
				arr[k]=b[j];
				j++;
			}
			k++;
		}
		while(i<m){					//if there are still values left in left array (right array finished first)
			arr[k]=a[i];
			k++;
			i++;
		}
		while(j<size-m){			//if there are still values left in the right array (left array finished first)
			arr[k]=b[j];
			k++;
			j++;
		}
		/*System.out.println(Arrays.toString(a)							//displays both sorted halves and result of merge for testing
							+"\n"+Arrays.toString(b)
							+"\n"+"Merges into: "
							+"\n"+Arrays.toString(arr)+"\n");*/
		return arr;														//return sorted array
	}
}
