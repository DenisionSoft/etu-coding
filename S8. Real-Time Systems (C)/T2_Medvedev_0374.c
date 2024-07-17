/*
 * Выполнил: Медведев Денис, 0374
 * Задание: Т2. Приложение Linux: "Вызов лифта"
 * Дата выполнения: 26.02.2024
 * Версия: 0.1
 *
 * Скрипт для компиляции и запуска программы:
 * gcc T2_Medvedev_0374.c && ./a.out
 */
// ------------------------------------------- // 
/*
 * Общее описание программы:
 * Программа в рамках задания имитирует работу лифта.
 * Пользователь может нажимать кнопки "u" и "d" для вызова лифта на этаж.
 * 
 * Программа использует сигналы для управления лифтом.
 */
// ------------------------------------------- //

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <signal.h>
#include <unistd.h>

#define TOP        0
#define BOTTOM     1
#define THIS_FLOOR 2

#define Up   48
#define Down 49

int state;
sigset_t sigset;

void move(int direction, int target) {
    char *dir = (direction == Up) ? "UP" : "DOWN";

    int n = 3;
    for (int i = 1; i <= n; i++) {
        printf("*** Go %s\n", dir);
        usleep(500000);
    }

    state = target;
    printf("state %d\n", state);
}

void lift_control(int signo, siginfo_t *info, void *nk) {
    switch (state) {
        case THIS_FLOOR:
            if (signo == Up) {
                printf("This Floor, door is opened\n");
                sleep(1);
                move(Up, TOP);
            }
            if (signo == Down) {
                printf("This Floor, door is opened\n");
                sleep(1);
                move(Down, BOTTOM);
            }
            break;
        case TOP:
            if (signo == Up) {
                move(Down, THIS_FLOOR);
                printf("This Floor, door is opened\n");
                sleep(1);
                move(Up, TOP);
            }
            if (signo == Down) {
                move(Down, THIS_FLOOR);
                printf("This Floor, door is opened\n");
                sleep(1);
                move(Down, BOTTOM);
            }
            break;
        case BOTTOM:
            if (signo == Up) {
                move(Up, THIS_FLOOR);
                printf("This Floor, door is opened\n");
                sleep(1);
                move(Up, TOP);
            }
            if (signo == Down) {
                move(Up, THIS_FLOOR);
                printf("This Floor, door is opened\n");
                sleep(1);
                move(Down, BOTTOM);
            }
            break;
    }
}

void *push_button(void *args) {
    char ch;

    struct sigaction act;
    act.sa_sigaction = lift_control;
    act.sa_flags = SA_SIGINFO;
    act.sa_mask = sigset;
    sigaction(Up, &act, NULL);
    sigaction(Down, &act, NULL);

    while (ch != 'q') {
        ch = getchar();
        switch (ch) {
            case 'u':
                raise(Up);
                break;
            case 'd':
                raise(Down);
                break;
            case 'q':
                break;
        }
    }
    return NULL;
}

int main() {
    pthread_t t;
    printf("Start\n");
    state = THIS_FLOOR;

    sigemptyset(&sigset);
    sigaddset(&sigset, Up);
    sigaddset(&sigset, Down);

    pthread_create(&t, NULL, &push_button, NULL);
    pthread_join(t, NULL);

    printf("Finish\n");
    return 0;
}

// ----------------------------------------//
/*
    Start
    u
    This Floor, door is opened
    *** Go UP
    *** Go UP
    *** Go UP
    state 0
    u
    *** Go DOWN
    *** Go DOWN
    *** Go DOWN
    state 2
    This Floor, door is opened
    *** Go UP
    *** Go UP
    *** Go UP
    state 0
    d
    *** Go DOWN
    *** Go DOWN
    *** Go DOWN
    state 2
    This Floor, door is opened
    *** Go DOWN
    *** Go DOWN
    *** Go DOWN
    state 1
    q
    Finish
*/