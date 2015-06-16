package com.ponkotuy.poi.data

import java.io.File

import com.ponkotuy.poi.app.APoiApp

import scala.xml.{Null, Elem, NodeSeq}
import scala.xml.parsing.ConstructingParser

case class APoi(args: List[Argument], body: NodeSeq) {

  def build(apps: Any*): NodeSeq = {
    val app = APoiApp(this, apps)
    val doc = ConstructingParser.fromFile(new File("sample.apoi"), false).document()
    doc.children.foreach {
      case e: Elem => println(e); e
      case _ => Null
    }
    NodeSeq.fromSeq(Nil)
  }

}

case class Argument(name: String, typ: String)
