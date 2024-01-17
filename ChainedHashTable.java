/*
 * ChainedHashTable.java
 *
 * Computer Science 112, Boston University
 * 
 * Modifications and additions by:
 *     name:Yingru Zou
 *     email:Yinging@bu.edu
 */

import java.util.*;     // to allow for the use of Arrays.toString() in testing

/*
 * A class that implements a hash table using separate chaining.
 */
public class ChainedHashTable implements HashTable {
    /* 
     * Private inner class for a node in a linked list
     * for a given position of the hash table
     */
    private class Node {
        private Object key;
        private LLQueue<Object> values;
        private Node next;
        
        private Node(Object key, Object value) {
            this.key = key;
            values = new LLQueue<Object>();
            values.insert(value);
            next = null;
        }
    }
    
    private Node[] table;      // the hash table itself
    private int numKeys;       // the total number of keys in the table
        
    /* hash function */
    public int h1(Object key) {
        int h1 = key.hashCode() % table.length;
        if (h1 < 0) {
            h1 += table.length;
        }
        return h1;
    }
    
    /*** Add your constructor here ***/
    public ChainedHashTable(int size){
        if(size<=0){
            throw new IllegalArgumentException();
        }else{
            table = new Node[size];
            numKeys=0;
        }
    }
    
    /*
     * insert - insert the specified (key, value) pair in the hash table.
     * Returns true if the pair can be added and false if there is overflow.
     */
    public boolean insert(Object key, Object value) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if(table[h1(key)]==null){
            int i = h1(key);
            table[i] =new Node(key, value);
            System.out.println(table[i].values.toString());

        }
        else{
            Node trav = table[h1(key)];
            while(trav != null){
                if(trav.key.equals(key)){
                    trav.values.insert(value);
                    return true;
                }

                trav = trav.next;
            }
            Node x = new Node(key, value);
            Node y = table[h1(key)];
            x.next = y;
            table[h1(key)] = x;
        }
        numKeys++;
        return true;
    }
    
    /*
     * search - search for the specified key and return the
     * associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> search(Object key) {
        /** Replace the following line with your implementation. **/
        if(key==null){
            throw new IllegalArgumentException();
        }
        if(table[h1(key)]==null){
            return null;
        }
        Node trav = table[h1(key)];
        while(trav!=null){
            if(trav.key.equals(key)){
                break;
            }
            trav=trav.next;
        }
        return trav.values; 
    }
    
    /* 
     * remove - remove from the table the entry for the specified key
     * and return the associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> remove(Object key) {
        /** Replace the following line with your implementation. **/
        if(key==null){
            throw new IllegalArgumentException();
        }
        if(table[h1(key)]==null){
            return null;
        }
        Node trav=table[h1(key)];
        Node trail=null;
        while(trav!=null){
            if(!trav.key.equals(key)){
                trail=trav;
                trav=trav.next;
            }else{
                if(trail==null){
                    table[h1(key)]= trav.next;
                }else{
                    trail.next=trav.next;
                }
                numKeys--;
                break;
            }
        }
        return trav.values;
    }
    
    
    /*** Add the other required methods here ***/
    public int getNumKeys(){
        return numKeys;
    }

    public double load(){
        double d = (double)numKeys/table.length;
        return d;
    }

    public Object[] getAllKeys(){
        Object[] o = new Object[numKeys];
        int num = 0;
        for(int i = 0;i<table.length;i++){
            Node trav = table[i];
            while(trav != null){
                o[num]=trav.key;
                num++;
                if(trav.next!=null&&!trav.key.equals(trav.next.key)){
                    trav = trav.next;
                }else{
                    break;
                }
            }         
        }
        return o;

    }

    public void resize(int i){
        ChainedHashTable x = new ChainedHashTable(i);
        Object[] keyArray = getAllKeys();
        if(i<table.length){
            throw new IllegalArgumentException();
        }if(i==table.length){
            return;
        }else{
            for(int j = 0;j<numKeys;j++){
                int value1=h1(keyArray[j]);
                x.insert(keyArray[j],value1);
            }
            table=x.table;
        }
    }
    /*
     * toString - returns a string representation of this ChainedHashTable
     * object. *** You should NOT change this method. ***
     */
    public String toString() {
        String s = "[";
        
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                s += "null";
            } else {
                String keys = "{";
                Node trav = table[i];
                while (trav != null) {
                    keys += trav.key;
                    if (trav.next != null) {
                        keys += "; ";
                    }
                    trav = trav.next;
                }
                keys += "}";
                s += keys;
            }
        
            if (i < table.length - 1) {
                s += ", ";
            }
        }       
        
        s += "]";
        return s;
    }

    public static void main(String[] args) {
        ChainedHashTable table = new ChainedHashTable(5);
        table.insert("howdy", 15);
        Object key = "howdy";
        System.out.println(table.search(key));
        table.insert("goodbye", 10);
        table.insert("apple", 5);
        table.insert("howdy", 25); 
        table.insert("howdy", 15);
        table.insert("goodbye", 10);
        table.insert("apple", 5);
        table.insert("how", 25); 
        System.out.println(table.search("howdy"));
        //3.1
        System.out.println(table.insert("goodbye", 10));
        System.out.println(table); 
        //3.2
        System.out.println(table.insert("howdy", 15));
        System.out.println(table); 
        //4.1
        System.out.println(table.getNumKeys());
        //4.2
        table.insert("howdy", 15);
        table.insert("goodbye", 10);
        table.insert("apple", 5);
        table.insert("howdy", 25); 
        System.out.println(table.getNumKeys());
        //5.1
        ChainedHashTable table1 = new ChainedHashTable(5);
        table1.insert("howdy", 15);
        table1.insert("goodbye", 10);
        System.out.println(table1.load());
        //5.2
        ChainedHashTable table2 = new ChainedHashTable(5);
        table2.insert("howdy", 15);
        System.out.println(table2.load());
        //6.1
        Object[] keys = table1.getAllKeys();
        System.out.println(Arrays.toString(keys));
        //6.2
        Object[] keys2 = table2.getAllKeys();
        System.out.println(Arrays.toString(keys2));
        //7.1
        ChainedHashTable table3 = new ChainedHashTable(5);
        table3.insert("howdy", 15);
        table3.insert("goodbye", 10);
        table3.insert("apple", 5);
        System.out.println(table3);
        table3.resize(5);
        System.out.println(table3);
        //7.2
        table3.resize(10);
        System.out.println(table3);
    }
}
