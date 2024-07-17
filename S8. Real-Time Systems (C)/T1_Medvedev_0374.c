/*
 * Выполнил: Медведев Денис, 0374
 * Задание: Т1. Приложение Linux: "Обеспечение таймаута"
 * Дата выполнения: 16.02.2024
 * Версия: 0.1
 *
 * Скрипт для компиляции и запуска программы:
 * gcc T1_Medvedev_0374.c && ./a.out
 */
// ------------------------------------------- // 
/*
 * Общее описание программы:
 * Для искуственной задержки используются случайные числа от 0 до 30000 микросекунд.
 * Дозволенный предел задержки (по условию) - 4% от 500000 микросекунд = 20000 микросекунд.
 * Для искуственного зависания случайное число проверяется на кратность 3,
 * что даёт вероятность приблизительно 30%.
 * В качестве дедлайн-сигнала используется пользовательский SIGUSR2.
 */
// ------------------------------------------- //

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <sys/time.h>
#include <unistd.h>

// Обработчик сигнала SIGUSR2
void deadlineHandler(int sig) {
    printf("####   Deadline exceeded   ####\n");
}

// Обработчик сигнала SIGALRM
void alarmHandler(int sig) {
    printf("#####-- Restart reqired! --#####\n");
    exit(SIGUSR1);
}

// Функция искуственной нагрузки
void doControl() {
    int t, x;

    srandom(time(NULL));
    // случайная задержка от 0 до 30000 микросекунд
    double dt = (double)random() / (double)RAND_MAX * 30000;
    t = 500000 + dt;

    printf("########## %d\n", t);

    // вероятность зависания приблизительно 1/3
    x = random() % 3;
    if (x == 0) while(1);

    usleep(t);
}

// Установка обработчика сигнала
void set_sigaction(int sig, void (*handler)(int)) {
    struct sigaction act;
    act.sa_handler = handler;
    act.sa_flags = 0;
    sigemptyset(&act.sa_mask);
    sigaddset(&act.sa_mask, sig);
    sigaction(sig, &act, NULL);
}

int main() {
    // установка обработчиков сигналов
    set_sigaction(SIGALRM, alarmHandler);
    set_sigaction(SIGUSR2, deadlineHandler);
    
    struct timeval fro, to;

    while(1) {
        // установка таймера на 1 секунду
        alarm(1);
        // измерение времени выполнения функции
        gettimeofday(&fro, NULL);
        doControl();
        gettimeofday(&to, NULL);
        // сброс таймера
        alarm(0);

        int delta = (to.tv_sec - fro.tv_sec) * 1000000 + (to.tv_usec - fro.tv_usec);

        // проверка превышения мягкого дедлайна
        if (delta > 500000 * 1.04) {
            raise(SIGUSR2);
        }

        // ожидание до следующего цикла
        usleep(3000000 - delta);
    }
}

// ----------------------------------------//
/*
    ########## 526757
    ####   Deadline exceeded   ####
    ########## 513881
    ########## 501149
    ########## 503510
    ########## 505720
    ########## 522963
    #####-- Restart reqired! --#####
*/
