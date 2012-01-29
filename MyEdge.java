public class MyEdge
{
  private static int nextId = 5;
  
  private int id;
  
  public MyEdge()
  {
    id = nextId++;
  }
  
  public String toString()
  {
    return String.format("%d", id);
  }
}