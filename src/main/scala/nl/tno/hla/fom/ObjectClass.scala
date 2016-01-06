package nl.tno.hla.fom

import scala.xml._

case class ObjectClass(objectClass: Node) {
  lazy val name          = (objectClass \ "name").text
  lazy val sharing       = (objectClass \ "sharing").text
  lazy val semantics     = (objectClass \ "semantics").text.filter(_ != '\n')
  lazy val attributes    = (objectClass \ "attribute") map { a => Attribute(a) } 
  lazy val objectClasses = (objectClass \ "objectClass") map { o => ObjectClass(o) }
    
  def getDescription(): String = { return semantics; }
}