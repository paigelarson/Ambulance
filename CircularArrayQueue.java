

import java.util.Arrays;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;

public class CircularArrayQueue implements Queue 
{ 
  private int head = 0; // index of the next element to be removed
  private int tail = 0; // index of the next element be added
  private int theSize; // number of elements stored in the array (may be less than elements.length)
  private Object[] elements; // array that stores the data
  
  public CircularArrayQueue(int capacity)
  { 
      elements=new Object[capacity];
  }
 
     public boolean isEmpty()
  { 	
       return(size()==0);
  } 
  public void enqueue(Object x)
  {  if (size() == elements.length)
        throw new IllegalStateException("There is no room to enqueue another element.");
    else
    {
       elements[tail]=x;
       tail++;
       if(tail >= elements.length)
       {
           tail=0;
       }
        
        
    update();
    }
  } 

  public Object dequeue()
  { 
      Object t;
      if (elements[head] == null)
        throw new IllegalStateException("There is nothing to dequeue.");
    else
    {
        t=elements[head];
        elements[head]=null;
        head++;
       if(head>=elements.length)
       {
           head=0;
       }
       
       update();
    }
      return t;
  }

  public Object peekFront()
  { Object a;
      if (elements[head] == null)
        throw new NoSuchElementException("There is no element to peek at.");
    else
    {
        a=elements[head];
        update();
    } 
    return a;
  }
  
  public int size()
  {   
      int counter=0;
      for(int c=0; c < elements.length; c++)
      {
          if(elements[c]!=null)
          {
              counter++;
          }
          
      }
      return counter;
  }
  
  public void update()
  { System.out.print("head: " + head + "  tail: " + tail + "   ");
    String arr = Arrays.toString(elements);
    System.out.println(arr);
  }
  
  public static void main(String[] args)
  { 
    CircularArrayQueue asd= new CircularArrayQueue(4);
    asd.enqueue("2");
    asd.enqueue("4");
    asd.enqueue("6");
    asd.dequeue();
    //asd.dequeue();
  System.out.println("dequeue:"+ asd.dequeue());
   System.out.println(asd.isEmpty());
  }
  
} 
