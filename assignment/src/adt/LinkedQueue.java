package adt;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lim SiYu， Lee Seng Wai, Tang Le Yi, Ivan Wai Kim Hou
 * @param <T>
 */
public class LinkedQueue<T> implements QueueADT<T>{

    private class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    public int test;
    private Node front;
    private Node rear;
    private int count;
    
    public LinkedQueue(){
        front = null;
        rear = null;
        count = 0;
    }
    
    
    // Add Queue
    @Override
    public boolean enqueue(T newEntry) {
        
        Node newNode = new Node(newEntry);
        
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        
        count++;
        return true;
    }
    
    // Remove Queue (FIFO)
    @Override
    public T dequeue() {
        
        if (isEmpty()) {
            return null;
        }
        
        T removedData = front.data;
        
        front = front.next;
        
        if (front == null){
            rear = null;
        }
        
        count--;
        return removedData;
    }
    
    // Get the front (1st) data in queue
    @Override
    public T peek() {
        
        if (isEmpty()) {
            return null;
        } else {
            return front.data;
        }
        
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }
    
    @Override
    public int getCapacity(){
        return Integer.MAX_VALUE;
    }

    @Override
    public void clear() {
        front = rear = null;
        count = 0;
    }
    
    @Override
    public boolean contains(T item) {
        if (item == null) {
            return false;
        }
        
        Node current = front;
        
        while (current != null) {
            if (current.data.equals(item)) {
                return true;
            }
            
            current = current.next;
        }
        
        return false;
    }
    
    @Override
    public Object[] toArray() {
        Object[] array = new Object[count];
        Node current = front;
        int i = 0;
        
        while (current != null) {
            array[i++] = current.data;
            current = current.next;
        }
        
        return array;
    }
    
    


    
}

