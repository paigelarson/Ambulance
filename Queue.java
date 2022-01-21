public interface Queue 
{ 
    
    
  boolean isEmpty(); 

  void enqueue(Object x); 

  Object dequeue(); 

  Object peekFront(); 
  
  int size();
} 
