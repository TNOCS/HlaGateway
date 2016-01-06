package nl.tno.hla.swagger

class PrimitiveDataType {
  def toSwaggerParameter(propName: String, summary: String, numberOfSpaces: Int): String = { return "" }

  def toSwaggerParameter(name: String, summary: String, dataType: String, format: String, desc: String, numberOfSpaces: Int, min: Int = 0, max: Int = 0): String = { 
    val spaces = " " * (numberOfSpaces - 1)
    if (max > min) {
    return s"""$spaces - name: $name
      |$spaces summary: $summary
      |$spaces in: query
      |$spaces type: $dataType
      |$spaces format: $format
      |$spaces minimum: $min
      |$spaces maximum: $max
      |$spaces description: $desc
      |""".stripMargin      
    } else {
    return s"""$spaces - name: $name
      |$spaces summary: $summary
      |$spaces in: query
      |$spaces type: $dataType
      |$spaces format: $format
      |$spaces description: $desc
      |""".stripMargin      
    }
  }
  
  def toSwaggerModelProperty(propName: String, summary: String, numberOfSpaces: Int): String = { return "" }

  def toSwaggerModelProperty(name: String, summary: String, dataType: String, format: String, desc: String, numberOfSpaces: Int, min: Int = 0, max: Int = 0): String = {
    val spaces = " " * (numberOfSpaces - 1)
    if (max > min) {
      return s"""$spaces $name:
        |$spaces   type: $dataType
        |$spaces   format: $format
        |$spaces   minimum: $min
        |$spaces   maximum: $max
        |$spaces   description: $desc
        |""".stripMargin
    } else {
      return s"""$spaces $name:
        |$spaces   type: $dataType
        |$spaces   format: $format
        |$spaces   description: $desc
        |""".stripMargin
    }    
  }

  def toSwaggerItemProperty(numberOfSpaces: Int): String = { return "" }

  def toSwaggerItemProperty(dataType: String, format: String, desc: String, numberOfSpaces: Int, min: Int = 0, max: Int = 0): String = {
    val spaces = " " * (numberOfSpaces - 1)
    if (max > min) {
      return s"""|$spaces type: $dataType
        |$spaces format: $format
        |$spaces minimum: $min
        |$spaces maximum: $max
        |""".stripMargin
    } else {
      return s"""|$spaces type: $dataType
        |$spaces format: $format
        |""".stripMargin
    }    
  }
}