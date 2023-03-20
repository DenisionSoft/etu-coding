#ifndef TINTERFACE_H
#define TINTERFACE_H

#include "matrix.h"

#include <QWidget>
#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QInputDialog>
#include <QSignalMapper>
#include <QVBoxLayout>

class TInterface : public QWidget
{
    Q_OBJECT

    QPushButton *SetMatrixButton;
    QPushButton *GetDeterminantButton;
    QPushButton *GetTransposedMatrixButton;
    QPushButton *GetRankButton;
    QPushButton *DisplayMatrixButton;
    QPushButton *ExitButton;

    matrix mat;

public:
    TInterface(QWidget *parent = nullptr);
    ~TInterface();

public slots:
    void SetMatrixButtonClicked();
    void GetDeterminantButtonClicked();
    void GetTransposedMatrixButtonClicked();
    void GetRankButtonClicked();
    void DisplayMatrixButtonClicked();
    void ExitButtonClicked();
};
#endif // TINTERFACE_H
