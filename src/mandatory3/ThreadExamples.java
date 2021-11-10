package mandatory3;

import java.util.concurrent.Semaphore;

public class ThreadExamples {
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
