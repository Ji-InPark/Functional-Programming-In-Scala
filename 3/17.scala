/**
 * List[double]의 각 값을 String으로 변환하는 함수를 작성하라.
 * d: Double을 String으로 변환할 때에는 d.toString이라는 표현식을 사용하면 된다.
 */

def toString(as: List[Double]) = foldRight[Double, List[String]](as, Nil)((a, b) => Cons(a.toString, b))
