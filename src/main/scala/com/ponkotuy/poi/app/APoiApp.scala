package com.ponkotuy.poi.app

import com.ponkotuy.poi.data.{APoi, Argument}

import scala.collection.breakOut
import scala.xml.{Node, Elem, Text, NodeSeq}

case class APoiApp(apoi: APoi, vars: Map[String, Any]) {
  import APoiApp._

  def build: NodeSeq = {
    def f(x: Node): NodeSeq = x match {
      case e: Elem if withAttr(e, Repeat.name) =>
        Repeat.apply(e, this).getOrElse(NodeSeq.Empty)
      case e: Elem => e.copy(child = e.child.flatMap(f))
      case Text(text) => Text(replace(text))
      case n => println(n.getClass); n
    }
    apoi.body.flatMap(f)
  }

  def replace(str: String): String = {
    val replaces = varPattern.findAllMatchIn(str).flatMap { matcher =>
      val vname = matcher.group(1).trim
      vars.get(vname).map { v =>
        Replace(matcher.matched, v.toString)
      }
    }
    replaces.foldLeft(str) { (orig, replace) => replace.exec(orig) }
  }
}

object APoiApp {
  def apply(apoi: APoi, apps: Seq[Any]): APoiApp = {
    assert(apoi.args.size == apps.size, "引数の数が一致しません")
    val vars: Map[String, Any] =
      apoi.args.zip(apps).map { case (Argument(name, _), app) =>
        name -> app
      }(breakOut)
    new APoiApp(apoi, vars)
  }

  def withAttr(x: Node, name: String): Boolean = x.attribute(name).isDefined

  val varPattern = """\{\{([^\}]+)\}\}""".r
}

case class Replace(orig: String, to: String) {
  def exec(str: String) = str.replace(orig, to)
}
