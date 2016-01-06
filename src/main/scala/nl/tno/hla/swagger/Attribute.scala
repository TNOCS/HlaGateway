package nl.tno.hla.swagger

/**
 * Represents a simple data type as defined in the FOM.
 */
case class Attribute(attribute: nl.tno.hla.fom.Attribute) {
  lazy val name            = attribute.name
  lazy val dataType        = attribute.dataType
  lazy val updateType      = attribute.updateType
  lazy val updateCondition = attribute.updateCondition
  lazy val ownership       = attribute.ownership
  lazy val sharing         = attribute.sharing
  lazy val transportation  = attribute.transportation
  lazy val order           = attribute.order
  lazy val semantics       = attribute.semantics

  def getDescription(): String = { return semantics; }

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