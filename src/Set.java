package com.mycompany.cache_memory_simulator;

public class Set {
    public Line[] Lines;
    public int fillingSet;
    public int lineOrder;
    public static int k_way;
    public int lineNumber;
        
    public Set(){
        Lines = new Line[k_way];
        for (int i = 0; i < k_way; i++) {
            Lines[i] = new Line();
        }
        fillingSet = 0;
        lineOrder = 0;
    }
    
    public int checkHit(int tag){
        for (int i = 0; i < k_way; i++) {
            if(Lines[i].tag == tag){
                return i;
            }
        }
        return -1;
    }
    
    public void increaseLinesUsage(){
	for (int i = 0 ; i < k_way ; i++ ){
            if ( i == lineNumber )
		continue ;
            Lines[i].lastUsed += 1 ;
	}
    }
    
    public void FIFO(){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < k_way; i++) {
            if(Lines[i].inputOrder < min){
                min = Lines[i].inputOrder;
                lineNumber = i;
            }
        }
    }
    
    public void LRU(){
        int max = 0;
        for (int i = 0; i < k_way; i++) {
            if(Lines[i].lastUsed > max){
                max = Lines[i].lastUsed;
                lineNumber = i;
            }
        }
    }
    
    public void LFU(){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < k_way; i++) {
            if(Lines[i].frequency < min){
                min = Lines[i].frequency;
                lineNumber = i;
            }
        }
    }
}