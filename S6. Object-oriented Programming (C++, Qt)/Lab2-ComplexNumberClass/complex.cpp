#include "complex.h"

complex::complex()
{

}

complex::complex(const int& r)
{
    re = r;
    im = 0;
}

complex complex::operator+ (complex c)
{
    complex t;
    t.re = re + c.re;
    t.im = c.im + im;
    return t;
}

complex complex::operator- () // unary minus
{
    complex t;
    t.re = -re;
    t.im = -im;
    return t;
}

complex complex::operator- (complex c) // binary minus
{
    complex t;
    t.re = re - c.re;
    t.im = im - c.im;
    return t;
}

complex complex::operator* (complex c)
{
    complex t;
    t.re = re*c.re - im*c.im;
    t.im = re*c.im + im*c.re;
    return t;
}

/*
complex complex::operator* (double d)
{
    complex t;
    t.re = re*d;
    t.im = im*d;
    return t;
}
*/

complex operator* (double d, complex c)
{
    complex t;
    t.re = c.re*d;
    t.im = c.im*d;
    return t;
}

complex complex::operator/ (complex c)
{
    complex t;
    t.re = (re*c.re + im*c.im)/(c.re*c.re + c.im*c.im);
    t.im = (im*c.re - re*c.im)/(c.re*c.re + c.im*c.im);
    return t;
}

std::ostream& operator<<(std::ostream& os, complex c)
{
    if (c.im < 0)
        os<<"("<<c.re<<c.im<<"i)";
    else
        os<<"("<<c.re<<"+"<<c.im<<"i)";
    return os;
}

std::istream& operator>>(std::istream& is, complex& c)
{
    is>>c.re>>c.im;
    return is;
}

bool complex::operator== (complex c)
{
    return (re==c.re)&&(im==c.im);
}
