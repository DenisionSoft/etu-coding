#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <iostream>

int main() {
    printf("процесс 2 начал работу\n");
    putenv("PATH=.");
    pid_t pid = fork();

    char *argv[4];
    argv[0] = "Lab4.1";
    argv[1] = "H";
    argv[2] = "i";
    argv[3] = NULL;

    int ret;

    if (pid == 0) {
        execvp("Lab4.1", argv);
    }
    else if (pid >0) {
        printf("ID процесса родителя: %d\n", getppid());
        printf("ID процесса 2: %d\n", getpid()); 
        printf("ID дочернего процесса: %d\n", pid); 

        while (waitpid(pid, &ret, WNOHANG) == 0) {
            printf("\n...ждём...\n");
            usleep(500000);
        }

        printf("дочерний процесс завершился с кодом %d\n", ret);
    }
    else {
        perror("fork");
    }

    printf("процесс 2 закончил работу\n");
    
    return 0;
}