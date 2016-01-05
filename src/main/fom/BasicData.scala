package main.fom

import scala.xml._

/**
 * Represents a basic data type as defined in the FOM.
 */
case class BasicData(basicData: Node) extends PrimitiveDataType {
  lazy private val name           = (basicData \ "name").text
  lazy private val size           = (basicData \ "size").text
  lazy private val interpretation = (basicData \ "interpretation").text
  lazy private val endian         = (basicData \ "endian").text
  lazy private val encoding       = (basicData \ "encoding").text
  
  lazy private val dataType = name match {
    case "RPRunsignedInteger8BE"  => "integer"
    case "RPRunsignedInteger16BE" => "integer"
    case "RPRunsignedInteger32BE" => "integer"
    case "RPRunsignedInteger64BE" => "integer"
    case default => "UNKNOWN Basic data type: " + name
  }
    
  lazy private val format = name match {
    case "RPRunsignedInteger8BE"  => "int32"
    case "RPRunsignedInteger16BE" => "int32"
    case "RPRunsignedInteger32BE" => "int32"
    case "RPRunsignedInteger64BE" => "int64"
    case default => "UNKNOWN Basic data type: " + name
  }
    
   /**
   * Convert the data type to a Swagger parameter definition.
   */
  override def toSwaggerParameter(propName: String, summary: String, numberOfSpaces: Int): String = {
    return toSwaggerParameter(propName, summary, dataType, format, s"""$name ($interpretation)""", numberOfSpaces)
  }
  
  /**
   * Convert the data type to a Swagger model property.
   */
  override def toSwaggerModelProperty(propName: String, summary: String, numberOfSpaces: Int): String = {
    return toSwaggerModelProperty(propName, summary, dataType, format, s"""$name ($interpretation)""", numberOfSpaces)
  }
    
  /**
   * Convert the data type to a Swagger item property
   */
  override def toSwaggerItemProperty(numberOfSpaces: Int): String = {
    return toSwaggerItemProperty(dataType, format, s"""$name ($interpretation)""", numberOfSpaces)  
  }

  
}