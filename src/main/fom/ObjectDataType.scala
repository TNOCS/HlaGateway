package main.fom

import scala.xml._

/**
 * A class that represents a data type that is represented as an object.
 */
 class ObjectDataType {
  /**
   * Specifies whether the object data type is referenced in a definition
   */
  var isReferenced = false
  
  /**
   * Specifies whether the object data type has already been defined
   */
  var isDefined = false
  
  /**
   * Override this method in a sub-class
   */
  def toSwaggerModel(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, ObjectDataType], numberOfSpaces: Int = 2): String = { return "" }
  
  /**
   * Generate a reference to a swagger definition
   */
  def toSwaggerModelReference(name: String, summary: String, numberOfSpaces: Int): String = {
    isReferenced = true
    val spaces = " " * numberOfSpaces
    return spaces + "$ref: '#/definitions/" + name + "'\n"
  }
}