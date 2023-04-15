package init;

public class MoreSort {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

	public static void quickSort(int[] a, int leftIndex, int rightIndex)
	{
		int pIndex = partition(a, leftIndex, rightIndex);
		if(leftIndex < pIndex - 1)
			quickSort(a,leftIndex, pIndex-1);
		if(rightIndex > pIndex)
			quickSort(a,pIndex, rightIndex);
		
	}
	
	public static int partition(int[] a, int leftIndex, int rightIndex)
	{
		int i = leftIndex;
		int j = rightIndex;
		int pivot = a[(leftIndex+rightIndex)/2];
		while(i <= j)
		{
			while(a[i] < pivot)
				i++;
			while(a[j] > pivot)
				j--;
			if(i <= j)
			{
				int temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				i++;
				j--;
			}
		}
		return i;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
