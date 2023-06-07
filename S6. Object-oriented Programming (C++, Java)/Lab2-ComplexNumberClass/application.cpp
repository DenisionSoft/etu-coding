#include "application.h"
#include "matrix.h"
#include "number.h"
#include <iostream>
#include <conio.h>


void application::Menu()
{
    matrix mat(3);
    int c, pos = 0;

    printf("Matrix calculator, version 2. Made by Medvedev Denis, Machula Maria & Smirnova Yulia, group 0374.");
    printf("\nPress any key to continue...");

    while(true) {
        c = _getwch();
        system("cls");
        if (c == 224) continue;
        switch (c) {
        case 72:
            if (pos > 1) pos--;
            else pos += 5;
            break;
        case 80:
            if (pos < 6) pos++;
            else pos -= 5;
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
        case 27:
            return;
        }
        MenuRender(pos);
        if (c == 13)
            switch (pos)
            {
            case 1: {
                int nsize;
                std::cout << "Enter size of matrix: ";
                std::cin >> nsize;
                std::cout << "Enter each element with real part first, press Enter, then imaginary part without i, press Enter again to move on.\n";
                mat = matrix(nsize);
                SetMatrix(mat);
                std::cout << "Result:\n";
                PrintMatrix(mat);
                break;
            }
            case 2: {
                number det = mat.determinant();
                std::cout << "Determinator: " << det << std::endl;
                break;
            }
            case 3: {
                number** trans = mat.matrixTranspose();
                CopyMatrix(trans, mat);
                std::cout << "Transposed matrix:\n";
                PrintMatrix(mat);
                break;
            }
            case 4: {
                int rank = mat.calculateRank();
                std::cout << "Rank: " << rank << std::endl;
                break;
            }
            case 5: {
                std::cout << "Current matrix:" << std::endl;
                PrintMatrix(mat);
                break;
            }
            case 6: {
                std::cout << "Exit" << std::endl;
                return;
            }
            }
    }
}

void application::MenuRender(int pos)
{
    printf("Use arrow keys and num keys 1-6 to navigate. Press Enter to select and ESC to exit.\n\n");
    pos == 1 ? printf("> ") : printf("  ");
    printf("1 - Set a matrix\n");
    pos == 2 ? printf("> ") : printf("  ");
    printf("2 - Calculate determinant\n");
    pos == 3 ? printf("> ") : printf("  ");
    printf("3 - Calculate transponded matrix\n");
    pos == 4 ? printf("> ") : printf("  ");
    printf("4 - Calculate rank\n");
    pos == 5 ? printf("> ") : printf("  ");
    printf("5 - Show current matrix\n");
    pos == 6 ? printf("> ") : printf("  ");
    printf("6 - Exit\n");
}

void application::SetMatrix(matrix mat)
{
    number **matrix = mat.getMatrix();
    int size = mat.getSize();

    for(int i = 0; i < size; i++)
    {
        for(int j = 0; j < size; j++)
        {
            int it = i;
            int jt = j;
            std::cout << "Enter element [" << it + 1 << "][" << jt + 1 << "]: " << std::endl;
            std::cin >> matrix[i][j];
        }
    }
}

void application::PrintMatrix(matrix mat)
{
    number **matrix = mat.getMatrix();
    int size = mat.getSize();

    for(int i = 0; i < size; i++)
    {
        for(int j = 0; j < size; j++)
        {
            std::cout << matrix[i][j] << " ";
        }
        std::cout << std::endl;
    }
}

void application::CopyMatrix(number** from, matrix to)
{
    number **matrix = to.getMatrix();
    int size = to.getSize();

    for(int i = 0; i < size; i++)
    {
        for(int j = 0; j < size; j++)
        {
            matrix[i][j] = from[i][j];
        }
    }
}
