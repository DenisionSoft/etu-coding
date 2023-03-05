#include <dos.h>
#include <conio.h>
#include <stdio.h>
#include <stdlib.h>


char* reverseBytes(char* bytes, int size);
void extractBytes(char* bytes, int size);
void extractBits(char curByte);

void main(){
float input;
char* bytes;
int background, text, attr;
char* gamma[16]={"black",
				"blue",
				"green",
				"cyan",
				"red",
				"magenta",
				"brown",
				"lightgray",
				"darkgray",
				"lightblue",
				"lightgreen",
				"lightcyan",
				"lightred",
				"lightmagenta",
				"yellow",
				"white"};


 window(3, 12, 77, 23);

 printf("Number in memory with scrolling by Medvedev Denis, 0374.\n");
 gotoxy(1, 2);
 printf("Enter float number: ");
 scanf("%f",&input);
 bytes = (char*)&input;

 for (background = 0; background < 8; background++){

     textbackground(background);

     for (text = 0; text < 16; text++){
     	if (text == background) {continue;}
		textcolor(text);
		insline();
		gotoxy(1,1);
		extractBytes(reverseBytes(bytes, sizeof(input)), sizeof(input));
		printf("| Back %s, text %s \n", gamma[background], gamma[text]);
	    delay(300);
		gotoxy(1, 1);
     }
 }
 getch();
}


void scrolling(int direction, char l_row, char l_col, char r_row, char r_col, char attr){
    union REGS reg;
    if (direction){
        reg.h.al = 1;
        reg.h.ah = direction;
    }
    else{
        reg.h.al = 0;
        reg.h.ah = 6;
    }
    reg.h.ch = l_row;
    reg.h.cl = l_col;
    reg.h.dh = r_row;
    reg.h.dl = r_col;
    reg.h.bh = attr;
    int86(0x10, &reg, &reg);
}

char* reverseBytes(char* bytes, int size){
    char bytesret[1000];
    int i, j;
    for (i = size - 1, j = 0; i >= 0; i--, j++)
        bytesret[j] = bytes[i];
    return bytesret;
}

void extractBytes(char* bytes, int size){
    int i;
    char current;
    for (i = 0; i < size; i++){
        current = bytes[i];
        extractBits(current);
	}
}

void extractBits(char curByte){
    int i, currentBit;
    for (i = 7; i > -1; i--){
        currentBit = (curByte >> i) & 1;
        printf("%d", currentBit);
    }
    printf(" ");
}
