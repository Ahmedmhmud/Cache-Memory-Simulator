package com.mycompany.cache_memory_simulator;

import java.util.Scanner;

public class FullyAssociativeMapping extends Mapping{
    public int replacementPolicy;   //Not strict as direct
    
    public Line[] Lines;            //Line features are embedded in Line class
    public int numOfLines;
    public boolean hitOrMiss;       //hit: true                 miss: false
    
    public int tag;
    public int blockOffset;
    public int blockNumber;
    
    public int lineNumber;
    public int lineOrder;           //For FIFO algorithm
    public static int fillingCache; //For first request list
    
    Scanner input = new Scanner(System.in);
    
    public FullyAssociativeMapping(){
        lineOrder = 0;
        fillingCache = 0;
        numOfHits = 0;
        numOfAccesses = 0;
        
        System.out.print("Main memory size: ");
        memorySize = input.nextInt();
        System.out.print("Cache memory size: ");
        cacheSize = input.nextInt();
        System.out.print("Block size: ");
        blockSize = input.nextInt();
        
        // #Lines = cache size / block size
        numOfLines = cacheSize / blockSize;
        
        //Block offset = log2(block size)
        blockOffset = (int) (Math.log(blockSize) / Math.log(2));
        
        // Write-hit strategy
        int userInput;
        System.out.println("Write-Hit strategy:\n\t1. Write through\n\t2. Write back");
        userInput = input.nextInt();
        write_hit = (userInput == 1);
        
        // Write-miss strategy
        System.out.println("Write-Miss strategy:\n\t1. Write allocate\n\t2. No Write allocate");
        userInput = input.nextInt();
        write_miss = (userInput == 1);
        
        //Replacement policy selection
        System.out.println("Select one of these replacement policies:\n\t1. FIFO\n\t2.LRU\n\t3. LFU");
        replacementPolicy = input.nextInt();
        
        Lines = new Line[numOfLines];
        for (int i = 0; i < numOfLines; i++) {
            Lines[i] = new Line();
        }
    }
    
    public void r_instruction(int address){
        numOfAccesses++;
        blockNumber = address / blockSize;
        lineNumber = this.checkHit(blockNumber);
        
        hitOrMiss = (lineNumber != -1);
        
        if(hitOrMiss){
            numOfHits++;
            Lines[lineNumber].frequency++;
            increaseLinesUsage();
            Lines[lineNumber].lastUsed = 0;
            System.out.println("Cache hit!");
            
        } else{
            System.out.println("Cache miss!");
            if(fillingCache < numOfLines){
                lineNumber = fillingCache;
                Lines[lineNumber].tag = blockNumber;
                Lines[lineNumber].frequency++;
                increaseLinesUsage();
                Lines[lineNumber].lastUsed = 0;
                Lines[lineNumber].inputOrder = lineOrder;
                fillingCache++;
            } else{
                switch(replacementPolicy){
                    case 1 -> FIFO();
                    case 2 -> LRU();
                    case 3 -> LFU();
                }
                Lines[lineNumber].fillLine(blockNumber, lineOrder);
                if(Lines[lineNumber].dirtyBit){
                    System.out.println("Write back");
                    Lines[lineNumber].dirtyBit = false;
                }
            }
            lineOrder++;
        }   
    }
    
    public void w_instruction(int address){
        numOfAccesses++;
        blockNumber = address / blockSize;
        lineNumber = this.checkHit(blockNumber);
        
        hitOrMiss = (lineNumber != -1);
        
        if(hitOrMiss){
            numOfHits++;
            System.out.println("Cache hit!");
            if(write_hit){
                System.out.println("Write through");
            } else{
                Lines[lineNumber].dirtyBit = true;
            }
            increaseLinesUsage();
            Lines[lineNumber].lastUsed = 0;
            Lines[lineNumber].frequency++;
            
        } else{
            System.out.println("Cache miss!");
            if(write_miss){
                System.out.println("Write allocate");
                if(fillingCache < numOfLines){
                    lineNumber = fillingCache;
                    fillingCache++;
                    Lines[lineNumber].tag = blockNumber;
                    Lines[lineNumber].frequency++;
                    increaseLinesUsage();
                    Lines[lineNumber].lastUsed = 0;
                    Lines[lineNumber].inputOrder = lineOrder;
                } else{
                    switch(replacementPolicy){
                        case 1 -> FIFO();
                        case 2 -> LRU();
                        case 3 -> LFU();
                    }
                    Lines[lineNumber].fillLine(blockNumber, lineOrder);
                }
                if(write_hit){
                    System.out.println("Write through");
                } else{
                    Lines[lineNumber].dirtyBit = true;
                }
            } else{
                System.out.println("No write allocate");
            }
            increaseLinesUsage();
            Lines[lineNumber].lastUsed = 0;
            Lines[lineNumber].frequency++;
        }
        
    }
    
    public void increaseLinesUsage(){
	for (int i = 0 ; i < numOfLines ; i++ ){
            if ( i == lineNumber )
		continue ;
            Lines[i].lastUsed += 1 ;
	}
    }
    
    public void FIFO(){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < numOfLines; i++) {
            if(Lines[i].inputOrder < min){
                min = Lines[i].inputOrder;
                lineNumber = i;
            }
        }
    }
    
    public void LRU(){
        int max = 0;
        for (int i = 0; i < numOfLines; i++) {
            if(Lines[i].lastUsed > max){
                max = Lines[i].lastUsed;
                lineNumber = i;
            }
        }
    }
    
    public void LFU(){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < numOfLines; i++) {
            if(Lines[i].frequency < min){
                min = Lines[i].frequency;
                lineNumber = i;
            }
        }
    }
    
    public int checkHit(int blocknum){
        for(int i = 0; i < numOfLines; i++){
            if(Lines[i].tag == blocknum)
                return i;
        }
        return -1;
    }
}
