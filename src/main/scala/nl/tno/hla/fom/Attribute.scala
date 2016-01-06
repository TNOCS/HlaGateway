package nl.tno.hla.fom

import scala.xml._

/**
 * Represents a simple data type as defined in the FOM.
 */
case class Attribute(attribute: Node) {
  lazy val name            = (attribute \ "name").text
  lazy val dataType        = (attribute \ "dataType").text
  lazy val updateType      = (attribute \ "updateType").text
  lazy val updateCondition = (attribute \ "updateCondition").text
  lazy val ownership       = (attribute \ "ownership").text
  lazy val sharing         = (attribute \ "sharing").text
  lazy val transportation  = (attribute \ "transportation").text
  lazy val order           = (attribute \ "order").text
  lazy val semantics       = (attribute \ "semantics").text.filter(_ != '\n')

  def getDescription(): String = { return semantics; }
}