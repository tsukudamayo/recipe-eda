import scala.io._

val source = Source.fromFile("sample.txt", "UTF-8")
try {
  source.getLines().foreach{line: String => println(line)
  }
} finally {
  source.close()
}


