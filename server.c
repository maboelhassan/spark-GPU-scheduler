#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>


#define MESSAGE_LENGTH 10
#define MAX_MESSAGE_LENGTH 134217728
#define INPUT_MESSAGE_LENGTH 67108864
#define OUTPUT_MESSAGE_LENGTH 3145728
#define GPU_COUNT 1
#define GPU_STREAM_COUNT 4 


struct request_info{
      int thread_number;
      int socket;
};


void * function(void *socket_arg){
   int check;
   int buffer [MESSAGE_LENGTH];
   bzero(buffer,MESSAGE_LENGTH);
   int sock = (int)socket_arg;
   check = read(sock,buffer,MESSAGE_LENGTH);
  
   
   if (check < 0) {
      perror("error couldn't read from the socket_arg");
      exit(1);
   }
 
   
   int another_message [MESSAGE_LENGTH];
   // fill another_message with the results of processing later on
   check = write(sock, another_message, MESSAGE_LENGTH);
   
   
   
   if (check < 0) {
      perror("erro couldn't write to the socket_arg");
      exit(1);
   }
   
   pthread_exit(NULL);
}

void * process(void * arg){
      short buffer[INPUT_MESSAGE_LENGTH / 2];
      int check_operation ;
      struct request_info * curr_request_info = (struct request_info *) arg;
      int thread_number = curr_request_info->thread_number;
      int socket = curr_request_info->socket;
      int GPU_device = thread_number % GPU_COUNT ;
      int GPU_stream = thread_number % GPU_STREAM_COUNT;
      
     
      check_operation = read(socket, buffer, INPUT_MESSAGE_LENGTH);
      if(check_operation < 0){
            perror("error reading data");
            exit(1);
      }
      
      
      
      
}






int main( int argc, char *argv[] ) {
   int thread_count = 0 ;
   int socket_file_desc, new_socket_file_desc, port_number, clilen;
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

   listen(socket_file_desc,5);
   clilen = sizeof(cli_addr);
   

   while (1) {
      new_socket_file_desc = accept(socket_file_desc, (struct sockaddr *) &cli_addr, &clilen);
		
      if (new_socket_file_desc < 0) {
         perror("ERROR while accepting connections");
         exit(1);
      }
        
        
        struct request_info req_info ;
        req_info.thread_number = thread_count ;   
        req_info.socket = new_socket_file_desc ; 
        thread_count ++;
        
        
	  pthread_t thread ;
	  int check_creation = pthread_create(&thread, NULL,function, (void *) new_socket_file_desc );
	  if(check_creation){
		  printf("error couldn't create thread\n");
	  }
		
   }
}
