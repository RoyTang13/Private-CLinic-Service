/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

/**
 *
 * @author Lim SiYu， Lee Seng Wai, Tang Le Yi, Ivan Wai Kim Hou
 * @param <T>
 */

public interface QueueADT<T>{
    
    //add the record in queue
    public boolean enqueue(T newEntry);
    
    //remove the record from first in the queue
    public T dequeue();
    
    //return the first value in the queue
    public T peek();
    
    //return true if empty
    public boolean isEmpty();
    
    //return the total number in the queue
    public int size();
    
    /**
     * Returns the maximum capacity of the queue.
     * @return 
     */
    public int getCapacity();
    
    //clear the queue record
    public void clear();
    
    /**
     * Returns true if the queue contains the specified item.Equality is checked using .equals().
     *
     * @param item  the item to search for
     * @return 
     */
    public boolean contains(T item);
    
    /**
     * Returns a copy of all queue elements as an array,
     * ordered from front to rear.
     *
     * @return Object array of all elements
     */
    public Object[] toArray();
    
    
}
