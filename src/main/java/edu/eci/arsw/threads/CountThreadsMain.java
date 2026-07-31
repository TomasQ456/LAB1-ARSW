/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {

    public static void main(String[] args){
        countInThree(0, 300000);
    }

    public static void countInThree(int a, int b){
        if (b < a) {
            throw new IllegalArgumentException("B debe ser mayor o igual que A");
        }

        int total = (b - a) / 3;

        CountThread ct1 = new CountThread(a, a + total);
        CountThread ct2 = new CountThread(a + total, a + total * 2);
        CountThread ct3 = new CountThread(a + total * 2, b);

        ct1.start();
        ct2.start();
        ct3.start();
    }
}