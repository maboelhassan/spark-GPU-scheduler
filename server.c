#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>

void * function(void *socket){
   int check;
   char buffer[256];
   bzero(buffer,256);
   int sock = (int)socket;
   check = read(sock,buffer,255);
   
   if (check < 0) {
      perror("error couldn't read from the socket");
      exit(1);
   }
   
   check = write(sock,"I got your message",18);
   
   if (check < 0) {
      perror("erro couldn't write to the socket");
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
      perror("error can't open socket");
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
