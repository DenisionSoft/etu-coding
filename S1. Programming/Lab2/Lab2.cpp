#include <iostream>
#include <conio.h>
using namespace std;

int main()
{
    setlocale(0, "");
    int c = 0, pos = 1;
    double num1 = 0, num2 = 0;
    printf("Калькулятор с меню, лабораторная работа №2, выполнил студент группы 0374 Медведев Денис Михайлович");
    printf("\nДля начала, нажмите на любую клавишу...");
    while (1) {
        c = _getch();
        system("cls");
        if (c == 224) continue;
        switch (c) {
        case 72:
            if (pos > 1) pos--;
            else pos += 6;
            break;
        case 80:
            if (pos < 7) pos++;
            else pos -= 6;
            break;
        }
        printf("Используйте стрелки вверх и вниз и цифры от 1 до 7 для навигации");
        printf("\n1) Операция сложения ");
        if (pos == 1) printf("<--");
        printf("\n2) Операция вычитания ");
        if (pos == 2) printf("<--");
        printf("\n3) Операция умножения ");
        if (pos == 3) printf("<--");
        printf("\n4) Операция деления ");
        if (pos == 4) printf("<--");
        printf("\n5) Ввод первого числа ");
        if (pos == 5) printf("<--");
        printf("\n6) Ввод второго числа ");
        if (pos == 6) printf("<--");
        printf("\n7) Выход из программы ");
        if (pos == 7) printf("<--");
        printf("\n\nПервое число %lf ", num1);
        printf("\nВторое число %lf \n", num2);
        if (c == 13)
            switch (pos)
            {
            case 1:
                printf("\nРезультат сложения = %lf ", num1+num2);
                break;
            case 2:
                printf("\nРезультат вычитания = %lf ", num1-num2);
                break;
            case 3:
                printf("\nРезультат умножения = %lf ", num1*num2);
                break;
            case 4:
                if (num2 == 0) { printf("\nДеление на 0! Введите второе число, отличное от нуля."); continue; }
                printf("\nРезультат деления = %lf ", num1/num2);
                break;
            case 5:
                printf("\nВведите новое значение первого числа: ");
                scanf_s("%lf", &num1);
                break;
            case 6:
                printf("\nВведите новое значение второго числа: ");
                scanf_s("%lf", &num2);
                break;
            case 7:
                return 0;
            }
        switch (c)
        {
        case 49: printf("\nРезультат сложения = %lf ", num1 + num2); break;
        case 50: printf("\nРезультат вычитания = %lf ", num1 - num2); break;
        case 51: printf("\nРезультат умножения = %lf ", num1 * num2); break;
        case 52:
            if (num2 == 0) { printf("\nДеление на 0! Введите второе число, отличное от нуля."); continue; }
            printf("\nРезультат деления = %lf ", num1 / num2);
            break;
        case 53:
            printf("\nВведите новое значение первого числа: ");
            scanf_s("%lf", &num1);
            break;
        case 54:
            printf("\nВведите новое значение второго числа: ");
            scanf_s("%lf", &num2);
            break;
        case 55: return 0;
        }
    }

    return 0;
}