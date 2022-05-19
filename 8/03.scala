/**
 * Prop의 표현이 다음과 같다고 할 때, &&를 Prop의 한 메서드로서 구현하라.
 */

trait Prop:
  self =>
  def check: Boolean
  def &&