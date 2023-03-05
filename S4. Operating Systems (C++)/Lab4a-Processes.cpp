#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

int main(int argc, char* argv[]) {

    printf("\nпроцесс 1 начал работу\n");
    printf("ID процесса 1: %d\n", getpid()); 
    printf("ID процесса родителя: %d\n", getppid()); 

    for(int i = 0; i < argc; i++) {
        printf("%s ", argv[i]);
        fflush(stdout);
        sleep(1);
    }

    printf("\nпроцесс 1 закончил работу\n");
    int retv = 0;
    for(int i = 0; i < argc-1; i++) {
        retv += (int)argv[i][0];
    }

    return retv;
}