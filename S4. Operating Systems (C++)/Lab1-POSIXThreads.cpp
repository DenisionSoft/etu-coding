#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <thread>
#include <time.h>

using namespace std;

typedef struct {
    int flag;
    char sym;
    double elapsed = 0;
}targs;

void* proc1(void *arg) {  
    printf("\nпоток 1 начал свою работу\n");

    targs *args = (targs*) arg; 

    while(args->flag == 0) {
        putchar(args->sym);
        fflush(stdout);
        sleep(1);
    }
    
    printf("поток 1 закончил свою работу\n");

    struct timespec ins;
    int s;
    clockid_t tid;
    s = pthread_getcpuclockid(pthread_self(), &tid);
    clock_gettime(tid, &ins);
    long seconds = ins.tv_sec;
    long nanoseconds = ins.tv_nsec;
    args->elapsed = seconds + nanoseconds*1e-9;
    pthread_exit((void*)1); 
    return 0;
}

void* proc2(void* arg) {    
    printf("\nпоток 2 начал свою работу\n");

    targs *args = (targs*) arg; 

    while(args->flag == 0) {
        putchar(args->sym);
        fflush(stdout);
        sleep(1);
    }

    printf("поток 2 закончил свою работу\n");

    struct timespec ins;
    int s;
    clockid_t tid;
    s = pthread_getcpuclockid(pthread_self(), &tid);
    clock_gettime(tid, &ins);
    long seconds = ins.tv_sec;
    long nanoseconds = ins.tv_nsec;
    args->elapsed = seconds + nanoseconds*1e-9;
    pthread_exit((void*)2);
    return 0;
}


int main() {
    printf("программа начала работу\n");

    struct timespec beg, mproc; 
    clockid_t cid, procid;
    int s, ss;

    targs arg1;
    targs arg2;
    arg1.flag = 0;
    arg1.sym = '1';
    arg2.flag = 0;
    arg2.sym = '2';

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
    printf("exitcode1 = %p, время работы потока 1 на процессоре = %.6f с\n", exitcode1, arg1.elapsed);
    pthread_join(id2, (void**)&exitcode2);
    printf("exitcode2 = %p, время работы потока 2 на процессоре = %.6f с\n",exitcode2, arg2.elapsed);

    s = pthread_getcpuclockid(pthread_self(), &cid);
    clock_gettime(cid, &beg);   
    long seconds = beg.tv_sec;
    long nanoseconds = beg.tv_nsec;
    double elapsed = seconds + nanoseconds*1e-9;
    printf("Время работы основного потока на процессоре = %.6f с.\n", elapsed);

    double total = arg1.elapsed + arg2.elapsed + elapsed;

    ss = clock_getcpuclockid(0, &procid);
    clock_gettime(procid, &mproc);   
    seconds = mproc.tv_sec;
    nanoseconds = mproc.tv_nsec;
    elapsed = seconds + nanoseconds*1e-9;
    printf("Общее время работы на процессоре = %.6f с.\n", elapsed);

    printf("Общее время (%.3f с) примерно равно сумме времени работы потоков (%.3f с)\n", elapsed, total);

    printf("программа завершила работу\n");
    return 0;
}
