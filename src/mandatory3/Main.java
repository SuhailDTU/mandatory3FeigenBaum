package mandatory3;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

class SharedRes {
    static int maxBufferSize = 500;
    static int count = 0;
    static double[] values = new double[2];
    static double[][] valueArray = new double[maxBufferSize][2];
    static int readHead = 0;
    static int writeHead = 0;
    static Semaphore fullSpaces = new Semaphore(0); //producer increments after every production, consumer decrement before after consumption
    static Semaphore emptySpaces = new Semaphore(maxBufferSize); //producer decrement before production, consumer increment after production.
    static boolean reset = false;
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

            //send resset signal
            try {
                semaphore.acquire(); //lock
                SharedRes.reset = true;
                semaphore.release(); //open
            }catch (InterruptedException interruptedException){
                interruptedException.printStackTrace();
            }
        }
    }

    void runFeigenWithSem(){//calculates 500 feigenbaum values
        try {
            //increment shared resourc
            while(obj.increaseLambda()) {
                for (int i = 0; i < SharedRes.maxBufferSize; i++) {
                    myArray = obj.feigenbaum();//calc values

                    SharedRes.emptySpaces.acquire(); // remove empty space, will wait if there is none
                    semaphore.acquire(); //lock

                    SharedRes.valueArray[SharedRes.writeHead]  = myArray; //write content and move head
                   // System.out.println("[writeHead: " +SharedRes.writeHead + "]");
                    SharedRes.writeHead = (SharedRes.writeHead + 1) % SharedRes.maxBufferSize;

                    semaphore.release(); //open

                    SharedRes.fullSpaces.release();//increment full spaces

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

                //if the screen needs to reset
                if(SharedRes.reset == true){
                    Thread.sleep(500); //wait to show work
                    painter.clear();
                    SharedRes.reset = false;
                }


                myArray = SharedRes.valueArray[SharedRes.readHead]; //read content and move head

                ///// use plotter here with data from myArray
                //System.out.println(myArray[0] + " " + myArray[1]); //display
                //System.out.println("[readHead: " + SharedRes.readHead+"]");
                    painter.drawPoint(((myArray[1]-1) / 3), (1 - (myArray[0])));
                /////

                SharedRes.readHead = (SharedRes.readHead + 1) % SharedRes.maxBufferSize;
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


        /*
        Painter painter = new Painter();

        int i = 1;
        while(true) {
            i++;
            painter.drawPoint(0.0001*i, 0.0001*i);
            try {
                System.out.println(i*0.0001);
                Thread.sleep(1);
            } catch (Exception exception){
                exception.printStackTrace();
            }
        }
        */




    }
}

