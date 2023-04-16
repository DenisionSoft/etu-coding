#ifndef GENMATRIX_H
#define GENMATRIX_H

#include "number.h"

class genmatrix
{
private:
    int cols;
    int rank;
protected:
    int rows;
    number **mat;
public:
    genmatrix(int rows, int cols);
    number** getMatrix();
    number** matrixTranspose();
    int calculateRank();
};

#endif // GENMATRIX_H
