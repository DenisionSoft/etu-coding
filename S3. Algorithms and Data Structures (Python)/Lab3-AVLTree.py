'''
Лабораторная работа #3 по АиСД, Медведев Денис, группа 0374
Код содержит АВЛ сбалансированное дерево, функции поиска, вставки и удаления
Также доступны итеративные функции обхода КЛП (прямой/preOrder), ЛКП (центрированный/inOrder), ЛПК (обратный/postOrder)
и обхода в ширину.
'''

# Элемент дерева с данными, левым и правым ребенком и значением высоты
class node(object):
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None
        self.height = 1

'''
Непосредственно АВЛ-дерево. 
Содержит юзабельные find(), insert(), delete() и внутренние rotate для балансировки.
'''
class AVLtree(object):
    def find(self, root, key):

        if root is None:
            print("это пройденный путь и", key, "не был найден.")
            return

        print(root.val, end=' ')

        if key == root.val:
            print("это пройденный путь и", key, "был найден!")
            return

        elif key < root.val:
            self.find(root.left, key)

        elif key > root.val:
            self.find(root.right, key)


    def insert(self, root, key):

        if not root: # обычная вставка в бинарное дерево
            return node(key)
        elif key < root.val:
            root.left = self.insert(root.left, key)
        else:
            root.right = self.insert(root.right, key)

        root.height = max(self.tree_height(root.left), self.tree_height(root.right))+1 # обновляем высоту

        # балансировка
        factorBalance = self.tree_balance(root)

        if factorBalance > 1 and key < root.left.val: # случай LL
            return self.rotate_right(root)

        if factorBalance < -1 and key > root.right.val:  # случай RR
            return self.rotate_left(root)

        if factorBalance > 1 and key > root.left.val:  # случай LR
            root.left = self.rotate_left(root.left)
            return self.rotate_right(root)

        if factorBalance < -1 and key < root.right.val:  # случай RL
            root.right = self.rotate_right(root.right)
            return self.rotate_left(root)

        return root


    def delete(self, root, key):

        if not root: # обычное удаление в бинарном дереве
            return root

        elif key < root.val:
            root.left = self.delete(root.left, key)

        elif key > root.val:
            root.right = self.delete(root.right, key)

        else:
            if root.left is None:
                temp = root.right
                root = None
                return temp

            elif root.right is None:
                temp = root.left
                root = None
                return temp

            temp = self.tree_min(root.right)
            root.val = temp.val
            root.right = self.delete(root.right,
                                     temp.val)

        if root is None:
            return root

        root.height = max(self.tree_height(root.left), self.tree_height(root.right))+1 # обновляем высоту

        # балансировка
        factorBalance = self.tree_balance(root)

        if factorBalance > 1 and self.tree_balance(root.left) >= 0: # случай LL
            return self.rotate_right(root)

        if factorBalance < -1 and self.tree_balance(root.right) <= 0: # случай RR
            return self.rotate_left(root)

        if factorBalance > 1 and self.tree_balance(root.left) < 0: # случай LR
            root.left = self.rotate_left(root.left)
            return self.rotate_right(root)

        if factorBalance < -1 and self.tree_balance(root.right) > 0: # случай RL
            root.right = self.rotate_right(root.right)
            return self.rotate_left(root)

        return root

    def rotate_left(self, z):
        y = z.right
        yl = y.left
        y.left = z
        z.right = yl
        z.height = max(self.tree_height(z.left), self.tree_height(z.right))+1
        y.height = max(self.tree_height(y.left), self.tree_height(y.right))+1
        return y

    def rotate_right(self, z):
        y = z.left
        yr = y.right
        y.right = z
        z.left = yr
        z.height = max(self.tree_height(z.left), self.tree_height(z.right))+1
        y.height = max(self.tree_height(y.left), self.tree_height(y.right))+1
        return y

    def tree_height(self, root):
        if not root: return 0
        return root.height

    def tree_balance(self, root):
        if not root: return 0
        return self.tree_height(root.left) - self.tree_height(root.right)

    def tree_min(self, root):
        if root is None or root.left is None: return root
        return self.tree_min(root.left)

'''
ЛКП
Центрированный итеративный обход (Inorder)
'''

def inOrder(root):
    current = root
    result = []

    while True:
        if current is not None:
            result.append(current)
            current = current.left
        elif (result):
            current = result.pop()
            print(current.val, end=" ")
            current = current.right
        else:
            break

'''
ЛПК
Обратный итеративный обход (Postorder)
'''

def postOrder(root):
    result = []

    while (True):
        while (root != None):
            result.append(root)
            result.append(root)
            root = root.left
        if (len(result) == 0):
            return
        root = result.pop()
        if (len(result) > 0 and result[-1] == root):
            root = root.right
        else:
            print(root.val, end=" ")
            root = None

'''
КЛП
Прямой итеративный обход (Preorder)
'''

def preOrder(root):
    if root is None:
        return

    result = []
    result.append(root)

    while (len(result) > 0):
        node = result.pop()
        print(node.val, end=" ")
        if node.right is not None:
            result.append(node.right)
        if node.left is not None:
            result.append(node.left)

'''
Обход в ширину
'''
def breadthOrder(root):
    if root is None:
        return

    result = []
    result.append(root)

    while (len(result) > 0):
        print(result[0].val, end=" ")
        node = result.pop(0)
        if node.left is not None:
            result.append(node.left)
        if node.right is not None:
            result.append(node.right)

'''
Исполняющаяся часть программы
'''
Tree = AVLtree() # создаём дерево
root = None

inputList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, -1, -2] # данная последовательность
for x in inputList: root = Tree.insert(root, x) # наполняем последовательностью

Tree.find(root, 12) # поиск элемента не в дереве