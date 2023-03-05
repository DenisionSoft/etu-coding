#ifndef COMPLEX_H
#define COMPLEX_H

#include <iostream>

class complex
{
    double re, im;
public:
    complex();
    complex(const int&);

    complex operator+ (complex);
    complex operator- (); // unary minus
    complex operator- (complex); // binary minus
    complex operator* (complex);
    friend complex operator* (double, complex);
    complex operator/ (complex);
    bool operator== (complex);

    friend std::ostream& operator<<(std::ostream&, complex);
    friend std::istream& operator>>(std::istream&, complex&);
};

#endif // COMPLEX_H
