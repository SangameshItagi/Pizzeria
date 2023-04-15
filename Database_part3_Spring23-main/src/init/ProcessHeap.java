package init;
///Written by Matthew Re
public class ProcessHeap
{
	private Process[] Heap;
	private int lastIndex;
	private int Size;
	
	public ProcessHeap()
	{
		init(Size);
	}
	
	public ProcessHeap(int aSize)
	{
		
		init(aSize);
	}
	private void init(int aSize)
	{
		if(aSize<=0)
			return;
		Heap = new Process[aSize];
	}
	
	//////////
	public void insert(Process aData)
	{
		if(lastIndex >= Heap.length)
			return;//heap is full
		Heap[lastIndex] = aData;
		bubbleUp();
		lastIndex++;
		//System.out.println("*");
	}
	public void bubbleUp()
	{
		int index = lastIndex;
		while(index > 0 &&
			((Comparable<String>) Heap[(index-1)/2]).compareTo("")<0)
			{
				Process temp = Heap[(index-1)/2];
				Heap[(index-1)/2] = Heap[index];
				Heap[index] = temp;
				index = (index-1)/2;
			}
	}
	//////////
	
	public Process peek()
	{
		return Heap[0];
	}
	
	public void printHeap()
	{
		for(int i = 0; i < lastIndex; i++)
			System.out.println(Heap[i].toString());
		//System.out.println("***Print***");
	}
	
	////////////
	
	public Process remove()
	{
		Process ret = Heap[0];
		Heap[0] = Heap[lastIndex-1];
		Heap[lastIndex-1] = null;
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
			//if(index*2+2 < lastIndex &&
					//Heap[index*2+1].compareTo(Heap[index*2+2])<0)
				bigIndex = index*2+2;
			if(((Comparable<String>) Heap[index]).compareTo("")<0)
			{
				Process temp = Heap[index];
				Heap[index] = Heap[bigIndex];
				Heap[bigIndex] = temp;
			}
			else
				break;
			index = bigIndex;
		}
	}
	
	///////////
	
	public void heapSort()
	{
		for(int i = 0; i < lastIndex; i++)
			System.out.println(this.remove());
	}
	
	public boolean isEmpty()
	{
		if(Heap[0]==null)
			return true;
		else
			return false;
	}

	public void insert(init.ProcessSimulator.Process process) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
}
