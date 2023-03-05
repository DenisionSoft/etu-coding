#include "matrix.h"

// Конструкторы и деструкторы
TMatrix::TMatrix() {
    size = 0;
    arr = nullptr;
}

TMatrix::TMatrix(size_t n) {
    size = n;
    arr = new number*[size];

    for (size_t i = 0; i < size; i++) {
        arr[i] = new number[size];
        for (size_t j = 0; j < size; j++) {
            arr[i][j] = 0;
        }
    }
}

TMatrix::TMatrix(size_t n, number** nums) {
    size = n;
    arr = new number*[size];

    for (size_t i = 0; i < size; i++) {
        arr[i] = new number[size];
        for (size_t j = 0; j < size; j++) {
            arr[i][j] = nums[i][j];
        }
    }
}

TMatrix::TMatrix(const TMatrix& matrix) {
    size = matrix.size;
    arr = new number*[size];

    for(size_t i = 0; i < size; i++) {
        arr[i] = new number[size];
        for(size_t j = 0; j < size; j++)
            arr[i][j] = matrix.arr[i][j];
    }
}

TMatrix::~TMatrix() {
    deleteMatrix();
}

// Публичные методы и функции
TMatrix& TMatrix::operator= (const TMatrix& matrix) {
    deleteMatrix();
    size = matrix.size;

    arr = new number*[size];
    for (size_t i = 0; i < size; i++){
        arr[i] = new number[size];
        for (size_t j = 0; j < size; j++) {
            arr[i][j] = matrix.arr[i][j];
        }
    }

    return *this;
}

number TMatrix::determinant() {
    TMatrix gMat(this->gauss());
    number det = number(1);

    for(size_t i = 0; i < size; i++)
        det *= gMat.arr[i][i];

    return det;
}

TMatrix TMatrix::transpose() {
    TMatrix matrix;

    number** transMat = new number*[size];
    for (size_t i = 0; i < size; i++)
        transMat[i] = new number[size];

    for (size_t i = 0; i < size; i++) {
        for (size_t j = i; j < size; j++) {
            transMat[j][i] = arr[i][j];
            transMat[i][j] = arr[j][i];
        }
    }

    matrix = TMatrix(size, transMat);
    for (size_t i = 0; i < size; i++)
        delete[] transMat[i];
    delete[] transMat;

    return matrix;
}

size_t TMatrix::rank() {
    TMatrix gMat(this->gauss());
    size_t r = 0;

    for(size_t i = 0; i < gMat.size; i++) {
        if (gMat.arr[i][i] != number(0))
            r++;
    }

    return r;
}

// Приватные методы и функции
void TMatrix::deleteMatrix() {
    if (arr != nullptr) {
        for (size_t i = 0; i < size; i++)
            delete[] arr[i];

        delete[] arr;
        arr = nullptr;
    }
}

void TMatrix::swapRows(size_t i, size_t j) {
    number* tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
}

void TMatrix::swapCols(size_t i, size_t j) {
    number tmp;
    for(size_t k = 0; k < size; k++) {
        tmp = arr[k][i];
        arr[k][i] = arr[k][j];
        arr[k][j] = tmp;
    }
}

TMatrix TMatrix::gauss() {
    TMatrix gMat(*this);

    for(size_t i = 0; i < gMat.size; i++) {
        if (gMat.arr[i][i] == number(0)) {
            bool seek = false;
            size_t row = i;
            size_t col = i;

            for(size_t j = i + 1; j < gMat.size && !seek; j++)
                if (gMat.arr[j][i] != number(0)) {
                    seek = true;
                    row = j;
                } else if (gMat.arr[i][j] != number(0)) {
                    seek = true;
                    col = j;
                } else if (gMat.arr[j][j] != number(0)) {
                    seek = true;
                    row = j;
                    col = j;
                }

            if (row == i && col == i)
                break;

            if (col != i)
                for(size_t k = 0; k < gMat.size; k++)
                    gMat.arr[k][col] *= number(-1);

            if (row != i)
                for(size_t k = 0; k < gMat.size; k++)
                    gMat.arr[row][k] *= number(-1);

            gMat.swapRows(i, row);
            gMat.swapCols(i, col);
        }

        for(size_t j = i + 1; j < gMat.size; j++) {
            number coef = gMat.arr[j][i] / gMat.arr[i][i];
            for (size_t k = i; k < gMat.size; k++)
                gMat.arr[j][k] -= coef * gMat.arr[i][k];
        }

        for(size_t j = i + 1; j < gMat.size; j++)
            gMat.arr[i][j] = 0;
    }

    return gMat;
}

ostream& operator<< (ostream& os, TMatrix& matrix) {
    for (size_t i = 0; i < matrix.size; i++) {
        for (size_t j = 0; j < matrix.size; j++) {
            os << matrix.arr[i][j] << "\t";
        }
        os << endl;
    }

    return os;
}
