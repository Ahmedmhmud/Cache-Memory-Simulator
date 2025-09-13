package com.mycompany.cache_memory_simulator;

import java.util.Scanner;

public class DirectMapping extends Mapping{
    public static int[] Lines;
    public int numOfLines;
    public static boolean[] dirtyBits;
    
    public int lineNumber;
    public int blockNumber;
    public boolean hitOrMiss;
    
    public int tag;
    public int index;
    public int blockOffset;
    
    Scanner input = new Scanner(System.in);
    
    public DirectMapping(){
        // members initialization
        numOfHits = 0;
        numOfAccesses = 0;
    
        // System info
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
        
        //Index = log2(number of lines)
        index = (int) (Math.log(numOfLines) / Math.log(2));
        
        // Write-hit strategy
        int userInput;
        System.out.println("Write-Hit strategy:\n\t1. Write through\n\t2. Write back");
        userInput = input.nextInt();
        write_hit = (userInput == 1);
        
        // Write-miss strategy
        System.out.println("Write-Miss strategy:\n\t1. Write allocate\n\t2. No Write allocate");
        userInput = input.nextInt();
        write_miss = (userInput == 1);
        
        //Initializing cache
        Lines = new int[numOfLines];
        dirtyBits = new boolean[numOfLines];
        for (int i = 0; i < numOfLines; i++) {
            Lines[i] = -1;          // Invalid tag for empty cache
            dirtyBits[i] = false;   // No modifications were made yet
        }
    }
    
    public void r_w_Instructions(int address, char instructionType){
        numOfAccesses++;
        blockNumber = address / blockSize;   // P.A bits = |  block number  | block offset |
        tag = blockNumber >> index;
        lineNumber = blockNumber % numOfLines;
        
        hitOrMiss = this.checkHit(lineNumber, tag);
        if(instructionType == 'R'){
            if(hitOrMiss){
                numOfHits++;
                System.out.println("Cache hit!");
            } else{
                System.out.println("Cache miss!");
                if(dirtyBits[lineNumber]){
                    System.out.println("Write back");
                    dirtyBits[lineNumber] = false;
                }
                Lines[lineNumber] = tag;
            }
        } else if(instructionType == 'W'){
            if(hitOrMiss){
                numOfHits++;
                System.out.println("Cache hit!");
                if(write_hit){
                    System.out.println("Write through");
                } else{
                    dirtyBits[lineNumber] = true;
                }
            } else{
                System.out.println("Cache miss!");
                if(write_miss){
                    Lines[lineNumber] = tag;
                    System.out.println("Write allocate");
                    if(write_hit){
                        System.out.println("Write through");
                    } else{
                        dirtyBits[lineNumber] = true;
                    }
                } else{
                    System.out.println("No write allocate");
                }
            }
        }
    }
    
    public boolean checkHit(int line ,int tag){
	return (Lines[line] == tag);
    }
}
