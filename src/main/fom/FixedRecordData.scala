package main.fom

import scala.xml._

case class FieldData(fieldData: Node) {
  lazy private val name      = (fieldData \ "name").text
  lazy private val dataType  = (fieldData \ "dataType").text
  lazy private val semantics = (fieldData \ "semantics").text

  /**
   * Convert to a Swagger model property.
   */
  def toSwaggerModelProperty(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, ObjectDataType], numberOfSpaces: Int): String = {
    // First check if the attribute requires a model to be described
    objectDataTypes get dataType match {
      case Some(dt) => {
        val spaces = " " * numberOfSpaces
        return spaces + name + ":\n" + dt.toSwaggerModelReference(dataType, semantics, numberOfSpaces + 2)
      }
      case None => {}
    }
    
    // Second, we assume its a primitive data type (or we fail).
    primitiveDataTypes get dataType match {
      case Some(dt) => return dt.toSwaggerModelProperty(name, semantics, numberOfSpaces)
      case None => { 
        val error = s""" *** FixedRecordData: Error finding data type $dataType for attribute $name!\n"""
        println(error)
        return error
      }
    }
  }
}

case class FixedRecordData(fixedRecord: Node) extends ObjectDataType {
  lazy private val name      = fixedRecord \ "name" text
  lazy private val encoding  = fixedRecord \ "encoding" text
  lazy private val semantics = fixedRecord \ "semantics" text
  lazy private val fields    = fixedRecord \ "field" map { f => FieldData(f) }
    
  /**
   * Generate a Swagger model definition
   */
  override def toSwaggerModel(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, ObjectDataType], numberOfSpaces: Int = 2): String = {
    val spaces = " " * (numberOfSpaces-1)
    var yaml = s"""$spaces $name:
      |$spaces   type: object
      |$spaces   description: $semantics
      |$spaces   properties:
      |""".stripMargin
    fields.foreach( field => {
      yaml += field.toSwaggerModelProperty(primitiveDataTypes, objectDataTypes, numberOfSpaces + 4)
    })
    return yaml    
  }

}