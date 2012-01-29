	 public class MyVertex
	  {
	    private static int nextId = 5;
	    
	    private int id;
	    
	    public MyVertex()
	    {
	      id = nextId++;
	    }
	    
	    public String toString()
	    {
	      return String.format("%d", id);
	    }
	  }