package com.mycompany.cache_memory_simulator;

public class Line {
    public int inputOrder;
    public boolean dirtyBit;
    public int tag;
    public int frequency;
    public int lastUsed;
    
    public Line(){
        dirtyBit = false;
        frequency = 0;
        lastUsed = 0;
        tag = -1;           //Invalid value means still empty
    }
    
    public void fillLine(int tag, int inputOrder){
        this.tag = tag;
        dirtyBit = false;
        this.inputOrder = inputOrder;
    }
}
