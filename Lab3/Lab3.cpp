#include <iostream>
#include <conio.h>
using namespace std;

void Menu(int pos);
void StartReversed(int num_rows, int num_columns);
void StartClockwise(int num_rows, int num_columns);
void CenterReversed(int num_rows, int num_columns);
void CenterClockwise(int num_rows, int num_columns);

int c = 0, pos = 0;
int num_rows = 3, num_columns = 3;

int main() {
	setlocale(0, "");
    printf("Спираль с меню, лабораторная работа №3, выполнил студент группы 0374 Медведев Денис Михайлович");
    printf("\nДля начала, нажмите на любую клавишу...");
    while (1) {
        c = _getch();
        system("cls");
        if (c == 224) continue;
        switch (c) {
        case 72:
            if (pos > 1) pos--;
            else pos += 7;
            break;
        case 80:
            if (pos < 8) pos++;
            else pos -= 7;
            break;
        case 49:
            pos = 1;
            break;
        case 50:
            pos = 2;
            break;
        case 51:
            pos = 3;
            break;
        case 52:
            pos = 4;
            break;
        case 53:
            pos = 5;
            break;
        case 54:
            pos = 6;
            break;
        case 55:
            pos = 7;
            break;
        case 56:
            pos = 8;
            break;
        case 27:
            return 0;
        }
        Menu(pos);
        if (c == 13)
            switch (pos)
            {
            case 1:
                CenterClockwise(num_rows, num_columns);
                break;
            case 2:
                CenterReversed(num_rows, num_columns);
                break;
            case 3:
                StartClockwise(num_rows, num_columns);
                break;
            case 4:
                StartReversed(num_rows, num_columns);
                break;
            case 5:
                num_columns++;
                break;
            case 6:
                if (num_columns > 2) num_columns--;
                break;
            case 7:
                num_rows++;
                break;
            case 8:
                if (num_rows > 2) num_rows--;
                break;
            }
    }
}

void Menu(int pos)
{
    printf("Используйте стрелки вверх и вниз и цифры от 1 до 8 для навигации. Нажмите ESC для выхода.");
    printf("\n1) Спираль от центра по часовой стрелке ");
    if (pos == 1) printf("<--");
    printf("\n2) Спираль от центра против часовой стрелки ");
    if (pos == 2) printf("<--");
    printf("\n3) Спираль к центру по часовой стрелке ");
    if (pos == 3) printf("<--");
    printf("\n4) Спираль к центру против часовой стрелки ");
    if (pos == 4) printf("<--");
    printf("\n5) Увеличить размер спирали по горизонтали ");
    if (pos == 5) printf("<--");
    printf("\n6) Уменьшить размер спирали по горизонтали ");
    if (pos == 6) printf("<--");
    printf("\n7) Увеличить размер спирали по вертикали ");
    if (pos == 7) printf("<--");
    printf("\n8) Уменьшить размер спирали по вертикали ");
    if (pos == 8) printf("<--");
    printf("\n\nТекущие размеры: %i на %i\n\n", num_rows, num_columns);
}

void StartClockwise(int num_rows, int num_columns)
{
    int s = 1;
    int** array = new int* [num_rows];
    for (int i = 0; i < num_rows; ++i)
        array[i] = new int[num_columns];

    for (int i = 0; i < num_rows; i++)
        for (int j = 0; j < num_columns; j++)
            array[i][j] = 0;

    for (int y = 0; y < num_columns; y++) {
        array[0][y] = s;
        s++;
    }
    for (int x = 1; x < num_rows; x++) {
        array[x][num_columns - 1] = s;
        s++;
    }
    for (int y = num_columns - 2; y >= 0; y--) {
        array[num_rows - 1][y] = s;
        s++;
    }
    for (int x = num_rows - 2; x > 0; x--) {
        array[x][0] = s;
        s++;
    }

    int c = 1;
    int d = 1;

    while (s < num_rows * num_columns) {

        while (array[c][d + 1] == 0) {
            array[c][d] = s;
            s++;
            d++;
        }

        while (array[c + 1][d] == 0) {
            array[c][d] = s;
            s++;
            c++;
        }

        while (array[c][d - 1] == 0) {
            array[c][d] = s;
            s++;
            d--;
        }

        while (array[c - 1][d] == 0) {
            array[c][d] = s;
            s++;
            c--;
        }
    }

    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] == 0) {
                array[x][y] = s;
            }
        }
    }


    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] < 10) {
                //cout для простого вывода массива и пробелов
                cout << array[x][y] << "  ";
            }
            else {
                cout << array[x][y] << " ";
            }
        }
        printf("\n");
    }
    for (int i = 0; i < num_rows; ++i)
        delete[] array[i];
    delete[] array;
}

void StartReversed(int num_rows, int num_columns)
{
    int s = 1;
    int** array = new int* [num_rows];
    for (int i = 0; i < num_rows; ++i)
        array[i] = new int[num_columns];

    for (int i = 0; i < num_rows; i++)
        for (int j = 0; j < num_columns; j++)
            array[i][j] = 0;
    for (int y = 0; y < num_rows; y++) {
        array[y][0] = s;
        s++;
    }

    for (int x = 1; x < num_columns; x++) {
        array[num_rows - 1][x] = s;
        s++;
    }

    for (int y = num_rows - 2; y >= 0; y--) {
        array[y][num_columns - 1] = s;
        s++;
    }

    for (int x = num_columns - 2; x > 0; x--) {
        array[0][x] = s;
        s++;
    }

    int c = 1;
    int d = 1;

    while (s < num_rows * num_columns) {
        while (array[c + 1][d] == 0) {
            array[c][d] = s;
            s++;
            c++;
        }

        while (array[c][d + 1] == 0) {
            array[c][d] = s;
            s++;
            d++;
        }

        while (array[c - 1][d] == 0) {
            array[c][d] = s;
            s++;
            c--;
        }

        while (array[c][d - 1] == 0) {
            array[c][d] = s;
            s++;
            d--;
        }
    }

    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] == 0) {
                array[x][y] = s;
            }
        }
    }

    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] < 10) {
                cout << array[x][y] << "  ";
            }
            else {
                cout << array[x][y] << " ";
            }
        }
        printf("\n");
    }
    for (int i = 0; i < num_rows; ++i)
        delete[] array[i];
    delete[] array;
}

void CenterReversed(int num_rows, int num_columns)
{
    int s = num_rows * num_columns;
    int** array = new int* [num_rows];
    for (int i = 0; i < num_rows; ++i)
        array[i] = new int[num_columns];

    for (int i = 0; i < num_rows; i++)
        for (int j = 0; j < num_columns; j++)
            array[i][j] = 0;

    for (int y = 0; y < num_columns; y++) {
        array[0][y] = s;
        s--;
    }
    for (int x = 1; x < num_rows; x++) {
        array[x][num_columns - 1] = s;
        s--;
    }
    for (int y = num_columns - 2; y >= 0; y--) {
        array[num_rows - 1][y] = s;
        s--;
    }
    for (int x = num_rows - 2; x > 0; x--) {
        array[x][0] = s;
        s--;
    }

    int c = 1;
    int d = 1;

    while (s > 1) {
        while (array[c][d + 1] == 0) {
            array[c][d] = s;
            s--;
            d++;
        }

        while (array[c + 1][d] == 0) {
            array[c][d] = s;
            s--;
            c++;
        }

        while (array[c][d - 1] == 0) {
            array[c][d] = s;
            s--;
            d--;
        }

        while (array[c - 1][d] == 0) {
            array[c][d] = s;
            s--;
            c--;
        }
    }

    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] == 0) {
                array[x][y] = s;
            }
        }
    }

    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] < 10) {
                cout << array[x][y] << "  ";
            }
            else {
                cout << array[x][y] << " ";
            }
        }
        printf("\n");
    }
    for (int i = 0; i < num_rows; ++i)
        delete[] array[i];
    delete[] array;
}

void CenterClockwise(int num_rows, int num_columns)
{
    int s = num_rows * num_columns;
    int** array = new int* [num_rows];
    for (int i = 0; i < num_rows; ++i)
        array[i] = new int[num_columns];

    for (int i = 0; i < num_rows; i++)
        for (int j = 0; j < num_columns; j++)
            array[i][j] = 0;
    for (int y = 0; y < num_rows; y++) {
        array[y][0] = s;
        s--;
    }
    for (int x = 1; x < num_columns; x++) {
        array[num_rows - 1][x] = s;
        s--;
    }
    for (int y = num_rows - 2; y >= 0; y--) {
        array[y][num_columns - 1] = s;
        s--;
    }
    for (int x = num_columns - 2; x > 0; x--) {
        array[0][x] = s;
        s--;
    }

    int c = 1;
    int d = 1;

    while (s > 1) {
        while (array[c + 1][d] == 0) {
            array[c][d] = s;
            s--;
            c++;
        }

        while (array[c][d + 1] == 0) {
            array[c][d] = s;
            s--;
            d++;
        }

        while (array[c - 1][d] == 0) {
            array[c][d] = s;
            s--;
            c--;
        }

        while (array[c][d - 1] == 0) {
            array[c][d] = s;
            s--;
            d--;
        }
    }

    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] == 0) {
                array[x][y] = s;
            }
        }
    }

    for (int x = 0; x < num_rows; x++) {
        for (int y = 0; y < num_columns; y++) {
            if (array[x][y] < 10) {
                cout << array[x][y] << "  ";
            }
            else {
                cout << array[x][y] << " ";
            }
        }
        printf("\n");
    }
    for (int i = 0; i < num_rows; ++i)
        delete[] array[i];
    delete[] array;
}
