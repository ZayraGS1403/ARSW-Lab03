/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;


public class Consumer extends Thread{
    
    private Queue<Integer> queue;
    
    
    public Consumer(Queue<Integer> queue){
        this.queue=queue;        
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized(queue){
                while (queue.isEmpty()){
                    try {
                        queue.wait(); // espera si la cola este vacia
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                int elem = queue.poll();
                System.out.println("Consumer consumes " + elem);
                queue.notifyAll(); // ntifica al productor que hay espacio disponible

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

