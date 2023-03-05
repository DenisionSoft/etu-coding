#define _USE_MATH_DEFINES

#include <dos.h>
#include <conio.h>
#include <stdio.h>
#include <stdlib.h>
#include <graphics.h>
#include <math.h>
#include <string.h>

int main(void){
    int x, y;
    int x_centered, y_centered;
    int x_edge, y_edge;
    float y_max = 0;
    float x_max = 0;
    int x_fetched;
    int y_fetched;
    float y_graph;
    float y_global;
    char buff1[32];
    char buff2[32];
    int point;
    int check = 0;
    x = 0;
    y = 0;

    int graph_driver;     //используемый драйвер
    int graph_mode;       //графический режим видеоадаптера
    int graph_error_code; //внутренний код ошибки
                          //Определение типа видеоадаптера, загрузка подходящего .BGI-драйвера и установка максимального режима
    graph_driver = DETECT;
    initgraph(&graph_driver, &graph_mode, "..\\BGI");
    //Определение кода ошибки при выполнении инициализации
    graph_error_code = graphresult();
    if (graph_error_code != grOk){ //всегда следует проверять наличие ошибки
        printf("%s", grapherrormsg(graph_error_code));
        return 255;
    }

    x_edge = getmaxx();
    y_edge = getmaxy();
    x_centered = x_edge / 2;
    y_centered = y_edge / 2;
    x_centered = x_centered/4-70;

    setcolor(15); //оси
    settextstyle(0, 0, 1);
    setlinestyle(SOLID_LINE, 0, NORM_WIDTH);
    line(x_centered, y_centered, x_edge, y_centered); // ось х
    line(x_centered, 0, x_centered, y_edge); // ось у

	setcolor(15); //начало координат
    settextjustify(LEFT_TEXT, TOP_TEXT);
    outtextxy(x_centered + 7, y_centered + 10, "0");

    setlinestyle(SOLID_LINE, 0, NORM_WIDTH);
    settextjustify(CENTER_TEXT, TOP_TEXT);
    for (int o = 0; o <= x_edge; ++o){ // точки на х
        point = o - x_centered;
        if (point < 0) continue;
        if (point != 0 && point % 40 == 0){
            setcolor(15);
            line(o, y_centered - 5, o, y_centered + 5);
            setcolor(15);
            sprintf(buff1, "%d", point / 38);
            outtextxy(o, y_centered + 10, buff1);
        }
    }

    settextjustify(LEFT_TEXT, CENTER_TEXT);
    for (int p = 0; p <= y_edge; ++p){ // точки на у
        point = y_centered - p;
        if (point != 0 && point % 150 == 0){
            setcolor(15);
            line(x_centered - 5, p, x_centered + 5, p);
            setcolor(15);
            sprintf(buff1, "%d", point / 150);
            outtextxy(x_centered + 10, p - 4, buff1);
        }
    }

    setcolor(4); // рендер графика
    for (float n = M_PI/2 * 38; n <= 5*M_PI * 38; n++){
        y_graph = pow(sin(n / 38), 2) - pow(cos(n / 38), 3);
        y_global = y_graph * 150;
        x_fetched = x_centered + n;
        y_fetched = y_centered - y_global;

        if (!check){
            moveto(x_fetched, y_fetched);
            check = 1;
        }

        if (y_graph > y_max){
        	x_max = n / 38;
            y_max = pow(sin(n / 38), 2) - pow(cos(n / 38), 3);
        }
        lineto(x_fetched, y_fetched);
    }

    settextstyle(0, 0, 2); //вывод надписей
    setcolor(15);
    settextjustify(RIGHT_TEXT, TOP_TEXT);
    sprintf(buff1, "Maximum @ %.2f", y_max);
    outtextxy(x_edge - 10, 10, buff1);
    sprintf(buff2, "y = sin^2(x) - cos^3(x)");
    outtextxy(x_edge - 10, 30, buff2);

    getch();
    closegraph();
}
