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

    def deleteNode(self, position):
        if self.head is None:
            return

        temp = self.head

        if position == 0:
            self.head = temp.next
            return

        for i in range(position - 1):
            temp = temp.next

            if temp is None:
                break

        if temp is None:
            return

        if temp.next is None:
            return

        nxt = temp.next.next
        temp.next = nxt


if __name__ == '__main__':
    llist = LinkedList()

    llist.push(7)
    llist.push(1)
    llist.push(3)
    llist.push(2)
    llist.push(8)

    print("Created Linked List: ")
    llist.printList()
    llist.deleteNode(2)

    print("Linked List after Deletion at position 2: ")
    llist.printList()
