#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <string.h>
#include <sys/resource.h>
#include <errno.h>
#include <fcntl.h>
#include <string>


using namespace std;

typedef struct {
    int flag;
    char sym;
    int fd;
}targs;

void* proc1(void *arg) {  
    printf("\nпоток 1 начал свою работу\n");
    
    struct rlimit rl;
    unsigned long res;
    string resins;
    char buff[256];
    targs *args = (targs*) arg; 

    while(args->flag == 0) {
        getrlimit(RLIMIT_CPU, &rl);

        res = rl.rlim_cur;
        resins = to_string(res);
        int leng = resins.length();
        const char *c = resins.c_str();
        copy(c, c+leng, buff);

        if(write(args->fd, buff, (size_t)leng) == -1) {
            printf("Ошибка в записывающем потоке %s\n", strerror(errno));
        }
        sleep(1);
    }
    
    printf("поток 1 закончил свою работу\n");
    pthread_exit((void*)1);
    return 0;
}

void* proc2(void* arg) {    
    printf("\nпоток 2 начал свою работу\n");
    targs *args = (targs*) arg;
    char buff[256];
    int cntr = 0;
    int size = 256;
    string cppstr;

    while(args->flag == 0) {
        if(read(args->fd, buff, size) == -1){
            printf("Ошибка в читающем потоке %s\n", strerror(errno));
        }
        else {
            printf("%s\n", buff);
        }
        if ((cntr > 0) && (cntr < 4)) {
            cppstr = buff;
            size = cppstr.length();
            }
        cntr++;
        sleep(1);
    }

    printf("поток 2 закончил свою работу\n");
    pthread_exit((void*)2);
    return 0;
}

int main() {
    printf("программа начала работу\n");

    int filedes[2];
    pipe(filedes);
    fcntl(filedes[0], F_SETFL, O_NONBLOCK);
    fcntl(filedes[1], F_SETFL, O_NONBLOCK);

    targs arg1;
    targs arg2;
    arg1.flag = 0;
    arg1.sym = '1';
    arg1.fd = filedes[1];
    arg2.flag = 0;
    arg2.sym = '2';
    arg2.fd = filedes[0];

    pthread_t id1;
    pthread_t id2;
    pthread_create(&id1, NULL, proc1, &arg1);
    pthread_create(&id2, NULL, proc2, &arg2);

    printf("программа ждет нажатия клавиши\n");
    getchar();

    printf("клавиша нажата\n");
    arg1.flag = 1;
    arg2.flag = 1;

    int *exitcode1;
    int *exitcode2;
    pthread_join(id1, (void**)&exitcode1);
    printf("exitcode1 = %p\n", exitcode1);
    pthread_join(id2, (void**)&exitcode2);
    printf("exitcode2 = %p\n",exitcode2);

    close(filedes[0]);
    close(filedes[1]);

    printf("программа завершила работу\n");
    return 0;
}
