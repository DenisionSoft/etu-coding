#ifndef TMATRIX_H
#define TMATRIX_H

#include "number.h"
#include <iostream>

using namespace std;

class TMatrix
{
public:
    TMatrix();
    TMatrix(size_t);
    TMatrix(size_t, number**);
    TMatrix(const TMatrix&);
    ~TMatrix();

    TMatrix& operator= (const TMatrix&);
    number determinant();
    TMatrix transpose();
    size_t rank();

private:
    size_t size;
    number** arr;

    void deleteMatrix();
    void swapRows(size_t, size_t);
    void swapCols(size_t, size_t);
    TMatrix gauss();
    friend ostream& operator<< (ostream&, TMatrix&);
};

#endif // TMATRIX_H
