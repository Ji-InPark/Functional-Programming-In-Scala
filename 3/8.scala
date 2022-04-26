/**
 * foldRight(List(1, 2, 3), Nil:List[Int])(Cons(_, _)) 처럼 Nil과 cons 자체를 foldRight에 전달하면 어떤 일이 발생할까?
 * 이로부터, foldRight와 list의 자료 생성자들 사이의 관계에 관해 어떤 점을 알 수 있는가?
 */
// 원래 리스트를 그대로 얻게 된다. foldRight는 리스트 생성자를 그대로 따라가면서 f를 적용하기 때문
