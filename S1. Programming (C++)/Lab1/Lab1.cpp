#include <iostream>
using namespace std;

int main()
{
    double num1, num2;
    int oper = 0;
    setlocale(0, "");

    printf("\nВведите первое число: ");
    while (!scanf_s("%lf", &num1)) {
        printf("Это не число!\n");
        printf("Введите первое число: ");
        scanf_s("%*[^\r\n]");
    }

    printf("\nВведите второе число: ");
    while (!scanf_s("%lf", &num2)) {
        printf("Это не число!\n");
        printf("Введите первое число: ");
        scanf_s("%*[^\r\n]");
    }

    while ((oper != 1) && (oper != 2) && (oper != 3) && (oper != 4)) {
        printf("\nВведите номер операции (1 для +, 2 для -, 3 для *, 4 для /): ");
        while ((!scanf_s("%i", &oper))) {
            printf("Операции несуществует, попробуйте снова: ");
            scanf_s("%*[^\r\n]");
        }
    }

    switch (oper)
    {
    case 1:
        printf("Ответ = %lf\n\n", num1 + num2);
        break;
    case 2:
        printf("Ответ = %lf\n\n", num1 - num2);
        break;
    case 3:
        printf("Ответ = %lf\n\n", num1 * num2);
        break;
    case 4:
        if (num2 == 0) {
            printf("\n\n\aДеление на 0! Попробуйте снова.\n\n");
            while (num2 == 0) {
                printf("Введите второе число, отличное от нуля: ");
                while ((!scanf_s("%lf", &num2))) {
                    printf("Это не число! Попробуйте снова: ");
                    scanf_s("%*[^\r\n]");
                }
            }
            printf("Ответ = %lf\n", num1 / num2);
        }
        else
            printf("Ответ = %lf\n", num1 / num2);
        break;
    default:
        break;
    }
    return 0;
}