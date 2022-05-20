/**
 * 목록의 크기를 명시적으로 받지 않는 listof 조합기를 구현하라. 이 조합기는 Gen 대신 SGen을 돌려주어야 한다.
 * 구현은 받느시 요청된 크기의 목록들을 생성해야 한다.
 */

def listOf[A](g: Gen[A]): SGen[List[A]]
