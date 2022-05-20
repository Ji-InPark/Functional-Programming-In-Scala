/**WHITE 문제
 * union과 비슷하되, 각 Gen에 대한 가중치를 받고 각 Gen에서 해당 가중치에 비례하는 확률로 갑들을 뽑는 weighted를 구현하라.
 */

def weighted[A](g1: (Gen[A],Double),g2: (Gen[A],Double)): Gen[A]