package cpu;

public class MultiThreadedTwoThreadsFloat implements Runnable {
	
	float a = 1.9f;
	public void run(){
		
		
		for(int i=0;i<4200000;i++){//floating point operations to calculate FLOPS
			a = a + 1.5f;
			a = a - 1.5f;
			a = a * a;
			a = a / 1.5f;
			a = a + 1.5f;
			a = a - 1.5f;
			a = a * a;
			a = a / 1.5f;
		}
}
public static void main(String[] args){
	MultiThreadedTwoThreadsFloat nr = new MultiThreadedTwoThreadsFloat();
	Thread t1 = new Thread(nr);
	t1.setName("T1");
	Thread t2 = new Thread(nr);
	t2.setName("T2");
	
	long start = System.nanoTime();
	t1.start();
	t2.start();
	
	long elapsed = System.nanoTime() - start;
	System.out.println("The time elapsed for float operations with two threads: "+(elapsed * 0.000000001)+ " seconds");
	double flops = ((2*8*4200000)/elapsed);
	System.out.println("The value of GFLOPS is "+flops);
	}
}
