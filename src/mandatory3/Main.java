package mandatory3;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

class SharedRes {
    static int maxBufferSize = 500;
    static int count = 0;
    static double[] values = new double[2];
    static double[][] valueArray = new double[500][2];
    static int readHead = 0;
    static int writeHead = 0;
    static Semaphore fullSpaces = new Semaphore(0); //producer increments after every production, consumer decrement before after consumption
    static Semaphore emptySpaces = new Semaphore(500); //producer decrement before production, consumer increment after production.
}

//thread that uses the feigenbaum
//tries to access resource at the same time
class FeigenBaumThread extends Thread{
    Semaphore semaphore;
    int limit;
    feigenbaumClass obj = new feigenbaumClass();
    double myArray[] = new double[2];


    public FeigenBaumThread(Semaphore semaphore){
        this.semaphore = semaphore;
        this.limit = limit;
    }
    @Override
    public void run(){
        while(true) {//keep regenerating the tree
            runFeigenWithSem();
            obj.setLambda(1);
        }
    }

    void runFeigenWithSem(){//calculates 500 feigenbaum values
        try {
            //increment shared resourc
            while(obj.increaseLambda()) {
                for (int i = 0; i < 500; i++) {
                    myArray = obj.feigenbaum();//calc values

                    SharedRes.emptySpaces.acquire(); // remove empty space, will wait if there is none
                    semaphore.acquire(); //lock

                    SharedRes.valueArray[SharedRes.writeHead]  = myArray; //write content and move head
                   // System.out.println("[writeHead: " +SharedRes.writeHead + "]");
                    SharedRes.writeHead = (SharedRes.writeHead + 1) % 500;

                    semaphore.release(); //open

                    SharedRes.fullSpaces.release();//increment full spaces

                    //Thread.sleep(100);
                }
            }
        }catch ( InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }
    }
}

class GUIThread extends Thread{
   Semaphore semaphore;
    //other parameters
    double myArray[] = new double[2];
    Painter painter = new Painter();

    public GUIThread(Semaphore semaphore){
        this.semaphore = semaphore;
    }
    @Override
    public void run(){
        //put code
        try {
            while(true) {
                //System.out.println("in Gui before fullLock");
                //System.out.println(SharedRes.fullSpaces);
                SharedRes.fullSpaces.acquire(); // remove content, will wait if there is none
                //System.out.println("in Gui after fullLock");

                semaphore.acquire(); //lock
                myArray = SharedRes.valueArray[SharedRes.readHead]; //read content and move head

                ///// use plotter here with data from myArray
                //System.out.println(myArray[0] + " " + myArray[1]); //display
                //System.out.println("[readHead: " + SharedRes.readHead+"]");
                painter.drawPoint((myArray[0]/100), (myArray[1]/100));
                /////

                SharedRes.readHead = (SharedRes.readHead + 1) % 500;
                semaphore.release(); //open

                SharedRes.emptySpaces.release(); // increment empty spaces
            }

        }catch (InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }

    }

}

public class Main {

    public static void main(String[] args) {


        Semaphore semaphore = new Semaphore(1);

        //creating and running the feigenbaum thread


        Thread gui = new GUIThread(semaphore);
        Thread feigen = new FeigenBaumThread(semaphore);
        gui.start();
        feigen.start();



        //testing making a window
        try {
            feigen.join();
        } catch (InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }


        try {
            gui.join();
        } catch (InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }


    }
}

