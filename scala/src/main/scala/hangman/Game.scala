package hangman


class Game(word: String, maxMistakes: Int = 5) {

  var moves: Vector[Char] = Vector.empty
  var visible: String = "_" * word.size
  var mistakes: Int = 0

  def move(letter: Char): Unit = {
    state match {
      case InProgress(_) =>
        moves = moves :+ letter
        if (word.contains(letter)) {
          val indicies = word.zipWithIndex.filter(_._1 == letter).map(_._2)
          visible = visible.zipWithIndex.map {
            case (let, idx) => if (indicies.contains(idx)) letter else let
          }.mkString
        } else {
          mistakes = mistakes + 1
        }
      case _ =>
    }
  }

  def state: GameState = {
    if (mistakes < maxMistakes) {
      if (visible == word) {
        Won(moves.size)
      } else {
        InProgress(maxMistakes - mistakes)
      }
    } else {
      Lost
    }
  }
}

