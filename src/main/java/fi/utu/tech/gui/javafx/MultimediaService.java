package fi.utu.tech.gui.javafx;

import java.lang.reflect.Constructor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class offers a memory loaded and fixed size multipurpose blocking queue as a multimedia service
 * The service instantiates a given number of objects in memory for fast data access, such as audio and graphics.
 * 
 * The type of object to be loaded in memory, can be assigned with a constructor parameter cls.
 * The given class will be instantiated with a parameter list args, also given in as a parameter in the constructor.
 * 
 * The producer thread (the run() method) adds objects to the queue and maintains the queue full,
 * an object in queue can then be consumed (the take() method).
 *  
 * The producer/consumer queue has the following features: 
 * - A fixed size queue (QUEUE_SIZE) 
 * - A lock to protect access to the queue 
 * - A condition variable to wait for when the queue is full (notFull) 
 * - A condition variable to wait for when the queue is empty (notEmpty)
 * 
 * If the queue is full, the producer thread will wait until there is space in the queue.
 * If the queue is empty, the consumer thread will wait until there is an object in the queue.
 * @author j-code
 *
 */

public class MultimediaService extends Thread {
	private final int QUEUE_SIZE;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private final Object[] items;
    private int putptr, takeptr, count;
    private Object[] args;
    private Constructor<?> ctor;
    
    /**
     * Initializes a new instance of MultimediaService class with
     * specifying the type of an object, its arguments and the queue size.
     * 
     * @param cls The class type for the objects to instantiate in the queue.
     * @param args The arguments for the class constructor.
     * @param queueSize The size of the queue.
     * @throws Exception This exception is thrown, if for given array of instance arguments is not found any matching constructor
     */
    
    public MultimediaService(Class<?> cls, Object[] args, int queueSize) throws Exception {
    	this.args = args;
    	
    	for (Constructor<?> ctor: cls.getConstructors()) {
    		Class<?>[] cClasses = ctor.getParameterTypes();
    		if (cClasses.length != args.length) continue;
    		int nSameTypes = 0;
    		for (int i = 0; i < cClasses.length; i++) {
    			if (cClasses[i].equals(args[i].getClass())) nSameTypes++;
    		}
    		if (nSameTypes == args.length) {
    			this.ctor = ctor;
    			break;
    		}
    	}
    	if (ctor == null) throw new Exception("No matching contructor found for types in given arguments");
    	QUEUE_SIZE = queueSize;
    	items = new Object[QUEUE_SIZE];
    	setDaemon(true);
    }
    
    public MultimediaService(Class<?> cls, Object[] args) throws Exception {
    	this(cls, args, 3);
    }
    
    
    /**
     * Puts an object into the queue.
     * 
     * @param x The object to put in queue.
     * @throws InterruptedException 
     */
    private void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == QUEUE_SIZE) notFull.await();
            items[putptr] = x;
            if (++putptr == QUEUE_SIZE) putptr = 0;
            ++count;
            //System.out.println("MultimediaService: adding to queue:" + x);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Takes an object from the queue.
     * 
     * @return The taken object from the queue.
     * @throws InterruptedException 
     */
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) notEmpty.await();
            Object x = items[takeptr];
            if (++takeptr == QUEUE_SIZE) takeptr = 0;
            --count;
            // System.out.println("MultimediaService: removing from queue:" + x);
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void run() {
        while (true) {
            try {
            	put(ctor.newInstance(args));
            } catch (InterruptedException ex) {
                // Restore interrupted state...
                Thread.currentThread().interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
}
