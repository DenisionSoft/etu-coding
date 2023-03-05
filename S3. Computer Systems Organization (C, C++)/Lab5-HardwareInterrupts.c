#include <dos.h>
#include <conio.h>

int press = 0;
 
void interrupt(far *prev)(...); //переменная, предназначенная для хранения вектора прерываний

void interrupt far my_kbrd_int(...){ //функция обработчика прерывания
	clrscr();
	++press; //iterate scan codes
	cprintf("ESC to exit. Scan codes recieved: %d", press);
	_chain_intr(prev); //chains from one interrupt handler to another
}

int main(){
	cprintf("Press any button on keyboard...");
	prev = getvect(0x09); //IRQ1 - KEYBOARD DATA READY
	setvect(0x09, my_kbrd_int);
	while (getch()!=27) {}
	setvect(0x09, prev);
}