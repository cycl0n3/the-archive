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

    def getCount(self):
        temp = self.head
        count = 0

        while temp:
            count += 1
            temp = temp.next

        return count

    def getCountRec(self, node):
        if not node:
            return 0
        else:
            return 1 + self.getCountRec(node.next)


if __name__ == '__main__':
    llist = LinkedList()

    llist.push(1)
    llist.push(3)
    llist.push(1)
    llist.push(2)
    llist.push(1)

    print('The count of nodes is: ', llist.getCount())
    print('The count of nodes is: ', llist.getCountRec(llist.head))
