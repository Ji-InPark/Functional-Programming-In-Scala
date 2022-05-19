/**
 * flatMap을 구현하고, 그것을 이용해서 다음과 같은 listOfN의 좀 더 동적인 버전을 구현하라.
 * flatMap과 listOfN을 Gen 클래스의 매서드로 둘것.
 */

def flatMap[B](f:A => Gen[B]): Gen[B]
def listOfN(size: Gen[Int]): Gen[List[A]]