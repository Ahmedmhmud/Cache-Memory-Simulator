package com.mycompany.cache_memory_simulator;

import java.util.Scanner;

public class CacheSelection {
    public int address;
    Scanner input = new Scanner(System.in);

    public void DirectSelected(){
        DirectMapping cache = new DirectMapping();
        OUTER:
        while (true) {
            System.out.println("Select cache instruction:\n\t1. Read\n\t2. Write\n\t3.Exit");
            int userInput = input.nextInt();
            switch (userInput) {
                case 1 -> {
                    System.out.print("Enter memory address: ");
                    address = input.nextInt();
                    cache.r_w_Instructions(address, 'R');
                }
                case 2 -> {
                    System.out.print("Enter memory address: ");
                    address = input.nextInt();
                    cache.r_w_Instructions(address, 'W');
                }
                case 3 -> {
                    cache.printStatistics();
                    break OUTER;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }
    
    public void FullySelected(){
        FullyAssociativeMapping cache = new FullyAssociativeMapping();
        OUTER:
        while (true) {
            System.out.println("Select cache instruction:\n\t1. Read\n\t2. Write\n\t3.Exit");
            int userInput = input.nextInt();
            switch (userInput) {
                case 1 -> {
                    System.out.print("Enter memory address: ");
                    address = input.nextInt();
                    cache.r_instruction(address);
                }
                case 2 -> {
                    System.out.print("Enter memory address: ");
                    address = input.nextInt();
                    cache.w_instruction(address);
                }
                case 3 -> {
                    cache.printStatistics();
                    break OUTER;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }
    
    public void SetSelected(){
        SetAssociativeMapping cache = new SetAssociativeMapping();
        OUTER:
        while (true) {
            System.out.println("Select cache instruction:\n\t1. Read\n\t2. Write\n\t3.Exit");
            int userInput = input.nextInt();
            switch (userInput) {
                case 1 -> {
                    System.out.print("Enter memory address: ");
                    address = input.nextInt();
                    cache.r_instruction(address);
                }
                case 2 -> {
                    System.out.print("Enter memory address: ");
                    address = input.nextInt();
                    cache.w_instruction(address);
                }
                case 3 -> {
                    cache.printStatistics();
                    break OUTER;
                }
                default -> System.out.println("Invalid input!");
            }
        }
    }
}
