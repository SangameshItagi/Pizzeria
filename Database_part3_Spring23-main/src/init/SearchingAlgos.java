package init;

public class SearchingAlgos {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
	public static boolean linSearch(int[] a, int val)
	{
		for(int i = 0; i < a.length; i++)
			if(a[i] == val)
				return true;
		return false;
	}
	
	public static boolean linSearchRe(int[] a, int val, int index)
	{
		if(a[index] == val)
			return true;
		else if(index >= a.length)
			return false;
		else
			return linSearchRe(a,val,index+1);
	}
	
	public static boolean binarySearchRe(int[] a, int val, int minIndex, int maxIndex)
	{
		int midIndex = (maxIndex + minIndex)/2;
		if(minIndex > maxIndex)
			return false;
		else if(a[midIndex]==val)
			return true;
		else
		{
			if(val > a[midIndex])
				return binarySearchRe(a,val,midIndex+1, maxIndex);
			else
				return binarySearchRe(a,val,midIndex,midIndex-1);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
