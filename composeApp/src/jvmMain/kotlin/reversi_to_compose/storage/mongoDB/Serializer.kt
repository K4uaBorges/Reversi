package reversi_to_compose.storage.mongoDB;

interface Serializer<Data> {
    fun serialize(d: Data): String;
    fun deserialize(txt: String): Data;

}