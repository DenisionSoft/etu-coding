#include "application.h"
#include "matrix.h"
#include "common.h"

TApplication::TApplication(int argc, char *argv[])
            : QCoreApplication(argc,argv)
{
    TCommParams pars = { QHostAddress("127.0.0.1"), 10000,
                         QHostAddress("127.0.0.1"), 10001};
    comm = new TCommunicator(pars, this);

    connect(comm,SIGNAL(recieved(QByteArray)),this,SLOT(recieve(QByteArray)));
}

void TApplication::recieve(QByteArray msg)
{
    QString s(msg);
    int size = s.section(separator, 0, 0).toInt();
    int request = s.section(separator, -1).toInt();
    s = s.section(separator, 1, -2);

    matrix mat(size);

    for (int i = 0; i < size; i++)
    {
        QString row = s.section(separator, i, i);
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

    QString answer;

    switch (request)
    {
        case GET_DETERMINANT_REQUEST: {
            answer << QString().setNum(GET_DETERMINANT_ANSWER);
            number det = mat.determinant();
            det.simplify();
            answer << det;
            break;
        }
        case GET_TRANSPONDED_REQUEST: {
            answer << QString().setNum(GET_TRANSPONDED_ANSWER);

            number** trans = mat.matrixTranspose();
            number **matrix = mat.getMatrix();

            for(int i = 0; i < size; i++)
            {
                for(int j = 0; j < size; j++)
                {
                    matrix[i][j] = trans[i][j];
                }
            }

            for (int i = 0; i < size; i++)
            {
                QString row("");
                row += mat.getMatrix()[i][0];
                for (int j = 1; j < size; j++)
                {
                    row += " ";
                    row += mat.getMatrix()[i][j];
                }
                answer << row;
            }
            answer += QString().setNum(size);
            break;
        }
        case GET_RANK_REQUEST: {
            answer << QString().setNum(GET_RANK_ANSWER);
            QString rank = QString::number(mat.calculateRank());
            answer += rank;
            break;
        }
        default: return;

    }

    answer = answer.toUtf8();
    comm->send(QByteArray().append(answer.toLocal8Bit()));
}
