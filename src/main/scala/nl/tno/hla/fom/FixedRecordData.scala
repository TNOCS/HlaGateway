package nl.tno.hla.fom

import scala.xml._

case class FieldData(fieldData: Node) {
  lazy val name      = (fieldData \ "name").text
  lazy val dataType  = (fieldData \ "dataType").text
  lazy val semantics = (fieldData \ "semantics").text.filter(_ != '\n')
}

case class FixedRecordData(fixedRecord: Node) extends ObjectDataType {
  lazy val name      = fixedRecord \ "name" text
  lazy val encoding  = fixedRecord \ "encoding" text
  lazy val semantics = (fixedRecord \ "semantics").text.filter(_ != '\n')
  lazy val fields    = fixedRecord \ "field" map { f => FieldData(f) }
}