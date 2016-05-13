package cpu;

/*class to find GFLOPS for 4 threads*/
public class MultiThreadedFourThreadsFloat implements Runnable {
	float a = 1.9f,b = 0.2f,c = 5.6f,d =7.6f;
	public void run(){
			for(int i=0;i<2250000;i++){ //do floating point arithmetic operations repeatedly
				a = (a + 7.5f) - 1.5f + ((a * b)/1.5f) + (a / 10.5f);
				b = a + 1.3f - (b + 1.5f) + (d + 1.88f);
				a += 1.6f;
				b += 1.8f;
				d = d + i;
				b = (a+i) + 5.6f + (c+i) + (d +0.9f) + (a-c) + (a + d);
			}
	}
	public static void main(String[] args){
		MultiThreadedFourThreadsFloat nr = new MultiThreadedFourThreadsFloat();//create instance of runnable object
		Thread t1 = new Thread(nr); //create the threads
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
		System.out.println("The time elapsed for float operations with four threads: "+(elapsed*0.000000001)+" seconds");
		double flops = ((4*25*2250000)/elapsed);
    		System.out.println("The value of GFLOPS is "+flops);
	}
}
