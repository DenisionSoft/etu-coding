class Node:
    def __init__(self, name=""):
        self.name = name
        self.edges = []
        self.visited = False

    def nodeName(self):
        return self.name

    def nodeAddEdge(self, edge=None):
        self.edges.append(edge)

    def nodeEdges(self):
        return self.edges

    def nodeVisited(self, b=True):
        self.visited = b

    def isNodeVisited(self):
        return self.visited

class Edge:
    def __init__(self, fro, to, weight=1):
        self.fro = fro
        self.to = to
        self.weight = weight

    def edgeFrom(self):
        return self.fro

    def edgeTo(self):
        return self.to

    def edgeWeight(self):
        return self.weight

class Graph:
    def __init__(self):
        self.nodes = []

    def newNode(self, vertex=None):
        self.nodes.append(vertex)

    def kruskalAlgorythm(self):
        list = []

        for node in self.nodes:
            for edge in node.nodeEdges():
                list.append(edge)

        while len(list) > 0:
            m = list[0]
            for edge in list:
                if edge.edgeWeight() < m.edgeWeight():
                    m = edge
            list.remove(m)
            if not m.edgeFrom().isNodeVisited() or not m.edgeTo().isNodeVisited():
                print(m.edgeFrom().nodeName() + " - " + m.edgeTo().nodeName())
                m.edgeFrom().nodeVisited()
                m.edgeTo().nodeVisited()


    def breadthSearch(self, node):
        str = ""
        queue = []

        node.nodeVisited(True)
        queue.insert(0, node)

        while len(queue) > 0:
            n = queue.pop()
            str += n.nodeName() + " | "
            for e in n.nodeEdges():
                if not e.edgeTo().isNodeVisited():
                    queue.insert(0, e.edgeTo())
                    e.edgeTo().nodeVisited()

        print(str)

    def depthSearch(self, node):
        str = ""
        stack = []

        node.nodeVisited(True)
        stack.append(node)

        while len(stack) > 0:
            n = stack.pop()
            str += n.nodeName() + " | "
            for e in n.nodeEdges():
                if not e.edgeTo().isNodeVisited():
                    stack.append(e.edgeTo())
                    e.edgeTo().nodeVisited()

        print(str)

gr = Graph()

p = Node("P")
q = Node("Q")
r = Node("R")
s = Node("S")

pq = Edge(p, q, 10)
p.nodeAddEdge(pq)

qr = Edge(q, r, 3)
q.nodeAddEdge(qr)

ps = Edge(p, s, 5)
p.nodeAddEdge(ps)

qs = Edge(q, s, 1)
q.nodeAddEdge(qs)


gr.newNode(p)
gr.newNode(q)

gr.kruskalAlgorythm()
#gr.breadthSearch(p)
#gr.depthSearch(p)

