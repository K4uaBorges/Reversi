package reversi_to_compose.model

open class TTTFatalException(msg: String) : Exception(msg){
    override fun toString(): String = message ?: "Erro Desconhecido"
}

class MongoClientException(msg: String) : TTTFatalException(msg)
class NoStorageOrModifierFileException : TTTFatalException("Cannot load game, try create a new game")
class NoPassPossibleException : TTTFatalException("Cannot pass possible game")
class NoChangesException : TTTFatalException("No Changes")
class NoGameStartedException : TTTFatalException("Game not started")
class NoPlayerColorException : TTTFatalException("Player Color is not running")

