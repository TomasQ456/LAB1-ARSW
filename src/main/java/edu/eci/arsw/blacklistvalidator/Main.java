/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

public class Main {
    
    public static void main(String a[]) throws InterruptedException {

        
        HostBlackListsValidator hblv = new HostBlackListsValidator();
        
        int numThreads = 100;
        
        System.out.println("Iniciando validación con " + numThreads + " hilos");
        
        long startTime = System.currentTimeMillis();
        
        List<Integer> blackListOcurrences = hblv.checkHost("202.24.34.55", numThreads);
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        System.out.println("The host was found en las listas: " + blackListOcurrences);
        System.out.println("Tiempo total de ejecución: " + totalTime + " milisegundos.");
        
    }
}