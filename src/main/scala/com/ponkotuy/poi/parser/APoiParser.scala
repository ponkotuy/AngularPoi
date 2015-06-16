package com.ponkotuy.poi.parser

import java.io.Reader

import com.ponkotuy.poi.data.{APoi, Argument}

import scala.collection.breakOut
import scala.io.Source
import scala.xml.XML

object APoiParser {
  case class APoiParserException(msg: String) extends Exception(msg)
  def load(reader: Reader): APoi = {
    val xml = XML.load(reader)
    val metas = (xml \ "head" \ "meta").filter(_.attribute("arg").isDefined)
    val args: List[Argument] = metas.map { meta =>
      val arg = for {
        name <- meta.attribute("arg").flatMap(_.headOption)
        typ <- meta.attribute("type").flatMap(_.headOption)
      } yield { Argument(name.text, typ.text) }
      arg.getOrElse(throw new APoiParserException("""Argument error: not found "type""""))
    }(breakOut)
    APoi(args, xml)
  }
}

object Main extends App {
  val reader = Source.fromFile("sample.apoi").bufferedReader()
  println(APoiParser.load(reader).build(List("a", "b", "c"), "Hello"))
}
