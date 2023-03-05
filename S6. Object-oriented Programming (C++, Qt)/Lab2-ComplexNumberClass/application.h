#ifndef APPLICATION_H
#define APPLICATION_H

#include "matrix.h"
class application
{
public:
    void Menu();
    void MenuRender(int pos);
    void SetMatrix(matrix mat);
    void PrintMatrix(matrix mat);
    void CopyMatrix(number** from, matrix to);
};

#endif // APPLICATION_H
