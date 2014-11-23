package hangman


class Game(word: String, maxMistakes: Int = 5) {

  var moves: Vector[Char] = Vector.empty
  var visible: String = "_" * word.size
  var mistakes: Int = 0
  val alphabet = "AĄBCĆDEĘFGHIJKLŁMNŃOÓPRSTUWXYŹŻ"

  def move(letter: Char): Unit = {
    state match {
      case InProgress(_) =>
        moves = moves :+ letter.toUpper
        if (word.toUpperCase.contains(letter.toUpper)) {
          val indicies = word.toUpperCase.zipWithIndex.filter(_._1 == letter.toUpper).map(_._2).toSet
          visible = visible.zipWithIndex.map {
            case (let, idx) => if (indicies.contains(idx)) letter.toUpper else let
          }.mkString
        } else {
          mistakes = mistakes + 1
        }
      case _ =>
    }
  }

  def state: GameState = {
    if (mistakes < maxMistakes) {
      if (visible.toUpperCase == word.toUpperCase) {
        Won(moves.size)
      } else {
        InProgress(maxMistakes - mistakes)
      }
    } else {
      Lost
    }
  }
}

