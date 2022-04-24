/**
 * List에 대한 함수 map과 비슷하게 트리의 각 요소를 주어진 함수로 수정하는 함수 map을 작성하라.
 */

def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = tree match {
  case Leaf(v) => Leaf(f(v))
  case Branch(left, right) => Branch(map(left)(f), map(right)(f))
}
