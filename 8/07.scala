/**
 * 같은 형식의 생성기 두 개를 하나로 결합하는 union을 구현하라. union은 각 생성기의 값들을 동일 확률로 뽑아서 취합해야 한다.
 */

def union[A](gl: Gen[A], g2 Gen[A]): Gen[A]