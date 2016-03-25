#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>


#define MESSAGE_LENGTH 10

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



int main( int argc, char *argv[] ) {
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
      
	  pthread_t thread ;
	  int check_creation = pthread_create(&thread, NULL,function, (void *) new_socket_file_desc );
	  if(check_creation){
		  printf("error couldn't create thread\n");
	  }
		
   }
}
