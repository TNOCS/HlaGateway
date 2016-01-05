package main.fom

import scala.xml._

/**
 * Represents a simple data type as defined in the FOM.
 */
case class SimpleData(simpleData: Node) extends PrimitiveDataType {
  lazy private val name           = (simpleData \ "name").text
  lazy private val representation = (simpleData \ "representation").text
  lazy private val units          = (simpleData \ "units").text
  lazy private val resolution     = (simpleData \ "resolution").text
  lazy private val accuracy       = (simpleData \ "accuracy").text
  lazy private val semantics      = (simpleData \ "semantics").text
  
  lazy private val dataType = representation match {
    case "HLAfloat32BE"           => "number"      
    case "HLAfloat64BE"           => "number"
    case "HLAinteger16BE"         => "integer"
    case "HLAinteger32BE"         => "integer"
    case "RPRunsignedInteger8BE"  => "integer"
    case "RPRunsignedInteger16BE" => "integer"
    case "RPRunsignedInteger32BE" => "integer"
    case "RPRunsignedInteger64BE" => "integer"
    case "HLAoctet"               => "integer"      
    case "HLAunicodeChar"         => "string"      
    case default => "UNKNOWN TYPE: " + representation
  }
    
  lazy private val format = representation match {
    case "HLAfloat32BE"           => "float"      
    case "HLAfloat64BE"           => "double"
    case "HLAinteger16BE"         => "int32"
    case "HLAinteger32BE"         => "int32"
    case "RPRunsignedInteger8BE"  => "int32"
    case "RPRunsignedInteger16BE" => "int32"
    case "RPRunsignedInteger32BE" => "int32"
    case "RPRunsignedInteger64BE" => "int64"
    case "HLAoctet"               => "int32"      
    case "HLAunicodeChar"         => "string"      
    case default => "UNKNOWN TYPE: " + representation
  }
    
  /**
   * Convert the simple data type to a Swagger parameter definition.
   */
  override def toSwaggerParameter(propName: String, summary: String, numberOfSpaces: Int): String = {
    return toSwaggerParameter(propName, summary, dataType, format, s"""$name ($semantics)""", numberOfSpaces)
  }
  
  /**
   * Convert the simple data type to a Swagger model property.
   */
  override def toSwaggerModelProperty(propName: String, summary: String, numberOfSpaces: Int): String = {
    return toSwaggerModelProperty(propName, summary, dataType, format, s"""$name ($semantics)""", numberOfSpaces)
  }
  
  /**
   * Convert the data type to a Swagger item property
   */
  override def toSwaggerItemProperty(numberOfSpaces: Int): String = {
    return toSwaggerItemProperty(dataType, format, s"""$name ($semantics)""", numberOfSpaces)  
  }
}