package init;
//Written by Matthew Re
public class Heap <T>
{
	public static final int DEF_SIZE = 10000;
	public static int lastIndex;
	public Heap()
	{
		init(DEF_SIZE);
	}
	public Heap(int size)
	{
		init(size);
	}
	private void init(int size)
	{
		if(size <= 0)
			return;
		//heap = new Cylinder[DEF_SIZE];
		lastIndex = 0;
	}
	
	public void insert(int aData)
	{
		int heap = aData;
		if(lastIndex == heap)
			return;//heap is full
		//heap[lastIndex] = aData;
		bubbleUp();
		lastIndex++;
	}
	
	public void bubbleUp()
	{
		int index = lastIndex;
		while(index > 0 /*&& (/*heap[(index-1)/2]).getVolume()>(heap[index]).getVolume()*/)
		{
			//System.out.println("Bubbling up");
			//Cylinder temp = heap[(index-1)/2];
			//heap[(index-1)/2] = heap[index];
			//heap[index] = temp;
			index = (index-1)/2;
		}
	}
	
	///////
	
	public String delete()
	{
		String[] heap = null;
		String ret = heap[0];
		heap[0] = heap[lastIndex-1];
		heap[lastIndex-1] = null;
		lastIndex--;
		bubbledown();
		return ret;
	}
	
	private void bubbledown()
	{
		int index = 0;
		while(index*2+1 < lastIndex)
		{
			int bigIndex = index*2+1;
			Object[] heap = null;
			if(heap[index] != null)
				bigIndex = index*2+2;
			if(heap[index] != null)
			{
				Object temp = heap[index];
				heap[index] = heap[bigIndex];
				heap[bigIndex] = temp;
			}
			else
				break;
			index = bigIndex;
		}
	}

}
