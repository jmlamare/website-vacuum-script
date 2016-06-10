package jml.example

import scalatags.Text.all._

// http://www.hars.de/2009/01/html-as-xml-in-scala.html
// scala.io.Source.fromURL("https://www.englishclub.com/ref/Idioms/A/").getLines().foreach(println)
object HtmlVacuum {
	def parse[T](url: String)(fn: (scala.xml.Node)=>T)(implicit parser: javax.xml.parsers.SAXParser): Option[T] = {
		try {
			val adapter = new scala.xml.parsing.NoBindingFactoryAdapter
			val source = new org.xml.sax.InputSource(url)
			val html = adapter.loadXML(source, parser)
			return Some(fn(adapter.loadXML(source, parser)))
		} catch {
			case e : java.lang.Exception => None
		}
	}
	def tryWith[T, Q](c: T {def close(): Unit})(f: (T) => Q): Q = {
		try {
			f(c)
		} finally {
			c.close()
		}
	}
	def main(args: Array[String]) {
		implicit val parser = new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser()

		tryWith(new java.io.PrintWriter(new java.io.File("output.html"))) { pw =>
			pw.print(
				 html(
					head(
						link(rel:="stylesheet", href:="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/css/materialize.min.css")
					),
					body(
						table(cls:="striped", 
							('A' to 'Z').flatMap(letter =>
								parse("https://www.englishclub.com/ref/Idioms/" + letter + "/") { html=>
									for {
										node <- html \\ "div" if (node \ "@class").text == "linklisting"
										val expr = (node \ "h3" \\ "a").text.trim()
										val desc = (node \ "div").text.trim()
										val url  = (node \ "h3" \ "a" \ "@href").toString
									} yield(expr, desc, url)
								}
								.getOrElse(List())
								.flatMap { case (expr, desc, url)=>
									println(url)
									parse(url) { html=>
										for {
											main <- html \\ "main" if (main \ "@id").text == "ec-main"
											samp <- (main \ "div") if (samp \ "@class").text == "example"
										} yield (expr, desc, (samp \\ "li").map(_.text.trim()))
									}.getOrElse(List())
								}
								.flatMap { case (expr, desc, samples)=>
									List(
										tr(
											td(expr), td(desc)
										),
										tr(
											td(colspan:=2,  dl(samples.map((sample)=>li(sample))))
										)
									);
								}
							)
						)
					)
				)
			)
		}
	}
}

