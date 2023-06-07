#include "interface.h"
#include "common.h"
#include "number.h"
#include "matrix.h"

TInterface::TInterface(QWidget *parent)
    : QWidget(parent)
{
    setWindowTitle("Работа №4");
    setFixedSize(300,250);

    SetMatrixButton = new QPushButton("Задать матрицу", this);
    SetMatrixButton->setGeometry(10,10,280,30);

    GetDeterminantButton = new QPushButton("Вычислить определитель", this);
    GetDeterminantButton->setGeometry(10,50,280,30);

    GetTransposedMatrixButton = new QPushButton("Вычислить транспонированную матрицу", this);
    GetTransposedMatrixButton->setGeometry(10,90,280,30);

    GetRankButton = new QPushButton("Вычислить ранг матрицы", this);
    GetRankButton->setGeometry(10,130,280,30);

    DisplayMatrixButton = new QPushButton("Вывести матрицу", this);
    DisplayMatrixButton->setGeometry(10,170,280,30);

    ExitButton = new QPushButton("Выход", this);
    ExitButton->setGeometry(10,210,280,30);

    connect(SetMatrixButton, SIGNAL(clicked()), this, SLOT(SetMatrixButtonClicked()));
    connect(GetDeterminantButton, SIGNAL(clicked()), this, SLOT(GetDeterminantButtonClicked()));
    connect(GetTransposedMatrixButton, SIGNAL(clicked()), this, SLOT(GetTransposedMatrixButtonClicked()));
    connect(GetRankButton, SIGNAL(clicked()), this, SLOT(GetRankButtonClicked()));
    connect(DisplayMatrixButton, SIGNAL(clicked()), this, SLOT(DisplayMatrixButtonClicked()));
    connect(ExitButton, SIGNAL(clicked()), this, SLOT(ExitButtonClicked()));
}

TInterface::~TInterface()
{
    delete SetMatrixButton;
    delete GetDeterminantButton;
    delete GetTransposedMatrixButton;
    delete GetRankButton;
    delete DisplayMatrixButton;
    delete ExitButton;
}

void TInterface::answer(QString msg)
{
    int request = msg.section(separator, 0, 0).toInt();

    switch (request)
    {   
        case GET_DETERMINANT_ANSWER: {
            QMessageBox::information(this, "Определитель", msg.section(separator, 1, 1));
            break;
        }
        case GET_RANK_ANSWER: {
            QMessageBox::information(this, "Ранг", msg.section(separator, 1, 1));
            break;
        }
        case GET_TRANSPONDED_ANSWER: {
            int tempsize = msg.section(separator, -1).toInt();
            QString s = msg.section(separator, 1, -2);

            matrix tempmat(tempsize);
            for (int i = 0; i < tempsize; i++)
            {
                QString row = s.section(separator, i, i);
                QStringList list = row.split(" ");

                for (int j = 0; j < tempsize; j++)
                {
                    QString value = list[j];
                    QStringList list2 = value.split("/");
                    int numerator = list2[0].toInt();
                    int denominator = list2[1].toInt();
                    number num(numerator, denominator);
                    num.simplify();
                    tempmat.getMatrix()[i][j] = num;
                }
            }

            number** temp = tempmat.getMatrix();
            number **matrix = mat.getMatrix();

            for(int i = 0; i < tempsize; i++)
            {
                for(int j = 0; j < tempsize; j++)
                {
                    matrix[i][j] = temp[i][j];
                }
            }

            break;
        }
    }

}

void TInterface::GetDeterminantButtonClicked()
{
    QString msg;
    msg = body;
    msg += QString().setNum(GET_DETERMINANT_REQUEST);
    emit request(msg);
}

void TInterface::GetTransposedMatrixButtonClicked()
{
    QString msg;
    msg = body;
    msg += QString().setNum(GET_TRANSPONDED_REQUEST);
    emit request(msg);
}

void TInterface::GetRankButtonClicked()
{
    QString msg;
    msg = body;
    msg += QString().setNum(GET_RANK_REQUEST);
    emit request(msg);
}

void TInterface::SetMatrixButtonClicked()
{

    QDialog *window = new QDialog();
    window->setWindowTitle("Редактор размера");
    window->setFixedSize(300,250);

    QVBoxLayout *layout = new QVBoxLayout(window);

    QLabel *label = new QLabel();
    label->setText("Введите размер матрицы, \nнажмите Задать и закройте окно");
    layout->addWidget(label);

    QLineEdit *lineEdit = new QLineEdit();
    layout->addWidget(lineEdit);

    QPushButton *button = new QPushButton("Задать");
    layout->addWidget(button);

    QSignalMapper *mapper = new QSignalMapper(window);

    connect(button, SIGNAL(clicked()), mapper, SLOT(map()));
    mapper->setMapping(button, 0);

    window->exec();

    QString sizestr = lineEdit->text();
    //body = sizestr;
    body = "";
    body << sizestr;

    int size = sizestr.toInt();

    mat = matrix(size);

    QDialog *window2 = new QDialog();
    window2->setWindowTitle("Редактор матрицы");
    window2->setFixedSize(1000,700);

    QVBoxLayout *layout2 = new QVBoxLayout(window2);

    QLabel *label2 = new QLabel();
    label2->setText("Введите значения матрицы через пробел, \nнажмите Задать и закройте окно");
    layout2->addWidget(label2);

    for (int i = 0; i < size; i++)
    {
        QLabel *label = new QLabel();
        label->setText("Введите " + QString::number(i+1) + " строку матрицы:");
        layout2->addWidget(label);

        QLineEdit *lineEdit = new QLineEdit();
        layout2->addWidget(lineEdit);

        connect(lineEdit, SIGNAL(editingFinished()), mapper, SLOT(map()));
        mapper->setMapping(lineEdit, i+1);
    }

    QPushButton *button2 = new QPushButton("Задать");
    layout2->addWidget(button2);

    connect(button2, SIGNAL(clicked()), mapper, SLOT(map()));
    mapper->setMapping(button2, size+1);

    window2->exec();

    for (int i = 0; i < size; i++)
    {
        QLineEdit *lineEdit = qobject_cast<QLineEdit*>(mapper->mapping(i+1));
        QString row = lineEdit->text();
        body << row;
        QStringList list = row.split(" ");
        for (int j = 0; j < size; j++)
        {
            QString value = list[j];
            QStringList list2 = value.split("/");
            int numerator = list2[0].toInt();
            int denominator = list2[1].toInt();
            number num(numerator, denominator);
            num.simplify();
            mat.getMatrix()[i][j] = num;
        }
    }

}

void TInterface::DisplayMatrixButtonClicked()
{
    QWidget *window = new QWidget;
    window->setWindowTitle("Матрица");
    window->setFixedSize(300,250);

    QLabel *label = new QLabel(window);
    label->setGeometry(10,10,280,30);
    QString size = QString::number(mat.getSize());
    label->setText("Размер матрицы: " + size);

    for (int i = 0; i < mat.getSize(); i++)
    {
        QLabel *label = new QLabel(window);
        label->setGeometry(10,50 + i*30,280,30);
        QString row("");
        row << mat.getMatrix()[i][0];
        for (int j = 1; j < mat.getSize(); j++)
        {
            row += " ";
            row << mat.getMatrix()[i][j];
        }
        label->setText(row);
    }

    QPushButton *ExitButton = new QPushButton("Выход", window);
    ExitButton->setGeometry(10,210,280,30);

    connect(ExitButton, SIGNAL(clicked()), window, SLOT(close()));

    window->show();
}

void TInterface::ExitButtonClicked()
{
    close();
}
