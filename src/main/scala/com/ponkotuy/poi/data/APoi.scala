package com.ponkotuy.poi.data

import com.ponkotuy.poi.app.APoiApp

import scala.xml.NodeSeq

case class APoi(args: List[Argument], body: NodeSeq) {
  def build(apps: Any*): NodeSeq = {
    APoiApp(this, apps).build
  }
}

case class Argument(name: String, typ: String)
