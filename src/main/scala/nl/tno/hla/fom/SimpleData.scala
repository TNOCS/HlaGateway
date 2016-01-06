package nl.tno.hla.fom

import scala.xml._

/**
 * Represents a simple data type as defined in the FOM.
 */
case class SimpleData(simpleData: Node) extends PrimitiveDataType {
  lazy val name           = (simpleData \ "name").text
  lazy val representation = (simpleData \ "representation").text.filter(_ != '\n')
  lazy val units          = (simpleData \ "units").text
  lazy val resolution     = (simpleData \ "resolution").text
  lazy val accuracy       = (simpleData \ "accuracy").text
  lazy val semantics      = (simpleData \ "semantics").text
}