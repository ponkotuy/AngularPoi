package com.ponkotuy.poi.app

import com.ponkotuy.poi.Global

import scala.xml.{Elem, NodeSeq}

object Repeat {
  def name = s"${Global.dir}-repeat"
  def apply(node: Elem, app: APoiApp): Option[NodeSeq] = {
    println("repeat!")
    for {
      attr <- node.attribute(name).flatMap(_.headOption)
      Array(before, after) = attr.text.split("in").map(_.trim)
      list <- app.vars.get(after)
    } yield {
      val newAttr = node.attributes.remove(name)
      list.asInstanceOf[Seq[Any]].flatMap { v =>
        val newNode = node.copy(attributes = newAttr)
        val newAPoi = app.apoi.copy(body = newNode)
        val newApp = APoiApp(newAPoi, app.vars + (before -> v))
        newApp.build
      }: NodeSeq
    }
  }
}
