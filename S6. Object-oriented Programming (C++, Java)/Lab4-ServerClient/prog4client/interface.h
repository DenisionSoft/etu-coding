#ifndef INTERFACE_H
#define INTERFACE_H

#include "matrix.h"

#include <QWidget>
#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QInputDialog>
#include <QSignalMapper>
#include <QVBoxLayout>
#include <QMessageBox>

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
    QString body = "3;0/1 0/1 0/1;0/1 0/1 0/1;0/1 0/1 0/1;";

public:
    TInterface(QWidget *parent = nullptr);
    ~TInterface();

public slots:
    void SetMatrixButtonClicked();
    void DisplayMatrixButtonClicked();
    void ExitButtonClicked();
    void answer(QString);

private slots:
    void GetDeterminantButtonClicked();
    void GetTransposedMatrixButtonClicked();
    void GetRankButtonClicked();

signals:
    void request(QString);

};

#endif // INTERFACE_H
