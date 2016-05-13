#include<stdio.h>
#include<time.h>
#include<stdlib.h>
#include<string.h>
#include<pthread.h>
#define MEMORY_CHUNK_SIZE 15728640
#define BUFFER_BYTE 1
#define BUFFER_KBYTE 1024
#define BUFFER_MBYTE 1048576

//calculate the average latency,throughput
static int i=0, j=0;
double latency_random_byte[2],latency_random_kb[2],latency_random_mb[2],latency_sequential_byte[2],latency_sequential_kb[2],latency_sequential_mb[2],throughput_random_byte[2],throughput_random_kb[2],throughput_random_mb[2],throughput_sequential_byte[2],throughput_sequential_kb[2],throughput_sequential_mb[2];

void *randomReadWrite(){
	
	char *array1,*array2;
	struct timeval start,end;
	int offset1 = rand() % 10;
	int offset2 = rand() % 5;
	time_t t;

	srand((unsigned) time(&t));//init for rand function
	array1 = (char *)malloc(MEMORY_CHUNK_SIZE);// create array of very large size
	array2 = (char *)malloc(MEMORY_CHUNK_SIZE);

	//random operations for 1 byte
	int index = 0;
	gettimeofday(&start,NULL);	
	while(index <= 1048576){
		memcpy(&array2[index+offset2],&array1[index+offset1],BUFFER_BYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	double diff = end.tv_usec - start.tv_usec;
	latency_random_byte[i] = diff * 1000;
	double throughput_byte = (1048576.0 * 1000000 / diff)/(1024.0 * 1024);
	throughput_random_byte[i] = throughput_byte; 
	
	//random operations for 1 KB
	index = 0;
	offset1 = rand() % 140;
	offset2 = rand() % 60;
	gettimeofday(&start,NULL);
	while(index < 5){
		memcpy(&array2[index+offset1],&array1[index+offset2],BUFFER_KBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;	
	latency_random_kb[i] = diff * 1000;
	double throughput_kbyte = (5.0 * 1000000 / diff)/(1024.0);
	throughput_random_kb[i] = throughput_kbyte;
	
	//random operations for 1 MB
	index=0;
	offset1 = rand() % 199;
	offset2 = rand() % 88;
	while(index < 7){
		memcpy(&array2[index+offset2],&array1[index+offset1],BUFFER_MBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
    latency_random_mb[i] = diff * 1000;
	double throughput_mbyte = (7.0 * 1000000 / diff);
	throughput_random_mb[i] = throughput_mbyte;
	i++;
}

void *sequentialReadWrite(){
	char *array1,*array2;
	struct timeval start,end;
	array1 = (char *)malloc(MEMORY_CHUNK_SIZE);
	array2 = (char *)malloc(MEMORY_CHUNK_SIZE);
	
	//sequential read + write for 1 byte
	int index=0;
	gettimeofday(&start,NULL);	
	while(index <= 1048576){
		memcpy(&array2[index],&array1[index],BUFFER_BYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	double diff = end.tv_usec - start.tv_usec;
	latency_sequential_byte[j] = diff*1000;
	double throughput_byte = (1048576.0 * 1000000 / diff)/(1024.0 * 1024);
	throughput_sequential_byte[j] = throughput_byte;
	
	//sequential read+write for 1 KB
	index = 0;
	gettimeofday(&start,NULL);
	while(index < 5){
		memcpy(&array2[index],&array1[index],BUFFER_KBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	latency_sequential_kb[j] = diff*1000;
	double throughput_kbyte = (5.0 * 1000000.0 / diff)/(1024.0);
	throughput_sequential_kb[j] = throughput_kbyte;
	
	//sequential read + write operations for 1 MB
	index=0;
	while(index < 7){
		memcpy(&array2[index],&array1[index],BUFFER_MBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	latency_sequential_mb[j] = diff*1000;
	double throughput_mbyte = (7.0 * 1000000 / diff);
	throughput_sequential_mb[j] = throughput_mbyte;
	j++;
}

void main(){
	pthread_t thread1,thread2,thread3,thread4;
	int i;
	struct timeval start,end;

	gettimeofday(&start,NULL);
	pthread_create(&thread1,NULL,sequentialReadWrite,NULL);
	pthread_create(&thread2,NULL,sequentialReadWrite,NULL);
	gettimeofday(&end,NULL);

	double diff = end.tv_usec - start.tv_usec;
	
	pthread_join(thread1,NULL);
	pthread_join(thread2,NULL);

	gettimeofday(&start,NULL);
	pthread_create(&thread3,NULL,randomReadWrite,NULL);
	pthread_create(&thread4,NULL,randomReadWrite,NULL);
	gettimeofday(&end,NULL);

	diff = end.tv_usec - start.tv_usec;

	pthread_join(thread3,NULL);
	pthread_join(thread4,NULL);
	
	//calculate average latency and throughput
	
	double latency_random_byte_avg = (latency_random_byte[0] + latency_random_byte[1])/2.0;
	double latency_random_kb_avg = (latency_random_kb[0] + latency_random_kb[1])/2.0;
	double latency_random_mb_avg = (latency_random_mb[0] + latency_random_mb[1])/2.0;

	printf("The value of latency in ms for random 1 Byte is %lf\n",latency_random_byte_avg);	
	printf("The value of latency in ms for random 1 KB is %lf\n",latency_random_kb_avg);
	printf("The value of latency in ms for random 1 MB is %lf\n",latency_random_mb_avg);

	double throughput_random_byte_avg = (throughput_random_byte[0] + throughput_random_byte[1])/2.0;
	double throughput_random_kb_avg = (throughput_random_kb[0] + throughput_random_kb[1])/2.0;
	double throughput_random_mb_avg = (throughput_random_mb[0] + throughput_random_mb[1])/2.0;
	
	printf("The value of Throughput in MB/s for random 1 Byte is %lf\n",throughput_random_byte_avg);
	printf("The value of Throughput in MB/s for random 1 KB is %lf\n",throughput_random_kb_avg);
	printf("The value of Throughput in MB/s for random 1 MB is %lf\n",throughput_random_mb_avg);

	double latency_sequential_byte_avg = (latency_sequential_byte[0] + latency_sequential_byte[1])/2.0;
	double latency_sequential_kb_avg = (latency_sequential_kb[0] + latency_sequential_kb[1])/2.0;
	double latency_sequential_mb_avg = (latency_sequential_mb[0] + latency_sequential_mb[1])/2.0;

	printf("The value of latency in ms for sequential 1 Byte is %lf\n",latency_sequential_byte_avg);	
	printf("The value of latency in ms for sequential 1 KB is %lf\n",latency_sequential_kb_avg);
	printf("The value of latency in ms for sequential 1 MB is %lf\n",latency_sequential_mb_avg);

	double throughput_sequential_byte_avg = (throughput_random_byte[0] + throughput_sequential_byte[1])/2.0;
	double throughput_sequential_kb_avg = (throughput_random_kb[0] + throughput_sequential_kb[1])/2.0;
	double throughput_sequential_mb_avg = (throughput_random_mb[0] + throughput_sequential_mb[1])/2.0;
	
	printf("The value of Throughput in MB/s for sequential 1 Byte is %lf\n",throughput_sequential_byte_avg);
	printf("The value of Throughput in MB/s for sequential 1 KB is %lf\n",throughput_sequential_kb_avg);
	printf("The value of Throughput in MB/s for sequential 1 MB is %lf\n",throughput_sequential_mb_avg);	
}
