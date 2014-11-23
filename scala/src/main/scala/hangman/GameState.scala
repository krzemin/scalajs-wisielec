package hangman

trait GameState
case class InProgress(mistakesLeft: Int) extends GameState
case object Lost extends GameState
case class Won(steps: Int) extends GameState

