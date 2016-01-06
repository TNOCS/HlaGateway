package nl.tno.hla.swagger

case class ObjectClass(objectClass: nl.tno.hla.fom.ObjectClass) {
  lazy val name          = objectClass.name
  lazy val sharing       = objectClass.sharing
  lazy val semantics     = objectClass.semantics
  lazy val attributes    = objectClass.attributes map { a => Attribute(a) } 
  lazy val objectClasses = objectClass.objectClasses map { o => ObjectClass(o) }
    
  def getDescription(): String = { return semantics; }
  
  /**
   * Convert to a Swagger model.
   */
  def toSwaggerModel(primitiveDataTypes: Map[String, PrimitiveDataType], objectDataTypes: Map[String, ObjectDataType]): String = {
    var yaml = s"""  $name:
      |    type: object
      |    properties:
      |""".stripMargin
    val numberOfSpaces = 6
    attributes.foreach( attribute => {
      yaml += attribute.toSwaggerModelProperty(primitiveDataTypes, objectDataTypes, numberOfSpaces)
    })
    
    // TODO Add other object classes as reference
    objectClasses.filter(o => o.attributes.size > 0).foreach(o => {
      val spaces = " " * numberOfSpaces
      yaml += spaces + o.name + ":\n" + spaces + "  $ref: '#/definitions/" + o.name + "'\n"
    })

    objectClasses.filter(o => o.attributes.size > 0).foreach(o => {
      yaml += o.toSwaggerModel(primitiveDataTypes, objectDataTypes)
    })
    return yaml
  }  
}