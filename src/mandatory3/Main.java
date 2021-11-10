package mandatory3;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

class SharedRes {
    static int count = 0;
    static Double[] values = new Double[2];
}
//tries to access resource after each other
class Thread1 extends Thread{
    Semaphore semaphore;
    String name;
    int limit;
    public Thread1(Semaphore semaphore, String name, int limit){
        this.semaphore = semaphore;
        this.name = name;
        this.limit = limit;
    }
    @Override
    public void run(){
        //aquire permit
        try {
            System.out.println(name + ":waiting for a permit.");
            semaphore.acquire();
            System.out.println(name + ":Acquired permit");

            //increment shared resource
            while(limit > 0){
                SharedRes.count++;
                System.out.println("counter: " + SharedRes.count);
                limit--;
            }

        }catch ( InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }


        // Release the permit.
        semaphore.release();
        System.out.println(name + ":Released the permit.");
    }
}

//tries to access resource at the same time
class Thread2 extends Thread{
    Semaphore semaphore;
    String name;
    int limit;
    public Thread2(Semaphore semaphore, String name, int limit){
        this.semaphore = semaphore;
        this.name = name;
        this.limit = limit;
    }
    @Override
    public void run(){
        //aquire permit
        try {

            //increment shared resource
            while(limit > 0){
                System.out.println(name + ":waiting for a permit.");
                semaphore.acquire();
                System.out.println(name + ":Acquired permit");


                SharedRes.count++;
                System.out.println("counter: " + SharedRes.count);
                limit--;

                // Release the permit.
                semaphore.release();
                System.out.println(name + ":Released the permit.");
            }

        }catch ( InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }
    }
}

//thread that uses the feigenbaum
//tries to access resource at the same time
class FeigenBaumThread extends Thread{
    Semaphore semaphore;
    int limit;
    public FeigenBaumThread(Semaphore semaphore){
        this.semaphore = semaphore;
    }
    @Override
    public void run(){
        //aquire permit
        try {

            //increment shared resource
            while(true){

                semaphore.acquire();

                // Release the permit.
                semaphore.release();
            }

        }catch ( InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        //testing threads
        Semaphore semaphore = new Semaphore(1);

	    Thread thread1 = new Thread2(semaphore,"thread1", 5 );
        Thread thread2 = new Thread2(semaphore,"thread2", 5 );

        thread1.run();
        thread2.run();


        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }

        System.out.println("Final Value:" + SharedRes.count);


	    //testing making a window
        JFrame frame = new JFrame("frame test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Label emptyLabel = new Label("sup");
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}

