#ifndef CUDA_H_   
#define CUDA_H_

__global__ void process_data_segment (float * d_out , float * d_in);
__global__ void process_data_segment(double *d_out, short * d_t1, short *d_t2, short *d_t3, int message_id);


void entry(short * input, double * result , int * number_results , int message_id , int input_size);

#endif
