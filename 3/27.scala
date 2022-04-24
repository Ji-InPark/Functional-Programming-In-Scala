/**
 * 트리의 뿌리(root)에서 임의의 잎으로의 가장 긴 경로의 길이를 돌려주는 함수 depth를 작성하라
 */

def depth[A](tree: Tree[A]): Int = tree match {
  case Leaf(_) => 0
  case Branch(left, right) => (depth(left) max depth(right)) + 1
}
