#include "rational.h"

rational::rational()
{

}

rational::rational(const int& n)
{
    a = n;
    b = 1;
}

rational::rational(const int& n, const int& d)
{
    a = n;
    b = d;
}

rational rational::operator+ (rational r)
{
    rational t;
    t.a = a*r.b + b*r.a;
    t.b = b*r.b;
    return t;
}

rational rational::operator- (rational r)
{
    rational t;
    t.a = a*r.b - b*r.a;
    t.b = b*r.b;
    return t;
}

rational rational::operator* (rational r)
{
    rational t;
    t.a = a*r.a;
    t.b = b*r.b;
    return t;
}

rational operator* (double d, rational r)
{
    rational t;
    t.a = d*r.a;
    t.b = r.b;
    return t;
}

rational rational::operator/ (rational r)
{
    rational t;
    t.a = a*r.b;
    t.b = b*r.a;
    return t;
}

bool rational::operator== (rational r)
{
    return (a*r.b == b*r.a);
}

std::ostream& operator<<(std::ostream& out, rational r)
{
    if (r.b == 1)
        out << r.a << "  ";
    else
        out << r.a << "/" << r.b;
    return out;
}

std::istream& operator>>(std::istream& in, rational& r)
{
    in >> r.a >> r.b;
    return in;
}

QString& operator<<(QString& out, rational r)
{
    if (r.b == 1)
        out += QString::number(r.a) + "  ";
    else
        out += QString::number(r.a) + "/" + QString::number(r.b);
    return out;
}

void rational::simplify()
{
    int g = gcd(a, b);
    a = a/g;
    b = b/g;
}

int rational::gcd(int a, int b)
{
    a = abs(a);
    b = abs(b);
    if (b == 0)
        return a;
    else
        return gcd(b, a%b);
}

int rational::getNumerator()
{
    return a;
}

int rational::getDenominator()
{
    return b;
}