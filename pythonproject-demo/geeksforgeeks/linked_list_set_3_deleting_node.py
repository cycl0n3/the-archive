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

    def deleteNode(self, key):
        temp = self.head

        if temp:
            if temp.data == key:
                self.head = temp.next
                return

        prev = None

        while temp:
            if temp.data == key:
                break

            prev = temp
            temp = temp.next

        if not temp:
            return

        prev.next = temp.next


if __name__ == '__main__':
    llist = LinkedList()

    llist.push(7)
    llist.push(1)
    llist.push(3)
    llist.push(2)

    print("Created Linked List: ")
    llist.printList()
    llist.deleteNode(1)
    print("Linked List after Deletion of 1:")
    llist.printList()
