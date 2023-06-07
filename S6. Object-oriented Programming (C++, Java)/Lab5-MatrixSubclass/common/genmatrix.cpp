#include "genmatrix.h"
#include "number.h"
#include <cmath>

genmatrix::genmatrix(int rows, int cols)
{
    this->rows = rows;
    this->cols = cols;
    this->rank = 0;
    this->mat = new number*[rows];
    for(int i = 0; i < rows; i++)
    {
        this->mat[i] = new number[cols];
        for (int j = 0; j < cols; j++)
        {
            this->mat[i][j] = 0;
        }
    }
}

number** genmatrix::getMatrix()
{
    return this->mat;
}

number** genmatrix::matrixTranspose()
{
    number** transpose = new number*[this->cols];
    for (int i = 0; i < this->cols; i++)
    {
        transpose[i] = new number[this->rows];
        for (int j = 0; j < this->rows; j++)
        {
            transpose[i][j] = this->mat[j][i];
        }
    }
    return transpose;
}

/*
int matrix::calculateRank()
{
    int rank = 0;
    number** temp = new number*[this->size];
    for (int i = 0; i < this->size; i++)
    {
        temp[i] = new number[this->size];
        for (int j = 0; j < this->size; j++)
        {
            temp[i][j] = this->mat[i][j];
        }
    }
    for (int i = 0; i < this->size; i++)
    {
        int pivot_row = i;
        while (pivot_row < this->size && temp[pivot_row][i] == 0) {
            pivot_row++;
        }
        if (pivot_row == this->size) {
            continue;
        }
        if (pivot_row != i) {
            number* temp2 = temp[i];
            temp[i] = temp[pivot_row];
            temp[pivot_row] = temp2;
        }
        for (int j = i + 1; j < this->size; j++)
        {
            number c = temp[j][i] / temp[i][i];
            for (int k = 0; k < this->size; k++)
            {
                temp[j][k] = temp[j][k] - c * temp[i][k];
            }
        }
        rank++;
    }
    return rank;
}
*/

int genmatrix::calculateRank()
{
    int rank = 0;
    number** temp = new number*[this->rows];
    for (int i = 0; i < this->rows; i++)
    {
        temp[i] = new number[this->cols];
        for (int j = 0; j < this->cols; j++)
        {
            temp[i][j] = this->mat[i][j];
        }
    }
    for (int i = 0; i < this->rows; i++)
    {
        int pivot_row = i;
        while (pivot_row < this->rows && temp[pivot_row][i] == 0) {
            pivot_row++;
        }
        if (pivot_row == this->rows) {
            continue;
        }
        if (pivot_row != i) {
            number* temp2 = temp[i];
            temp[i] = temp[pivot_row];
            temp[pivot_row] = temp2;
        }
        for (int j = i + 1; j < this->rows; j++)
        {
            number c = temp[j][i] / temp[i][i];
            for (int k = 0; k < this->cols; k++)
            {
                temp[j][k] = temp[j][k] - c * temp[i][k];
            }
        }
        rank++;
    }
    return rank;
}