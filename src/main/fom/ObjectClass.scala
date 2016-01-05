package main.fom

import scala.xml._

case class ObjectClass(objectClass: Node) {
  lazy private val name          = (objectClass \ "name").text
  lazy private val sharing       = (objectClass \ "sharing").text
  lazy private val semantics     = (objectClass \ "semantics").text
  lazy private val attributes    = (objectClass \ "attribute") map { a => Attribute(a) } 
  lazy private val objectClasses = (objectClass \ "objectClass")
  
  def getName(): String = { return name; }
  
  def getDescription(): String = { return semantics; }
  
  /**
   * Convert to a Swagger model.
   */
  def toSwaggerModel(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, ObjectDataType]): String = {
    var yaml = s"""  $name:
      |    type: object
      |    properties:
      |""".stripMargin
    attributes.foreach( attribute => {
      yaml += attribute.toSwaggerModelProperty(primitiveDataTypes, objectDataTypes, 6)
    })
    // TODO Add other object classes as reference
    return yaml
  }  
}