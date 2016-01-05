package main.fom

import scala.xml._

case class ArrayData(arrayData: Node) extends ObjectDataType {
  lazy private val name        = arrayData \ "name" text
  lazy private val dataType    = arrayData \ "dataType" text
  lazy private val cardinality = arrayData \ "cardinality" text
  lazy private val encoding    = arrayData \ "encoding" text
  lazy private val semantics   = arrayData \ "semantics" text
    
  /**
   * Generate a Swagger model definition
   */
  override def toSwaggerModel(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, ObjectDataType], numberOfSpaces: Int = 2): String = {
    val spaces = " " * (numberOfSpaces-1)
    
    // Deal with two special circumstances, both of which are strings, but are represented as array
    if (name == "RTIobjectId" || name == "RPRUserDefinedTag" || name == "DateTime18") {
      return s"""$spaces $name:
        |$spaces   type: string
        |""".stripMargin
    }
        
    var yaml = s"""$spaces $name:
      |$spaces   type: array
      |$spaces   items:
      |""".stripMargin
    // First check if the attribute requires a model to be described
    objectDataTypes get dataType match {
      case Some(dt) => {
        yaml += dt.toSwaggerModelReference(dataType, semantics, numberOfSpaces + 4)
        return yaml
      }
      case None => {}
    }
    
    // Second, we assume its a primitive data type (or we fail).
    primitiveDataTypes get dataType match {
      case Some(dt) => return yaml + dt.toSwaggerItemProperty(numberOfSpaces + 4)
      case None => { 
        val error = s""" *** ArrayData: Error finding data type $dataType for attribute $name!\n"""
        println(error)
        return yaml
      }
    }
  }

}