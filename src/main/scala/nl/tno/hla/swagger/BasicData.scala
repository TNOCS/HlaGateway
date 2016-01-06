package nl.tno.hla.swagger

/**
 * Represents a basic data type as defined in the FOM.
 */
case class BasicData(basicData: nl.tno.hla.fom.BasicData) extends PrimitiveDataType {
  lazy val name           = basicData.name
  lazy val size           = basicData.size
  lazy val interpretation = basicData.interpretation
  lazy val endian         = basicData.endian
  lazy val encoding       = basicData.encoding
  
  lazy val dataType = name match {
    case "RPRunsignedInteger8BE"  => "integer"
    case "RPRunsignedInteger16BE" => "integer"
    case "RPRunsignedInteger32BE" => "integer"
    case "RPRunsignedInteger64BE" => "integer"
    case default => "UNKNOWN Basic data type: " + name
  }
    
  lazy val format = name match {
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