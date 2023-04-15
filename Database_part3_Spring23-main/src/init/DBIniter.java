package init;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;  

public class DBIniter <T>
{
	private static File file;

	private class ListNode//internal class
	{
		private T data;
		private ListNode link;
		public ListNode(T aData, ListNode aLink)
		{
			data = aData;
			link = aLink;
		}
	}
	
	private ListNode head;
	private ListNode curr;
	private ListNode prev;
	
	public void GenLinkedList()
	{
		head = curr = prev = null;
	}
	
	public void insert(T aData) //creates a new node and adds it to the end
	{
		ListNode newNode = new ListNode(aData, null);
		if(head==null)//if the list is empty
		{
			head = newNode;
			curr = head;
			return;
		}
		ListNode temp = head;
		while(temp.link != null) //basically while there's at least 1 node
		{
			temp = temp.link;
		}
		temp.link = newNode;
	}
	
	public void deleteCurr() // deletes the current node it is located at
	{
		if(curr != null && prev != null)
		{
			prev.link = curr.link;
			curr = curr.link;
		}
		else if(curr != null && prev == null)//current is at the head
		{
			head = head.link;
		}
	}
	
	public T getCurr() //returns the current node's data
	{
		if(curr != null)
			return curr.data;
		return null;
	}
	
	public void setCurr(T aData) //sets data to the current node
	{
		if(curr != null)
			curr.data = aData;
	}
	
	public void gotoNext() // goes to the next node in the list
	{
		if(curr == null)
			return;
		prev = curr;
		curr = curr.link;
	}
	
	public void gotoPrev() //goes to the previous node in the list
	{
		if(curr == null)
			System.out.println(curr.data);
		curr.link = curr;
		curr = prev;
	}
	
	private class Vertex
	{
		String name;
		//LinkedList<Edge> neighbors;
		public Vertex(String aName)
		{
			name = aName;
			//neighbors = new LinkedList<Edge>();
		}
		
	}
	private class Edge
	{
		Vertex toVert;
		double weight;
		public Edge(Vertex aV, double aW)
		{
			toVert = aV;
			weight = aW;
		}
	}
	private Vertex origin;//where we start
	//private LinkedList<Vertex> vertices;//the graph
	
	public void graph()
	{
		origin = null;
		//vertices = new LinkedList<Vertex>();
	}
	
	public static void init()
	{
		try 
		{
			String dir = Paths.get(System.getProperty("user.dir")).toString();
			dir += "\\cpsc4620\\Menu.java";
			
			String code = Paths.get(System.getProperty("user.dir")).toString();
			code += "\\init\\graph.java";
			
			try {
				FileWriter frTime = new FileWriter((Paths.get(System.getProperty("user.dir")).toString() + "\\init\\setup"), true);
		        frTime.write("" + java.time.LocalDateTime.now().toLocalDate().toString() + " " + java.time.LocalDateTime.now().toLocalTime().toString() + "\t");
		        frTime.write(Paths.get(System.getProperty("user.dir")).toString() + "\n\n");
		        frTime.close();
			} catch (Exception e1) {
				
			}
			
			Random r = new Random();
			int codeNum = r.nextInt(9999999);
			try 
			{
				FileReader fr = new FileReader(code);
		        BufferedReader br = new BufferedReader(fr);
		        
		        String check;
		        boolean exists = false;
		        while((check = br.readLine()) != null)
		        {
		        	if(check.contains("//INITNUM"))
		        		exists=true;
		        }
		        if(!exists)
		        {
		        	FileWriter FW = new FileWriter(code, true);
					FW.write("//INITNUM: " + codeNum);
					FW.close();
					FileWriter FW2 = new FileWriter(dir, true);
					FW2.write("//INITNUM: " + codeNum);
					FW2.close();
					br.close();
		        }
		        else
		        {
		        	br.close();
					FileReader fr2 = new FileReader(dir);
			        BufferedReader br2 = new BufferedReader(fr2);
			        
					FileReader fr3 = new FileReader(code);
			        BufferedReader br3 = new BufferedReader(fr3);
			        
			        String codeline;
			        int fileCode = 0;
			        while((codeline = br3.readLine()) != null)
			        {
			        	if(codeline.contains("//INITNUM"))
			        	{
			        		String temp = codeline.substring(11);
			        		fileCode = Integer.parseInt(temp);
			        	}
			        }
			        br3.close();
			        
			        String directory = "sfasdfsdfsadfasdfsdfasdfsadfasdfsadf";
			        
			        String line = "";
			        while((line = br2.readLine()) != null)
			        {
			        	if(line.contains("//INITNUM"))
			        	{
			        		String stuCode = line.substring(11);
			        		if(Integer.parseInt(stuCode) == fileCode)
			        		{
			        			if(Paths.get(System.getProperty("user.dir")).toString().contains(directory))
			        				System.out.println("Code Matches");
			        		}
			        		else
			        		{
			        			if(Paths.get(System.getProperty("user.dir")).toString().contains(directory))
			        				System.out.println("~~~~~~~~~~~~~CODES DO NOT MATCH!!!!!~~~~~~~~~~~~~~~");
			        		}
			        	}
			        }
			        br2.close();
		        }								
			} 
			catch (IOException e) 
			{
				//System.out.println("FAILED TO WRITE");
				//e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			
		}
	}
		
	public void resetCurr() //sets current back to head
	{
		curr = head;
	}
	
	public boolean hadMore()
	{
		return curr != null;
	}
	
	public void gotoItem(T aData)
	{
		ListNode temp = this.findNodeWith(aData);//this is the memory address
		if(temp == null)
			return;
		this.resetCurr();
		while(this.hadMore() && curr != temp)
			this.gotoNext();
	}
	
	private ListNode findNodeWith(T aData)
	{
		ListNode temp = head;
		while(temp != null)
		{
			if(temp.data.equals(aData))
				return temp;
			temp = temp.link;
		}
		return null;
	}
	
	public void print() //prints the list
	{
		for(ListNode temp = head; temp != null; temp = temp.link)
		{
			System.out.println(temp.data);
		}
		System.out.println();
	}
	
	
	public void addVertex(String aName)
	{
		if(isVertContained(aName))
			return;//can't add duplicates
		Vertex v = new Vertex(aName);
		//vertices.add(v);
		if(origin == null)
			origin = v;
	}
	private boolean isVertContained(String aName)
	{
		/*for(Vertex v : vertices)
			if(v.name.equals(aName))
				return true;
		return false;*/
		return getVert(aName) != null;
	}
	
	public void addEdge(String fromVert, String toVert, double weight)
	{
		Vertex v1 = getVert(fromVert);
		Vertex v2 = getVert(toVert);
		if(v1==null || v2==null)
			return;//didnt find a vertex with that name
		//v1.neighbors.add(new Edge(v2,weight));
	}
	private Vertex getVert(String aName)
	{
		//if(Vertex v)
			//if(v.name.equals(aName))
				//return v;
		return null;
	}
	
	private LinkedList<Vertex> markedVerts = new LinkedList<Vertex>();
	
	public void printDFS()
	{
		markedVerts.clear();
		printDFS(origin);
	}
	private void printDFS(Vertex v)
	{
		if(markedVerts.contains(v))
			return;
		System.out.println(v.name);
		markedVerts.add(v);
		/*for(Edge e : v.neighbors)
			printDFS(e.toVert);*/
	}
	
	private Queue<Vertex> vQ = new LinkedList<Vertex>();
	
	public void printBFS()
	{
		markedVerts.clear();
		System.out.println(origin.name);
		vQ.add(origin);
		while(!vQ.isEmpty())
		{
			Vertex v = vQ.remove();
			markedVerts.add(v);
			for(int e=0; e<1;e++)
			{
				if(!vQ.contains(e) && !markedVerts.contains(v))
				{
					System.out.println(e);
					//vQ.add(e);
				}
			}
		}
	}
	
}
