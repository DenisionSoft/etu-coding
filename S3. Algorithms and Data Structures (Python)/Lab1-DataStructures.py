import ctypes
"""
Лабораторная работа по АиСД, Медведев Денис, группа 0734
В коде находятся динамический массив, односвязный список (Node и LinkedList), стэк и использующий его
алгоритм перевода из инфиксной в постфиксную нотацию, aka алгоритм сортировочной станции

Ввод в программу осуществляется в одну строку через пробелы, выводится полученный ввод и результат
"""

"""
Первая структура - динамический массив
Доступные функции - append(), insertAt(), pop(), removeAt()
"""
class DynamicArray(object): #динамический массив, n для колва элементов, size для текущего размера

    def __init__(self):
        self.n = 0
        self.size = 1
        self.A = self.make_array(self.size)

    def __len__(self):
        return self.n

    def __getitem__(self, i):
        if not 0 <= i < self.n:
            return IndexError('Index unavailable')

        return self.A[i]

    def append(self, item): # last in
        if self.n == self.size:
            self._resize(2 * self.size)

        self.A[self.n] = item
        self.n += 1

    def insertAt(self, item, index): # добавление по индексу
        if index < 0 or index > self.n:
            print('Index unavailable')
            return

        if self.n == self.size:
            self._resize(2 * self.size)

        for i in range(self.n - 1, index - 1, -1):
            self.A[i + 1] = self.A[i]

        self.A[index] = item
        self.n += 1

    def pop(self): # last out

        if self.n == 0:
            print('Array is empty')
            return

        self.A[self.n - 1] = 0
        self.n -= 1

    def removeAt(self, index): # удаление по индексу
        if self.n == 0:
            print('Array is empty')
            return

        if index < 0 or index >= self.n:
            return IndexError('Index unavailable')

        if index == self.n - 1:
            self.A[index] = 0
            self.n -= 1
            return

        for i in range(index, self.n - 1):
            self.A[i] = self.A[i + 1]

        self.A[self.n - 1] = 0
        self.n -= 1

    def _resize(self, new_cap):
        B = self.make_array(new_cap)  # новый массив
        for k in range(self.n):
            B[k] = self.A[k] # перенос значений
        self.A = B  # перелинк
        self.size = new_cap  # новый размер

    def make_array(self, new_cap):
        return (new_cap * ctypes.py_object)()

"""
Вторая структура - односвязный список и нода для него
Доступные функции - add_first(), add_last(), add_after(), remove(), clear()
"""
class Node:
    def __init__(self, data, place):
        self.data = data
        self.place = place
        self.next = None

    def __repr__(self):
        return self.data

class LinkedList:
    def __init__(self, nodes=None):
        self.head = None
        if nodes is not None: # для создания списка в стиле массивов
            node = Node(data=nodes.pop(0), place=0)
            self.head = node
            counter = 1
            for elem in nodes:
                node.next = Node(data=elem, place=counter)
                counter += 1
                node = node.next

    def __iter__(self):
        node = self.head
        while node is not None:
            yield node
            node = node.next

    def __repr__(self):
        node = self.head
        nodes = []
        while node is not None:
            nodes.append(node.data)
            node = node.next
        nodes.append("None")
        return " -> ".join(nodes)

    def add_first(self, node): # добавление в начало
        new_node = Node(node, 0)
        new_node.next = self.head
        self.head = new_node

        # пересчёт мест
        counter = 0
        cur_node = self.head
        while cur_node is not None:
            cur_node.place = counter
            counter += 1
            cur_node = cur_node.next

    def add_last(self, node): # добавление в конец
        atLast = self.head

        if self.head is None:
            new_node = Node(node, 0)
            self.head = new_node
            return

        while atLast.next:
            atLast = atLast.next
        new_place = atLast.place + 1
        new_node = Node(node, new_place)
        atLast.next = new_node

        # пересчёт мест
        counter = 0
        cur_node = self.head
        while cur_node is not None:
            cur_node.place = counter
            counter += 1
            cur_node = cur_node.next

    def add_after(self, place, new_data): # добавление после определенного места
        if self.head is None:
            new_node = Node(new_data, 0)
            self.head = new_node
            return

        new_place = place + 1
        new_node = Node(new_data, new_place)

        cur_node = self.head
        while cur_node.place != place:
            cur_node = cur_node.next
            if cur_node is None:
                print("Place unavailable for add_after ", place)
                return

        new_node.next = cur_node.next
        cur_node.next = new_node

        # пересчёт мест
        counter = 0
        cur_node = self.head
        while cur_node is not None:
            cur_node.place = counter
            counter += 1
            cur_node = cur_node.next

    def remove(self, place): # удаление
        if self.head is None:
            print("List is empty")
            return

        if place == 0:
            temp = self.head
            self.head = temp.next
            temp = None

        else:
            cur_node = self.head
            while cur_node is not None:
                if cur_node.place == place:
                    break
                prev = cur_node
                cur_node = cur_node.next

            if cur_node is None:
                print("Place unavailable for remove at ", place)
                return

            prev.next = cur_node.next
            cur_node = None

        # пересчёт мест
        counter = 0
        cur_node = self.head
        while cur_node is not None:
            cur_node.place = counter
            counter += 1
            cur_node = cur_node.next

    def clear(self): # очистка списка
        if self.head is None:
            print("List is empty")
            return

        while self.head is not None:
            list.remove(0)

"""
Третья структура - стэк
Доступные функции - outp(), spush(), spop(), slen()
"""
class Stack:
    def __init__(self, list=[]):
        self.list = []
        if list != []:
            while list != []:
                self.list.append(list.pop())

    def outp(self):
       return print(self.list)

    def __getitem__(self, i):
        return self.list[i]

    def spush(self, data):
        self.list.append(data)

    def spop(self):
        return self.list.pop()

    def slen(self):
        return len(self.list)

"""
И основная выполняющаяся часть программы - алгоритм сортировочной станции
"""
a = []
out = []
a = input().split()
print(a)
stack = Stack()

numbers = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
operators = ['+', '-', '*', '/','^']

ops = dict([ # приоритеты операций
    ('+', 0),
    ('-', 0),
    ('*', 1),
    ('/', 1),
    ('^', 2)
    ])

functions = ['sin', 'cos']
braces = ['(', ')']

for i in a:
    if i in numbers:
        out.append(int(i))

    if i in functions:
        stack.spush(i)

    if i in operators:
        if stack.slen() > 0:
            while (stack[stack.slen()-1] in operators) and (ops[i] <= ops[stack[stack.slen()-1]]):
                out.append(stack[stack.slen()-1])
                stack.spop()
                if stack.slen() == 0:
                    break
        stack.spush(i)

    if i == '(':
        stack.spush(i)

    if i == ')':
        while stack[stack.slen()-1] != '(':
            out.append(stack[stack.slen()-1])
            stack.spop()
        stack.spop()
        if stack.slen() > 0:
            if stack[stack.slen()-1] in functions:
                out.append(stack[stack.slen()-1])
                stack.spop()

while stack.slen() > 0:
    out.append(stack.spop())

print(out)
