/**
 * foldRight로 구현된 product(목록 3.2의 product2)가 0.0을 만났을 때 즉시 재귀를 멈추고 0.0을 돌려줄까?
 * 왜 그럴까? 아니라면 왜 아닐까?
 * foldRight를 긴 목록으로 호출했을 때 어떤 평가 단축이 어떤식으로 일어나는지 고찰하라.
 * 이는 다른 연습문제들보다 심오한 문제이며, 제 5장에서 다시 살펴볼 것이다.
 */
// 안 멈춘다. 리스트에 대해서만 순회를 하지 B에 대해서 검사하고, short-circuit 하는 코드가 전혀 없다
