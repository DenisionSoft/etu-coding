import random
import time
'''
Лабораторная работа #2 по АиСД, Медведев Денис, группа 0734
Код содержит вариацию insertion sort, модифицированную функцию merge из merge sort и код для timsort

Есть два варианта ввода в программу - своего массива и случайного. Параметры случайного можно ввести вручную
'''

MIN = 64 # минимальный minrun
SIZE = 10000 # размер случайного массива
NUMBERS = 10000 # максимальное случайное число

def GetMinRun(n): # вычисление minrun
    r = 0
    while n >= MIN:
        r |= n & 1
        n >>= 1
    return n + r

'''
Сортировка вставками
'''
def InsertionSort(a, fro, to):
    for i in range(fro+1, to+1):
        j = i
        while j>fro and a[j-1] > a[j]:
            a[j], a[j-1] = a[j-1], a[j]
            j -= 1

'''
Merge для Timsort
'''
def Merge(a, l, m, r):
    l1, l2 = m - l + 1, r - m
    left, right = [], []

    # приготавливаем свои половинки
    for i in range(0, l1):
        left.append(a[l + i])
    for i in range(0, l2):
        right.append(a[m + 1 + i])

    # сравниваем и мержим
    i, j, k = 0, 0, l
    while i < l1 and j < l2:
        if left[i] <= right[j]:
            a[k] = left[i]
            i += 1
        else:
            a[k] = right[j]
            j += 1
        k += 1

    # зачищаем лево
    while i < l1:
        a[k] = left[i]
        k += 1
        i += 1

    # зачищаем право
    while j < l2:
        a[k] = right[j]
        k += 1
        j += 1

'''
Непосредственно Timsort
'''
def TimSort(a):
    n = len(a)
    minrun = GetMinRun(n)

    for A in range(0, n, minrun): # сортируем раны
        Z = min(A + minrun - 1, n - 1)
        InsertionSort(a, A, Z)

    size = minrun # начинаем мержить
    while size < n:
        for left in range(0, n, 2 * size):
            mid = min(n - 1, left + size - 1)
            right = min((left + 2 * size - 1), (n - 1))
            if mid < right:
                Merge(a, left, mid, right)
        size = size*2

'''
Исполняющаяся часть кода
'''

choice = int(input("Для ввода своих данных, введите для начала 1. Для генерации случайных, введите 2: "))
if choice == 1:
    a = input().split()
else:
    SIZE = int(input("Введите размер генерируемого массива: "))
    NUMBER = int(input("Введите максимальное значение генерации: "))
    a = [random.randint(0,NUMBERS) for i in range(SIZE)]

print("Получен массив размера {0}, его содержимое: {1}".format(len(a), a))

# засекаем время
start_time = time.time()
TimSort(a)
timestop = time.time() - start_time

if timestop:
    print("Это было выполнено за {0} секунд".format(timestop))
print("Итоговый отсортированный массив:", a)
