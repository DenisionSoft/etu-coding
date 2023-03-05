#include <iostream>
#include <conio.h>
using namespace std;

void Menu(int pos); //для рендера меню

bool isFlag = false; //для вывода верного представления
bool update = true; //флаг на обновление экрана

int main() {
	setlocale(0, "");

	union { //(не)хакерский юнион для будущего (почти) тайп-паннинга
		float unf_float;
		uint32_t unf_unsign;
	} unf;
	unf.unf_float = 0;

	union { //для инта
		int32_t uni_sing;
		uint32_t uni_unsign;
	} uni;
	uni.uni_unsign = 0;

	int represent[32] = { 0 }; //массив для хранения результата

	int a = 0, pos = 0; //для работы меню

	while (1) {

		if (update) { //автоматическое обновление экрана, aka костыль(ТМ)
			update = false;
			a = 111;
		}

		else a = _getch();
		system("cls");
		if (a == 224) continue;
		switch (a) {
		case 72:
			if (pos > 1) pos--;
			else pos += 3;
			break;
		case 80:
			if (pos < 4) pos++;
			else pos -= 3;
			break;
		case 49:
			pos = 1;
			break;
		case 50:
			pos = 2;
			break;
		case 51:
			pos = 3;
			break;
		case 52:
			pos = 4;
			break;
		case 27:
			return 0;
		}
		Menu(pos);

		cout << endl << endl;
		if (isFlag) { //если последнее введённое - дробное, рендерим массив с разделением
			cout << unf.unf_float << " | "; //выводим дробный тип из юниона
			for (int i = 0; i < 32; ++i) { //проходимся по массиву с результатом
				if (i == 1 || i == 9) cout << " "; //после 1 бита знака и 8 битов порядка разделяем
				cout << represent[i];
			}
		}
		else { //в ином случае - рендерим как есть
			cout << uni.uni_sing << " | "; //выводим целый тип из юниона
			for (int i = 0; i < 32; ++i) cout << represent[i]; //проходимся по массиву с результатом
		}
		cout << endl << endl;

		if (a == 13) //если нажата Enter
			if (pos == 1) { //для ввода дробного числа
				cout << "Введите дробное число: ";
				cin >> unf.unf_float;
				auto temp = unf.unf_unsign; //передаём значение
				for (int i = 31; i >= 0; --i) { //проходим по всему числу
					represent[i] = temp % 2; //и записываем 0 или 1, используя четность
					temp = temp >> 1; //сдвигаем число для записи следующего бита
				}
				isFlag = true; //ставим флаг на дробное число
				update = true; //обновляем экран
			}
			else if (pos == 2) { //для целого аналогично
				cout << "Введите целое число: ";
				cin >> uni.uni_sing;
				auto temp = uni.uni_unsign; //передаём значение
				for (int i = 31; i >= 0; --i) { //проходим по всему числу
					represent[i] = temp % 2; //и записываем 0 или 1, используя четность
					temp = temp >> 1; //сдвигаем число для записи следующего бита
				}
				isFlag = false;
				update = true;
			}
			else if (pos == 3) { //изменение бита
				int bit;
				cout << "Введите номер изменяемого бита (с 0): ";
				cin >> bit;
				uint32_t changed = 1 << bit; //создаём новый инт с битом в нужном месте путём сдвига вправо
				if (isFlag) {
					unf.unf_unsign = unf.unf_unsign ^ changed; //используя XOR заменяем в числе именно тот бит, что записан ранее
					auto temp = unf.unf_unsign; //и пересчитвыаем массив с результатом, используя алгоритм выше
					for (int i = 31; i >= 0; --i) {
						represent[i] = temp % 2;
						temp = temp >> 1;
					}
				}
				else {
					uni.uni_unsign = uni.uni_unsign ^ changed; //аналогично для инт
					auto temp = uni.uni_unsign;
					for (int i = 31; i >= 0; --i) {
						represent[i] = temp % 2;
						temp = temp >> 1;
					}
				}
				update = true;
			}
			else if (pos == 4) { return 0; } //выход из программы
	}
	return 0;
}

void Menu(int pos) //для контента меню
{
	printf("Используйте стрелки вверх и вниз и цифры от 1 до 4 для навигации. Нажмите ESC для выхода.");
	printf("\n1) Ввести дробное число ");
	if (pos == 1) printf("<");
	printf("\n2) Ввести целое число ");
	if (pos == 2) printf("<");
	printf("\n3) Изменить бит ");
	if (pos == 3) printf("<");
	printf("\n4) Выйти из программы ");
	if (pos == 4) printf("<");
}
