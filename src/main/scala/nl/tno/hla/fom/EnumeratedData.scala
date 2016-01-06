package nl.tno.hla.fom

import scala.xml._

/**
 * Represents a enumerated data type as defined in the FOM.
 */
case class EnumeratedData(basicData: Node) extends PrimitiveDataType {
  lazy val name            = (basicData \ "name").text
  lazy val representation  = (basicData \ "representation").text
  lazy val semantics       = (basicData \ "semantics").text
  lazy val enumerator      = (basicData \ "enumerator")
  lazy val description     = enumerator map { e => (e \ "value").text + ". " + (e \ "name").text } mkString "; "
  lazy val fullDescription = s"""$semantics (Allowable values are $description)""".filter(_ != '\n')
  lazy val values          = (enumerator \ "value") map { v => v.text toInt }
  lazy val minimum         = values min
  lazy val maximum         = values max
}