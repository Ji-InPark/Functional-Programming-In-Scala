/**
 * Gen의 이러한 표현을 이용해서
 * unit, boolean, listOfN도 구현
 */

def unit[A](a: => A): Gen[A]
def boolean: Gen[Boolean]
def listOfN[A](n: Int, g: Gen[A]):Gen[List[A]]