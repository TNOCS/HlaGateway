package nl.tno.hla.swagger

import scala.xml._

/**
 * Represents a enumerated data type as defined in the FOM.
 */
case class EnumeratedData(basicData: nl.tno.hla.fom.EnumeratedData) extends PrimitiveDataType {
  lazy val name            = basicData.name
  lazy val representation  = basicData.representation
  lazy val semantics       = basicData.semantics
  lazy val enumerator      = basicData.enumerator
  lazy val fullDescription = basicData.fullDescription
  lazy val minimum         = basicData.minimum
  lazy val maximum         = basicData.maximum
  
  lazy private val dataType = representation match {
    case "HLAfloat32BE"           => "number"      
    case "HLAfloat64BE"           => "number"
    case "HLAinteger16BE"         => "integer"
    case "HLAinteger32BE"         => "integer"
    case "RPRunsignedInteger8BE"  => "integer"
    case "RPRunsignedInteger16BE" => "integer"
    case "RPRunsignedInteger32BE" => "integer"
    case "RPRunsignedInteger64BE" => "integer"
    case "HLAoctet"               => "number"      
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
    case default => "UNKNOWN TYPE: " + representation
  }

   /**
   * Convert the data type to a Swagger parameter definition.
   */
  override def toSwaggerParameter(propName: String, summary: String, numberOfSpaces: Int): String = {
    return toSwaggerParameter(propName, summary, dataType, format, s"""$name. $fullDescription""", numberOfSpaces, minimum, maximum)
  }
  
  /**
   * Convert the data type to a Swagger model property.
   */
  override def toSwaggerModelProperty(propName: String, summary: String, numberOfSpaces: Int): String = {
    // Deal with two special circumstances, both of which are strings, but are represented as array
    if (name == "RPRboolean") {
      val spaces = " " * (numberOfSpaces-1)
      return s"""$spaces $propName:
        |$spaces   type: boolean
        |$spaces   description: $name. $fullDescription
        |""".stripMargin
    }
    
    return toSwaggerModelProperty(propName, summary, dataType, format, s"""$name. $fullDescription""", numberOfSpaces, minimum, maximum)
  }
  
  /**
   * Convert the data type to a Swagger item property
   */
  override def toSwaggerItemProperty(numberOfSpaces: Int): String = {
    return toSwaggerItemProperty(dataType, format, s"""$name. $fullDescription""", numberOfSpaces, minimum, maximum)  
  }

}