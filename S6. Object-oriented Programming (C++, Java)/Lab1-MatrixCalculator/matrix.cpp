#include "matrix.h"
#include "number.h"
#include <cmath>

matrix::matrix(int size)
{
    this->size = size;
    this->rank = 0;
    this->det = 0;
    this->mat = new number*[size];
    for(int i = 0; i < size; i++)
    {
        this->mat[i] = new number[size];
        for (int j = 0; j < size; j++)
        {
            this->mat[i][j] = 0;
        }
    }
}

number** matrix::getMatrix()
{
    return this->mat;
}

int matrix::getSize()
{
    return this->size;
}

number matrix::determinant()
{
    number det = 0;
    if (this->size == 1)
    {
        det = this->mat[0][0];
    }
    else if (this->size == 2)
    {
        det = this->mat[0][0] * this->mat[1][1] - this->mat[0][1] * this->mat[1][0];
    }
    else
    {
        for (int i = 0; i < this->size; i++)
        {
            matrix submatrix(this->size - 1);
            for (int j = 1; j < this->size; j++)
            {
                for (int k = 0; k < this->size; k++)
                {
                    if (k < i)
                        submatrix.getMatrix()[j-1][k] = this->mat[j][k];
                    else if (k > i)
                        submatrix.getMatrix()[j-1][k-1] = this->mat[j][k];
                }
            }
            det += pow(-1, i) * this->mat[0][i] * submatrix.determinant();
        }
    }
    return det;
}

number** matrix::matrixTranspose()
{
    number** transpose = new number*[this->size];
    for (int i = 0; i < this->size; i++)
    {
        transpose[i] = new number[this->size];
        for (int j = 0; j < this->size; j++)
        {
            transpose[i][j] = this->mat[j][i];
        }
    }
    return transpose;
}

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
