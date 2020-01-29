import com.google.gson._
import java.util
import java.lang.reflect.Type
import scala.language.implicitConversions
import scala.collection.JavaConversions._

/** JSON を List に変換する */
class ScalaListDeserializer extends JsonDeserializer[List[_]]{

  override def deserialize(json: JsonElement,
                           typeOfT: Type,
                           context: JsonDeserializationContext): List[_] =
    // Java の List として構築してから Scala のコレクションに変換
    context.deserialize[util.List[_]](json, classOf[util.List[_]]).toList
}

/** JSON を Set に変換する */
class ScalaSetDeserializer extends JsonDeserializer[Set[_]]{

  override def deserialize(json: JsonElement,
                           typeOfT: Type,
                           context: JsonDeserializationContext): Set[_] =
    context.deserialize[util.List[_]](json, classOf[util.List[_]]).toSet
}

object Main extends App{
  // 準備
  val gb = new GsonBuilder
  // ScalaListDeserializer で List オブジェクトだけでなく、
  // Seq オブジェクトなども取得できるようにする
  gb.registerTypeHierarchyAdapter(classOf[GenTraversableOnce[_]], new ScalaListDeserializer)
  // ScalaSetDeserializer は Set オブジェクトだけ
  gb.registerTypeAdapter(classOf[Set[_]], new ScalaSetDeserializer)
  val gson = gb.create()

  // JSON => List
  val result1 = gson.fromJson("[1, 2, 3, 4, 5]", classOf[List[Int]])
  println(result1)  // 「List(1.0, 2.0, 3.0, 4.0, 5.0)」と表示

  // JSON => Set
  val result2 = gson.fromJson("""["abc", "def"]""", classOf[Set[String]])
  println(result2)  // 「Set(abc, def)」と表示

  // JSON => Seq
  val result3 = gson.fromJson("[1, 2, 3]", classOf[Seq[Int]])
  println(result3)  // 「List(1.0, 2.0, 3.0)」と表示
}
