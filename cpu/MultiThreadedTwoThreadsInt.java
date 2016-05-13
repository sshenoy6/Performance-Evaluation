package cpu;

public class MultiThreadedTwoThreadsInt implements Runnable{
	int a = 2,e=1200,g =998,b=9,d=87;
	public void run(){
	for(int i=0;i<1255000;i++){//integer operations to calculate IOPS
			a++;
			a--;
			a = a * a;
			a = a / 2;
			e = (b++) + (d--) +(e + (b * b) + b/10) + (b * b + 2 * g) + (b++) + (d++) + b + d;
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
	
	System.out.println("The time elapsed for int operations with two threads: "+(elapsed * 0.000000001)+" seconds");
	double gIops = ((2*23*1255000)/elapsed);
	System.out.println("The value of GIOPS is "+gIops);
}
}
