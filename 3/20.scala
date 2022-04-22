/**
 * map과 비슷하되 하나의 요소가 아니라 목록의 최종 결과 목록에 삽입하는 함수 flatMap을 작성하라.
 * 서명은 다음과 같다.
 *
 * 예를 들어 flatMap(List(1, 2, 3))(i => List(i, i))는 List(1, 1, 2, 2, 3, 3)이 되어야 한다.
 */

def flatMap[A,B](as: List[A])(f: A => List[B]): List[B]
