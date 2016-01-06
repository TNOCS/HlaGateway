package nl.tno.hla.fom

import scala.xml._

case class ArrayData(arrayData: Node) extends ObjectDataType {
  lazy val name        = arrayData \ "name" text
  lazy val dataType    = arrayData \ "dataType" text
  lazy val cardinality = arrayData \ "cardinality" text
  lazy val encoding    = arrayData \ "encoding" text
  lazy val semantics   = (arrayData \ "semantics").text.filter(_ != '\n')   
}