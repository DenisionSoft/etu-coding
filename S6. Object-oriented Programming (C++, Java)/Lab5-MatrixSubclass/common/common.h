#ifndef COMMON_H
#define COMMON_H

#include <QString>

enum messages
{
   GET_DETERMINANT_REQUEST = 1,
   GET_DETERMINANT_ANSWER,
   GET_TRANSPONDED_REQUEST,
   GET_TRANSPONDED_ANSWER,
   GET_RANK_REQUEST,
   GET_RANK_ANSWER,

};

extern const QChar separator;
QString& operator<< (QString&,const QString&);

#endif // COMMON_H
