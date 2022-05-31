
/**
 * getState와 setState, unit, flatMap에 공통으로 성립하는 법칙은 어떤것들이있을까?
 */

def getState[S]: State[S,S]
def setState[S](s: => S): State[S,Unit]

case class State[S,A](run: S => (A, S)) {
  def map[B](f: A => B): State[S, B] =
    State(s => {
      val (a, s1) = run(s)
      (f(a), s1)
    })

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State(s => {
      val (a, s1) = run(s)
      f(a).run(s1)
    })


}