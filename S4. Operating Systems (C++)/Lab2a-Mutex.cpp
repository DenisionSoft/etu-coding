#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

typedef struct {
    int flag;
    char sym;
    pthread_mutex_t* mutex;
}targs;

void* proc1(void *arg) {  
    printf("\nпоток 1 начал свою работу\n");
    targs *args = (targs*) arg; 

    while(args->flag == 0) {
        pthread_mutex_lock(args->mutex);
        int i = 0;
        while (i < 5) {
        putchar(args->sym);
        fflush(stdout);
        i++;
        sleep(1);
        }
        pthread_mutex_unlock(args->mutex);
        sleep(1);
    }
    
    printf("\nпоток 1 закончил свою работу\n");
    pthread_exit((void*)1);
    return 0;
}

void* proc2(void *arg) {  
    printf("\nпоток 2 начал свою работу\n");
    targs *args = (targs*) arg; 

    while(args->flag == 0) {
        pthread_mutex_lock(args->mutex);
        int i = 0;
        while (i < 5) {
        putchar(args->sym);
        fflush(stdout);
        i++;
        sleep(1);
        }
        pthread_mutex_unlock(args->mutex);
        sleep(1);
    }
    
    printf("\nпоток 2 закончил свою работу\n");
    pthread_exit((void*)2);
    return 0;
}

int main() {
    printf("программа начала работу\n");

    targs arg1;
    targs arg2;
    arg1.flag = 0;
    arg1.sym = '1';
    arg2.flag = 0;
    arg2.sym = '2';

    pthread_mutex_t mutarg;
    pthread_mutex_init(&mutarg, NULL);
    arg1.mutex = &mutarg;
    arg2.mutex = &mutarg;

    pthread_t id1;
    pthread_t id2;
    pthread_create(&id1, NULL, proc1, &arg1);
    pthread_create(&id2, NULL, proc2, &arg2);

    printf("\nпрограмма ждет нажатия клавиши\n");
    getchar();

    printf("\nклавиша нажата\n");
    arg1.flag = 1;
    arg2.flag = 1;

    pthread_mutex_destroy(&mutarg);

    int *exitcode1;
    int *exitcode2;
    pthread_join(id1, (void**)&exitcode1);
    printf("exitcode1 = %p\n", exitcode1);
    pthread_join(id2, (void**)&exitcode2);
    printf("exitcode2 = %p\n",exitcode2);

    printf("программа завершила работу\n");
    return 0;
}
