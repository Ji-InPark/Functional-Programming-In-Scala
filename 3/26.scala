/**
 * Tree[Int]에서 가장 큰 요소를 돌려주는 함수 maximum을 작성하라.
 * (스칼라에서 두 정수 x와 y의 최댓값은 x.maxZ(y)나 x max y로 계산할 수 있다.)
 */

def max(tree: Tree[Int]): Int = tree match {
  case Leaf(v) => v
  case Branch(left, right) => max(left) max max(right)
}
