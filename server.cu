#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include "cuda_kernels.cuh"

#define MESSAGE_LENGTH 10
#define MAX_MESSAGE_LENGTH 134217728
#define INPUT_MESSAGE_LENGTH 67108864
#define OUTPUT_MESSAGE_LENGTH 3145728
#define GPU_COUNT 1
#define GPU_STREAM_COUNT 4 
#define MAX_THREADS_COUNT 100

struct request_info{
      int thread_number;
      int socket;
      int kafka_message_id;
      int log_id;
};


// void * function(void *socket_arg){
//    int check;
//    int buffer [MESSAGE_LENGTH];
//    bzero(buffer,MESSAGE_LENGTH);
//    int sock = (int)socket_arg;
//    check = read(sock,buffer,MESSAGE_LENGTH);
  
   
//    if (check < 0) {
//       perror("error couldn't read from the socket_arg");
//       exit(1);
//    }
 
   
//    int another_message [MESSAGE_LENGTH];
//    // fill another_message with the results of processing later on
//    check = write(sock, another_message, MESSAGE_LENGTH);
   
   
   
//    if (check < 0) {
//       perror("erro couldn't write to the socket_arg");
//       exit(1);
//    }
   
//    pthread_exit(NULL);
// }

void * process(void * arg){
      short buffer[INPUT_MESSAGE_LENGTH / 2];
      int check_operation ;
      struct request_info * curr_request_info = (struct request_info *) arg;
      int thread_number = curr_request_info->thread_number;
      int socket = curr_request_info->socket;
      int kafka_message_id = curr_request_info->kafka_message_id;
      int GPU_device = thread_number % GPU_COUNT ;
      int GPU_stream = thread_number % GPU_STREAM_COUNT;
      
      int log_id = curr_request_info->log_id;
      
      // continue with the logging here
      
      
     
      check_operation = read(socket, buffer, INPUT_MESSAGE_LENGTH);
      if(check_operation < 0){
            perror("error reading data");
            exit(1);
      }
      
      int message_length_shorts = INPUT_MESSAGE_LENGTH / 2 ;
      int results_count = 0 ;
      int output_message_doubles = OUTPUT_MESSAGE_LENGTH / 8 ;
      double results[output_message_doubles];
      entry(buffer, results, &results_count, kafka_message_id, message_length_shorts);

      pthread_exit(NULL);
}






int main( int argc, char *argv[] ) {
   int log_counter = 0 ;
   int thread_count = 0 ;
   int socket_file_desc, new_socket_file_desc, port_number;
   socklen_t clilen;
   char buffer[256];
   struct sockaddr_in serv_addr, cli_addr;
   int n, pid;
   
   
   socket_file_desc = socket(AF_INET, SOCK_STREAM, 0);
   
   if (socket_file_desc < 0) {      
      perror("error can't open socket_arg");
      exit(1);
   }



   bzero((char *) &serv_addr, sizeof(serv_addr));
   port_number = 5001;
   
   serv_addr.sin_family = AF_INET;
   serv_addr.sin_addr.s_addr = INADDR_ANY;
   serv_addr.sin_port = htons(port_number);


   if (bind(socket_file_desc, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
      perror("erro can't bind");
      exit(1);
   }

   listen(socket_file_desc,15);
   clilen = sizeof(cli_addr);
   
   pthread_t threads[MAX_THREADS_COUNT];
   struct request_info req_info_arr[MAX_THREADS_COUNT];
   
   

   while (1) {
      new_socket_file_desc = accept(socket_file_desc, (struct sockaddr *) &cli_addr, &clilen);
		
      if (new_socket_file_desc < 0) {
         perror("error can't accept connections");
         exit(1);
      }
        
        
        int current_thread_number = thread_count ;
        req_info_arr[current_thread_number].thread_number = thread_count ;   
        req_info_arr[current_thread_number].socket = new_socket_file_desc ;
        /*
            kafka_message_id is always set to zero. needs to be handled later
        */
        req_info_arr[current_thread_number].kafka_message_id = 0;
        req_info_arr[current_thread_number].log_id = log_counter;
        thread_count ++;
        log_counter  ++;
        thread_count = thread_count % MAX_THREADS_COUNT ;
        
        
	  //pthread_t thread ;
	  //int check_creation = pthread_create(&thread, NULL,function, (void *) new_socket_file_desc );
        int check_operation = pthread_create(&threads[current_thread_number], NULL, process, (void *)&req_info_arr[current_thread_number]);
        
	  if(check_operation){
		  printf("error couldn't create thread\n");
	  }
	  log_counter ++ ;
   }
}