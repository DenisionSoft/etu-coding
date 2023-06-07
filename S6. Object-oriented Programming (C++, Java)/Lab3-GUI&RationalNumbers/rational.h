#ifndef RATIONAL_H
#define RATIONAL_H

#include <iostream>
#include <QString>

class rational
{
    int a, b;
public:
    rational();
    rational(const int&);
    rational(const int&, const int&);
    
    rational operator+ (rational);
    rational operator- (rational);
    rational operator* (rational);

    friend rational operator* (double, rational);
    rational operator/ (rational);
    bool operator== (rational);

    friend std::ostream& operator<<(std::ostream&, rational);
    friend std::istream& operator>>(std::istream&, rational&);

    friend QString& operator<<(QString&, rational);

    int gcd(int, int);

    int getNumerator();
    int getDenominator();

    void simplify();

};

#endif // RATIONAL_H
