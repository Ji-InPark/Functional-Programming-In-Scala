/**
 * sum, product와 목록의 길이를 계산하는 함수를 foldLeft를 이용해서 작성하라
 */

def sum(as: List[Int]) = foldLeft(as, 0)(_ + _)

def product(as: List[Int]) = foldLeft(as, 1)(_ * _)

def length[A](as: List[A]) = foldLeft(as, 0)((b, _) => b + 1)
