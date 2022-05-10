
/**
 * 7.12
 * choiceN에도 여전히 자의적인 구석이 남아 있다. List를 사용하는 것은 필요 이상으로 구체적인 것 같다. 
 * 이 조합기는 컨테이너의 구체적인 종류에 구애받을 필요가 없다.
 * 예를 들어, 계산들의 목록 대신 계산들의 Map이 있다면 어떨까?
 *  def choiceMap[K, V](Key: Par[K])(chocies: Map[K, Par[V]]): Par[V]
 * 원한다면 책을 잠시 덮고, choice와 choiceN, choiceMap의 구현에 사용할 수 있는 좀 더 일반적인 새 조합기를 고안해 보기 바란다.
 */