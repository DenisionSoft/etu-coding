#include "application.h"
#include "matrix.h"
#include <iostream>

using namespace std;

TApplication::TApplication() {

}

int TApplication::exec() {
    int ch = 1;
    TMatrix matrix(3);
    size_t size;
    number** arr;

    while(ch) {
        ch = menu();
        switch (ch) {
        case 1:
            cout << "Size of square matrix: ";
            cin >> size;
            cout << endl;

            cout << "Enter matrix elements: " << endl;

            arr = new number*[size];
            for (size_t i = 0; i < size; i++) {
                cout << "Enter " << i+1 << " line: ";
                arr[i] = new number[size];
                for (size_t j = 0; j < size; j++) {
                    cin >> arr[i][j];
                }
            }

            cout << endl;

            matrix = TMatrix(size, arr);
            for(size_t i = 0; i < size; i++)
                delete[] arr[i];
            delete[] arr;
            break;

        case 2:
            cout << "Determinant = " << matrix.determinant() << endl;
            cout << endl;
            break;

        case 3:
        {
            TMatrix transMat(matrix.transpose());
            cout << transMat << endl;
            break;
        }

        case 4:
            cout << "Rank = " << matrix.rank() << endl;
            cout << endl;
            break;

        case 5:
            cout << matrix << endl;
            break;

        default:
            break;
        }
    }

    return 0;
}

int TApplication::menu() {
    int ch;

    cout << "1 - Initialise matrix" << endl;
    cout << "2 - Determinant" << endl;
    cout << "3 - Transpose" << endl;
    cout << "4 - Rank" << endl;
    cout << "5 - Print matrix" << endl;
    cout << "0 - Exit" << endl;
    cout << "> ";
    cin >> ch;

    return ch;
}
