/**
 * 트리의 노드, 즉 잎(leaf)과 가지(branch)의 개수를 세는 함수 size를 작성하라.
 */

def size[A](tree: Tree[A]): Int = tree match {
  case Leaf(_) => 1
  case Branch(left, right) => size(left) + size(right) + 1
}
