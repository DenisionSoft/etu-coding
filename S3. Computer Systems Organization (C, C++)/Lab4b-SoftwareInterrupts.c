#include <conio.h> 
#include <dos.h> 

void main(){
	union REGS regs;
	regs.h.dl = '*';
	
	textbackground(BLACK);
	clrscr();
	cprintf("Window is 10, 12, 70, 23. Use arrows and ESC to exit.");
	window(10, 12, 70, 23);
	textbackground(GREEN);
	clrscr();
	textcolor(BLACK);
        _setcursortype (_NOCURSOR);
	int x = 1, y = 1;
	do
	{
		regs.h.ah = 7;
		int86(0x21, &regs, &regs);
		switch (regs.h.al)
		{
		    case 72: if (y > 1) { y--; } break;
		    case 75: if (x > 1) { x--; } break;
		    case 77: if (x <= 60) { x++; } break;
		    case 80: if (y <= 11) { y++; } break;
		    case 27: return;
		}
		clrscr();
		gotoxy(x, y);
		regs.h.ah = 2;
		int86(0x21, &regs, &regs);
	} while (regs.h.al != 32);
}