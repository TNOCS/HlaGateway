package main.fom

import scala.xml._

/**
 * Class for mapping the alternatives in VariantRecordData
 */

case class Alternative(alternative: Node) {
  lazy private val name       = (alternative \ "name"      ).text
  lazy private val dataType   = (alternative \ "dataType"  ).text
  lazy private val enumerator = (alternative \ "enumerator").text
  lazy private val semantics  = (alternative \ "semantics" ).text + s""" Note that this field is only valid of the discriminant is <code>$enumerator</code>."""

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
        val error = s""" *** VariantRecordData: Error finding data type $dataType for attribute $name!\n"""
        println(error)
        return error
      }
    }
  }
}

case class VariantRecordData(variantRecord: Node) extends ObjectDataType {
  lazy private val name         = variantRecord \ "name" text
  /** 
   *  The discriminant specifies which of the alternatives is valid
   */
  lazy private val discriminant = variantRecord \ "discriminant" text
  lazy private val dataType     = variantRecord \ "dataType" text
  lazy private val encoding     = variantRecord \ "encoding" text
  lazy private val semantics    = variantRecord \ "semantics" text
  lazy private val alternatives = variantRecord \ "alternative" map { a => Alternative(a) }
  
  /**
   * Generate a Swagger model definition
   */
  override def toSwaggerModel(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, ObjectDataType], numberOfSpaces: Int = 2): String = {
    var spaces = " " * (numberOfSpaces-1)
    var yaml = s"""$spaces $name:
      |$spaces   type: object
      |$spaces   description: $semantics
      |$spaces   properties:
      |""".stripMargin
    
    val nmbrOfSpaces = numberOfSpaces + 4
    // First check if the attribute requires a model to be described
    objectDataTypes get dataType match {
      case Some(dt) => {
        val spaces = " " * nmbrOfSpaces
        yaml += spaces + discriminant + ":\n" + dt.toSwaggerModelReference(dataType, semantics, nmbrOfSpaces + 2)
      }
      case None => {}
    }
    
    // Second, we assume its a primitive data type (or we fail).
    primitiveDataTypes get dataType match {
      case Some(dt) => yaml += dt.toSwaggerModelProperty(discriminant, semantics, nmbrOfSpaces)
      case None => { 
        val error = s""" *** VariantRecordData: Error finding data type $dataType for attribute $name!\n"""
        println(error)
        return error
      }
    }
      
    alternatives.foreach( field => {
      yaml += field.toSwaggerModelProperty(primitiveDataTypes, objectDataTypes, nmbrOfSpaces)
    })
    return yaml    
  }
  
}