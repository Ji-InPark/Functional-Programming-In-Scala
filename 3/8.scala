/**
 * foldRight(List(1, 2, 3), Nil:List[Int])(Cons(_, _)) 처럼 Nil과 cons 자체를 foldRight에 전달하면 어떤 일이 발생할까?
 * 이로부터, foldRight와 list의 자료 생성자들 사이의 관계에 관해 어떤 점을 알 수 있는가?
 */
어떤일이 발생하나요?
그냥 평과가정 추적처럼 끝까지 순회하는거 아닌가여?