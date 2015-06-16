package com.ponkotuy.poi.app

import com.ponkotuy.poi.data.{APoi, Argument}

import scala.collection.breakOut

case class APoiApp(apoi: APoi, apps: Seq[Any]) {
  import APoiApp._

  assert(apoi.args.size == apps.size, "引数の数が一致しません")
  implicit val vars: Map[String, Any] =
    apoi.args.zip(apps).map { case (Argument(name, _), app) =>
      name -> app
    }(breakOut)

  def replace(str: String): String = {
    val replaces = varPattern.findAllMatchIn(str).map { matcher =>
      Replace(matcher.matched, vars(matcher.group(0).trim).toString)
    }
    replaces.foldLeft(str) { (orig, replace) => replace.exec(orig) }
  }
}

case class Replace(orig: String, to: String) {
  def exec(str: String) = str.replace(orig, to)
}

object APoiApp {
  val varPattern = "{{([^}]+)}}".r
}

case class Vars(variables: Map[String, Any])
