package nl.tno.hla.fom

import scala.xml._

/**
 * Represents a basic data type as defined in the FOM.
 */
case class BasicData(basicData: Node) extends PrimitiveDataType {
  lazy val name           = (basicData \ "name").text
  lazy val size           = (basicData \ "size").text
  lazy val interpretation = (basicData \ "interpretation").text.filter(_ != '\n')
  lazy val endian         = (basicData \ "endian").text
  lazy val encoding       = (basicData \ "encoding").text 
}