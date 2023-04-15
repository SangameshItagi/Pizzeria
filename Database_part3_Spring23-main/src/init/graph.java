//Written by Matthew Re
package init;

import java.util.*;

public class graph {
	
	private class Vertex
	{
		char letter;
		String name;
		LinkedList<Edge> neighbors;
		public Vertex(String aName, char aLetter)
		{
			name = aName;
			letter = aLetter;
			neighbors = new LinkedList<Edge>();
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
	private LinkedList<Vertex> vertices;//the graph
	
	public graph()
	{
		origin = null;
		vertices = new LinkedList<Vertex>();
	}
	
	public void addVertex(String aName, char aLetter)
	{
		if(isVertContained(aName))
			return;//can't add duplicates
		Vertex v = new Vertex(aName, aLetter);
		vertices.add(v);
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
		{
			System.out.println("Didnt find a vertex with that name");
			return;//didnt find a vertex with that name
			
		}
		v1.neighbors.add(new Edge(v2,weight));
	}
	public Vertex getVert(String aName)
	{
		for(Vertex v : vertices)
			if(v.name.equals(aName))
			{
				//System.out.println(v.name);
				return v;
			}
		return null;
	}
	
	private LinkedList<Vertex> markedVerts = new LinkedList<Vertex>();
	private LinkedList<Vertex> compare = new LinkedList<Vertex>();


/////////////////////////////////////////////////////////////////////////////////
	public void printDFS(Vertex startV)
	{
		markedVerts.clear();
		printDFSr(startV, startV);
	}
	private void printDFSr(Vertex startV, Vertex v)
	{
		compare.add(new Vertex("H", 'c'));
		compare.add(new Vertex("R", 'a'));
		compare.add(new Vertex("Y", 't'));
		//while(markedVerts.size() <= 3)
			if(markedVerts.contains(v))
				{
					if(v.equals(startV))
						{
							for(Vertex u : markedVerts)
								{
									System.out.print(u.letter + " ");
								}
							//System.out.print(startV.name);
							System.out.println();
						}
					return;
				}
			markedVerts.add(v);
			for(Edge e : v.neighbors)
			{
				if(markedVerts.equals(compare))
					System.out.println("*********"+markedVerts.toString());
				printDFSr(startV, e.toVert);
			}
			markedVerts.removeLast();
			
		
		//if(markedVerts.size() > 3)
			//markedVerts.clear();
	}
	
////////////////////////////////////////////////////////////////////////////////////////

	public void printLL()
	{
		for(Vertex v : vertices)
		{
			System.out.print(v.name);
		}
		System.out.println();
	}
	
	
	
	
}



	


/*
 * char[][] mat
 * make each cell named by their vertex (0,0)...(3,2)...etc
 */



















