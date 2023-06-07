#include "genmatrix.h"
#include "matrix.h"
#include "number.h"
#include <cmath>

matrix::matrix(int size) : genmatrix(size, size)
{
    this->det = 0;
}

int matrix::getSize()
{
    return this->rows;
}

number matrix::determinant()
{
    int size = this->rows;

    number det = 0;

    if (size == 1)
    {
        det = this->mat[0][0];
    }
    else if (size == 2)
    {
        det = this->mat[0][0] * this->mat[1][1] - this->mat[0][1] * this->mat[1][0];
    }
    else
    {
        for (int i = 0; i < size; i++)
        {
            matrix submatrix(size - 1);
            for (int j = 1; j < size; j++)
            {
                for (int k = 0; k < size; k++)
                {
                    if (k < i)
                        submatrix.getMatrix()[j-1][k] = this->mat[j][k];
                    else if (k > i)
                        submatrix.getMatrix()[j-1][k-1] = this->mat[j][k];
                }
            }
            det = det + (int)(pow(-1, i)) * this->mat[0][i] * submatrix.determinant();
        }
    }
    return det;
}
