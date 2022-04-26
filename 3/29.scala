/**
 * size와 maximum, depth, map의 유사성을 요약해서 일반화한 새 함수 fold를 작성하라.
 * 그런 다음 그 함수들을 새 fold를 이용해서 다시 구현해라.
 * 이 fold 함수와 List에 대한 왼쪽, 오른쪽 fold 사이의 유사성을 찾아낼 수 있는가?
 */

def fold[A, B](tree: Tree[A])(f: (A) => B, g: (B, B) => B): B = tree match {
  case Leaf(v) => f(v)
  case Branch(left, right) => g(fold(left)(f, g), fold(right)(f, g))
}

def size[A](tree: Tree[A]) = fold[A, Int](tree)(_ => 1, _ + _)

def maximum(tree: Tree[Int]) = fold[Int, Int](tree)((a) => a, _ max _)

def depth[A](tree: Tree[A]) = fold[A, Int](tree)((_) => 0, (a, b) => (a max b) + 1)

def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = fold[A, Tree[B]](tree)((v) => Leaf(f(v)), (l, r) => Branch(l, r))
