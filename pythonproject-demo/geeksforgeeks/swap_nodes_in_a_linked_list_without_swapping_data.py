class Node(object):
    def __init__(self, data):
        self.data = data
        self.next = None


class LinkedList(object):
    def __init__(self):
        self.head = None

    def printList(self):
        temp = self.head

        while temp:
            print(temp.data)
            temp = temp.next

    def push(self, new_data):
        new_node = Node(new_data)
        new_node.next = self.head

        self.head = new_node

    def swapNodes(self, x, y):
        if x == y:
            return

        prevX = None
        currX = self.head
        while currX is not None and currX.data != x:
            prevX = currX
            currX = currX.next

        prevY = None
        currY = self.head
        while currY is not None and currY.data != y:
            prevY = currY
            currY = currY.next

        if currX is None or currY is None:
            return

        if prevX is not None:
            prevX.next = currY
        else:
            self.head = currY

        if prevY is not None:
            prevY.next = currX
        else:
            self.head = currX

        temp = currX.next
        currX.next = currY.next
        currY.next = temp


if __name__ == '__main__':
    llist = LinkedList()

    llist.push(7)
    llist.push(6)
    llist.push(5)
    llist.push(4)
    llist.push(3)
    llist.push(2)
    llist.push(1)
    print("Linked list before calling swapNodes() ")
    llist.printList()
    llist.swapNodes(4, 3)
    print("Linked list after calling swapNodes() ")
    llist.printList()
