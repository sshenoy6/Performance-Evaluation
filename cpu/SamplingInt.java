package cpu;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class SamplingInt implements Runnable  {
	int a = 1,b = 0;
	public void run() {
		for (int i = 0; i < 6998000; i++) {//running integer operations to calculate IOPS
			a++;
			a--;
			b = a * a;
			b = a / 2;
			a = a * a +  b * b + 2 * a * b;
			a++;
			a--;
			b = a * a;
			b = a / 2;
			a = a * a +  b * b + 2 * a * b;
			a++;
			a = a + i;
		}
	}

public static void main(String[] args) throws IOException, InterruptedException {
		
		SamplingInt nr = new SamplingInt();
		for(int i=0;i<600;i++){ //execute the code for 10 minutes by sleeping every second and noting down the values
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
		long elapsed = System.nanoTime() - start;
		FileWriter fw = null;
		 Writer pw = null;
		    try {
		        double iops = Math.ceil((23 * 6998000)/elapsed);
		        fw = new FileWriter("output_datapoints_int.txt",true); //create txt file to write graph values
		        pw = new PrintWriter(fw);
		        pw.write(iops+"\n");
		        pw.flush();
		    } catch (IOException ex) {
		        ex.getMessage();
		    }finally{
		    	pw.close();
		        fw.close();
		    }
		    Thread.sleep(1000);//sleep for 1 second
		}
	}
}