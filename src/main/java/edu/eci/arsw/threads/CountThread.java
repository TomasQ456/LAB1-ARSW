/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

public class CountThread extends Thread{
    int a;
    int b;
    public CountThread(int a, int b){
        if(a<b) {
            this.a = a;
            this.b = b;
        }
    }
    public void run(){
        System.out.println("thread is running...");
        for (int i = a; i <= b; i++) {
            System.out.println(i);
        }
    }
}

