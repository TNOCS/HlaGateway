package nl.tno.hla.swagger

import java.io._
import scala.xml._
import nl.tno.hla.fom._

/**
 * Generates a Swagger YAML file, which can be used to generate a REST server in Scalatra.
 */
class Generator {
  private var yaml = "";
  
  def create(fom: FomWrapper, host: String = "localhost:8080") {
    createHeader(fom, host)
    createRoutes(fom)
    createModels(fom)
  }
  
  /** 
   *  Create header for Swagger file
   */
  private def createHeader(fom: FomWrapper, host: String) { 
    val title = fom.name
    val desc  = fom.description
    // http://docs.scala-lang.org/overviews/core/string-interpolation.html
    yaml += s"""swagger: '2.0'
      |info:
      |  title: $title
      |  description: $desc
      |  version: 1.0.0
      |host: $host
      |schemes:
      |  - http
      |basePath: /v1
      |produces:
      |  - application/json
      |  - application/xml
      |""".stripMargin
  }
  
  /**
   * Create the REST routes
   */
  private def createRoutes(fom: FomWrapper) {
    yaml += "paths:\n"
    
    // Create all routes (get, objects and objects\id)
    fom.fomObjects.foreach(obj => {
      val ref     = "$ref"
      val name    = obj.name
      val path    = name.toLowerCase.replaceAll( "y$", "ie") + "s"
      val desc    = obj.getDescription()
      val summary = "Get all" + name.replaceAll( "([A-Z])", " $1" ) + " items.";

      yaml += s"""  /objects/$path:
        |    get:
        |      summary: $summary
        |      description: $desc
        |      responses:
        |        '200':
        |          description: An array of $name items
        |          schema:
        |            type: array
        |            items:
        |              $ref: '#/definitions/$name'
        |        default:
        |          description: Unexpected error
        |          schema:
        |            $ref: '#/definitions/Error'
        |""".stripMargin
    })
  }
  
  /**
   * Create the Swagger Models, i.e. a description of the objects that are returned
   */
  private def createModels(fom: FomWrapper) {    
    yaml += "definitions:" + "\n"
    fom.fomObjects.foreach(obj => {
      yaml += obj.toSwaggerModel(fom.primitiveDataTypes, fom.objectDataTypes)
    })
    
    // Create all the reference models
    var keepProcessing = true // A model may reference other models, so we have to keep processing until no changes are made
    while(keepProcessing) {
      keepProcessing = false
      fom.objectDataTypes.values.foreach(obj => {
        if (obj.isReferenced && !obj.isDefined) {
          obj.isDefined = true
          keepProcessing = true
          yaml += obj.toSwaggerModel(fom.primitiveDataTypes, fom.objectDataTypes)
        }
      })
    }
    
    // Create error models
    yaml += """  Error:
      |    type: object
      |    properties:
      |      code:
      |        type: integer
      |        format: int32
      |      message:
      |        type: string
      |      fields:
      |        type: string
      |""".stripMargin
  }
  
  /**
   * Save the created YAML to file
   */
  def save(filename: String) {
    val pw = new PrintWriter(new File(filename))   
    pw.write(yaml)
    pw.close
  }
  
}