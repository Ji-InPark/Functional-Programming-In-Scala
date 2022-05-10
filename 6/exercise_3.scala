trait RNG:
  def nextInt: (Int, RNG) // Should generate a random `Int`. We'll later define other functions in terms of `nextInt`.

object RNG:
  // NB - this was called SimpleRNG in the book text

  case class Simple(seed: Long) extends RNG:
    def nextInt: (Int, RNG) =
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

  /*
   * Ex 6.1
   * RNG.nextInt를 이용해서 0 이상, Int,MaxValue 이하의 난수 정수를 생성하는 함수를 작성하라. nextInt가 Int.MinValue를 돌려주는
   * 구석진 경우(음이 아닌 대응수가 없다)도 확실하게 처리해야 한다.
   */
  def nonNegativeInt(rng: RNG): (Int, RNG) = ???

  /*
   * Ex 6.2
   * 0 이상, 1 미만의 Double 난수를 발생하는 함수를 작성하라. 참고: 최대의 양의 정수를 얻으려면 Int.MaxValue를, x: Int를 Double로
   * 변환하려면 x.toDouble을 사용하면 된다.
   */
  def double(rng: RNG): (Double, RNG) = ???

  /*
   * Ex 6.3
   * 각각 난수쌍 (Int, Double) 하나, (Double, Int) 하나, 3튜플 (Double, Double, Double) 하나를 발생하는 함수들을 작성하라. 앞에서
   * 작성한 함수들을 재사용할 수 있어야 한다.
   */
  def intDouble(rng: RNG): ((Int,Double), RNG) = ???

  def doubleInt(rng: RNG): ((Double,Int), RNG) = ???

  def double3(rng: RNG): ((Double,Double,Double), RNG) = ???

  /*
   * Ex 6.4
   * 전수 난수들의 목록을 생성하는 함수를 작성하라.
   */
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = ???

  /*
   * Ex 6.5
   * 연습문제 6.2의 double을 map을 이용해서 좀 더 우아한 방식으로 구현하라.
   */
  /*
   * Ex 6.6
   * 다음과 같은 서명에 따라 map2를 구현하라. 이 함수는 두 상태 동작 ra 및 rb와 이들의 결과를 조합하는 함수 f를 받고 두 동작을 조합한 새
   * 동작을 돌려준다.
   */
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = ???

  /*
   * Ex 6.7
   * 어려움: 두 RNG 상태 전이를 조합할 수 있다면, 그런 상태 전이들의 목록 전체를 조합하는 것도 가능해야 마땅하다. 상태 전이들의 List를
   * 하나의 상태 전이로 조합하는 함수 sequence를 구현하라. 그리고 이 함수를 이용해서 이전에 작성한 ints 함수를 다시 구현하라. ints 함수의
   * 구현에서 x가 n번 되풀이되는 목록을 만들 일이 있으면 표준 라이브러리 함수 List.fill(n)(x)를 사용해도 좋다.
   */
  def sequence[A](rs: List[Rand[A]]): Rand[List[A]] = ???

  /*
   * Ex 6.8
   * flatMap을 구현하고 그것을 이용해서 nonNegativeLessThan을 구현하라.
   */
  def flatMap[A, B](r: Rand[A])(f: A => Rand[B]): Rand[B] = ???

  /*
   * Ex 6.9
   * map과 map2를 flatMap을 이용해서 다시 구현하라. 이것이 가능하다는 사실은 앞에서 flatMap이 map과 map2보다 더 강력하다고 말한 근거가
   * 된다.
   */
  def mapViaFlatMap[A, B](r: Rand[A])(f: A => B): Rand[B] = ???

  def map2ViaFlatMap[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = ???

opaque type State[S, +A] = S => (A, S)

object State:
  /*
   * Ex 6.10
   * 함수 unit, map, map2, flatMap, sequence를 일반화하라. 가능하면 이들을 State 경우 클래스의 메서드로 추가하되, 불가능한 경우에는
   * State 동반 객체에 넣을 것.
   */
  extension [S, A](underlying: State[S, A])
    def run(s: S): (A, S) = underlying(s)

    def map[B](f: A => B): State[S, B] =
      ???

    def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
      ???

    def flatMap[B](f: A => State[S, B]): State[S, B] =
      ???

  def apply[S, A](f: S => (A, S)): State[S, A] = f

enum Input:
  case Coin, Turn

case class Machine(locked: Boolean, candies: Int, coins: Int)

object Candy:
  /*
   * Ex 6.11
   * 어려움: State를 사용하는 경험을 쌓기 위해, 간단한 사탕 판매기를 본뜬 유한상태자동자(finite state automata)를 구현하라. 이
   * 판매기에는 두 종류의 입력이 있다. 하나는 사용자가 넣는 동전이고, 또 하나는 돌리면 사탕이 나오는 손잡이이다. 또한, 이 판매기는 사탕이 몇
   * 개나 남았는지, 동전이 몇 개나 들어 있는지도 추적한다.
   * 사탕 판매기의 자동 규칙은 다음과 같다.
   * - 잠겨진 판매기에 동전을 넣으면, 사탕이 남아 있는 경우 잠김이 풀린다.
   * - 풀린 판매기의 손잡이를 돌리면 사탕이 나오고 판매기가 잠긴다.
   * - 잠긴 판매기의 손잡이를 돌리거나 풀린 판매기에 동전을 넣으면 아무 일도 생기지 않는다.
   * - 사탕이 없는 판매기는 모든 입력을 무시한다.
   * simulateMachine 메서드는 입력들의 목록에 기초해서 판매기를 작동하고, 작동이 끝난 후 판매기에 있는 동전 개수와 사탕 개수를 돌려주어야
   * 한다. 예를 들어 동전이 10개, 사탕이 5개 있는 Machine에서 총 4개의 사탕이 성공적으로 팔렸다면 출력은 (14, 1)이어야 한다.
   */
  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = ???