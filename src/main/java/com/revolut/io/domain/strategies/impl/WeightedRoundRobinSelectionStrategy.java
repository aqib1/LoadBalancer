package com.revolut.io.strategies.impl;

import com.revolut.io.model.BackendInstance;
import com.revolut.io.strategies.SelectionStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WeightedRoundRobinSelectionStrategy implements SelectionStrategy {
    private final AtomicInteger index;
    private final AtomicInteger currentWeight;
    private final Lock lock;

    public WeightedRoundRobinSelectionStrategy() {
        this.index = new AtomicInteger(0);
        this.currentWeight = new AtomicInteger(0);
        this.lock = new ReentrantLock();
    }

    @Override
    public BackendInstance selectInstance(List<BackendInstance> instances) {
        if(instances.size() == 1) return instances.getFirst();
        this.lock.lock();
        try {
            int maxWeight = 0;
            Integer gcd = null;
            for(var instance: instances) {
                maxWeight = Math.max(maxWeight, instance.weight());
                gcd = (gcd == null) ? instance.weight() : gcd(gcd, instance.weight());
            }

           while(true) {
               int currentIndex = this.index.getAndUpdate(i -> (i + 1) % instances.size());
               if (currentIndex == 0) {
                   var finalGCD = gcd;
                   this.currentWeight.updateAndGet(i -> i - finalGCD);
                   if(this.currentWeight.get() <= 0) {
                       var finalMaxWeight = maxWeight;
                       this.currentWeight.getAndUpdate(i -> finalMaxWeight);
                   }
               }
               if (instances.get(currentIndex).weight() >= this.currentWeight.get()) {
                   return instances.get(currentIndex);
               }
           }
        } finally {
            this.lock.unlock();
        }
    }

    private Integer gcd(int a, int b) {
        return (b == 0) ? a : gcd(b, a % b);
    }
}
