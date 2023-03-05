#include <conio.h> 
#include <dos.h> 
#include <stdio.h>

void main(){
	int ch = 0;
	textbackground(BLACK);
	clrscr();
	cprintf("Window is 10, 12, 70, 23. Use arrows and ESC to exit.");
	window(10, 12, 70, 23);
	textbackground(GREEN);
	clrscr();
	textcolor(BLACK);
    _setcursortype (_NOCURSOR);
	int x = 1, y = 1;
	ch = getch();
	while (ch != 27){
		switch (ch){
		    case 72: if (y > 1) { y--; } break;
		    case 75: if (x > 1) { x--; } break;
		    case 77: if (x <= 60) { x++; } break;
		    case 80: if (y <= 11) { y++; } break;
		    case 27: return;
		}
		clrscr();
		gotoxy(x, y);
		putch('*');
		ch = getch();
	}
}
