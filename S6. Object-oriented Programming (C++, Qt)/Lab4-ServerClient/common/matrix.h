#ifndef MATRIX_H
#define MATRIX_H


#include "number.h"
class matrix
{
private:
    int size;
    int rank;
    number det;
    number **mat;
public:
    matrix();
    matrix(int size);
    number** getMatrix();
    int getSize();
    number determinant();
    number** matrixTranspose();
    int calculateRank();
};

#endif // MATRIX_H
