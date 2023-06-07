#ifndef GENMATRIX_H
#define GENMATRIX_H

#include "number.h"

class genmatrix
{
private:
    int cols;
    int rank;
    number **mat;
protected:
    int rows;
public:
    genmatrix(int rows, int cols);
    number** getMatrix();
    number** matrixTranspose();
    int calculateRank();
};

#endif // GENMATRIX_H

#ifndef MATRIX_H
#define MATRIX_H

#include "genmatrix.h"

class matrix : public genmatrix
{
private:
    number det;
public:
    matrix(int size);
    int getSize();
    number determinant();
};

#endif // MATRIX_H
