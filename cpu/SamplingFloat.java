package cpu;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class SamplingFloat implements Runnable {
	float a = 1.9f,b = 0.0f;
	public void run(){
			for(int i=0;i<2250000;i++){//floating point operations to calculate FLOPS
				a = a + 7.5f;
				a = a - 1.5f;
				b = a * a;
				b = a / 1.5f;
				b = a % 10.0f;
				b = a + 1.3f;
				a = b + 1.5f;
				b = b + 1.8f;
				a += 1.6f;
				b += 1.8f;
				a++;
			}
	}
	public static void main(String[] args) throws IOException, InterruptedException{
		SamplingFloat nr = new SamplingFloat();
        for(int i=0;i<600;i++){ //run the code for 600 s and write the data to file every second
        	Thread t1 = new Thread(nr);
    		t1.setName("T1");
    		Thread t2 = new Thread(nr);
    		t2.setName("T2");
    		Thread t3 = new Thread(nr);
    		t3.setName("T3");
    		Thread t4 = new Thread(nr);
    		t4.setName("T4");
        	long start = System.nanoTime();
        	t1.start();
        	t2.start();
        	t3.start();
        	t4.start();
        	double elapsed = System.nanoTime() - start;

        	FileWriter fw = null;
        	Writer pw = null;
        	try {
        		double flops = Math.ceil((4*11*2250000)/elapsed);
        		fw = new FileWriter("output_datapoints_float.txt",true);//create and write to txt file
        		pw = new PrintWriter(fw);
        		pw.write(flops+"\n");
               		pw.flush();

        	} catch (IOException ex) {
        		ex.getMessage();
        	}finally{
        		pw.close();
        		fw.close();
        	}
        Thread.sleep(1000);//wait for one second to note down the values
        }
	}
}