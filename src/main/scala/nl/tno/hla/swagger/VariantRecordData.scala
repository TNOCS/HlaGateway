package nl.tno.hla.swagger

/**
 * Class for mapping the alternatives in VariantRecordData
 */

case class Alternative(alternative: nl.tno.hla.fom.Alternative) {
  lazy private val name       = alternative.name
  lazy private val dataType   = alternative.dataType
  lazy private val enumerator = alternative.enumerator
  lazy private val semantics  = alternative.semantics
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

case class VariantRecordData(variantRecord: nl.tno.hla.fom.VariantRecordData) extends ObjectDataType {
  lazy private val name         = variantRecord.name
  /** 
   *  The discriminant specifies which of the alternatives is valid
   */
  lazy private val discriminant = variantRecord.discriminant
  lazy private val dataType     = variantRecord.dataType
  lazy private val encoding     = variantRecord.encoding
  lazy private val semantics    = variantRecord.semantics
  lazy private val alternatives = variantRecord.alternatives map { a => Alternative(a) }
  
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