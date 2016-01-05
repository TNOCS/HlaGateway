package main.fom

import scala.xml._

/**
 * Represents a simple data type as defined in the FOM.
 */
case class Attribute(attribute: Node) {
  lazy private val name            = (attribute \ "name").text
  lazy private val dataType        = (attribute \ "dataType").text
  lazy private val updateType      = (attribute \ "updateType").text
  lazy private val updateCondition = (attribute \ "updateCondition").text
  lazy private val ownership       = (attribute \ "ownership").text
  lazy private val sharing         = (attribute \ "sharing").text
  lazy private val transportation  = (attribute \ "transportation").text
  lazy private val order           = (attribute \ "order").text
  lazy private val semantics       = (attribute \ "semantics").text
  
  /**
   * Convert the simple data type to a Swagger parameter definition.
   */
  def toSwaggerParameter(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, PrimitiveDataType], numberOfSpaces: Int): String = {
    primitiveDataTypes get dataType match {
      case Some(dt) => return dt.toSwaggerParameter(name, semantics, numberOfSpaces)
      case None => { 
        val error = s""" *** Attribute: Error finding data type $dataType for attribute $name!\n"""
        println(error)
        return error
      }
    }
  }
  
  /**
   * Convert the simple data type to a Swagger model property.
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
        val error = s""" *** Attribute: Error finding data type $dataType for attribute $name!\n"""
        println(error)
        return ""
      }
    }
  }
}