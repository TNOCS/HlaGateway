package main.fom

import scala.xml._

class FomWrapper() { //(fomFile: String) {
  var fomObjects: Seq[ObjectClass] = null
  var name = ""
  var description = ""
  
  var primitiveDataTypes: Map[String, PrimitiveDataType] = null
  var objectDataTypes: Map[String, ObjectDataType] = null
  
//  private val fom        = XML.load(new java.io.InputStreamReader(new java.io.FileInputStream(fomFile), "UTF-8"))
//  var fomObjects: Seq[ObjectClass] = fom \ "objects" \ "objectClass" \ "objectClass" map { obj => ObjectClass(obj) }
//  val name: String = fom \ "modelIdentification" \ "name" text 
//  val description: String = fom \ "modelIdentification" \ "description" text   
//  
//  private val fomDataTypes           = fom \ "dataTypes"
//  private val basicDataTypes         = fomDataTypes \ "basicDataRepresentations" \ "basicData"         map { bd => ((bd \ "name").text -> BasicData(bd))         } toMap
//  private val simpleDataTypes        = fomDataTypes \ "simpleDataTypes"          \ "simpleData"        map { sd => ((sd \ "name").text -> SimpleData(sd))        } toMap
//  private val enumeratedDataTypes    = fomDataTypes \ "enumeratedDataTypes"      \ "enumeratedData"    map { ed => ((ed \ "name").text -> EnumeratedData(ed))    } toMap
//  private val arrayDataTypes         = fomDataTypes \ "arrayDataTypes"           \ "arrayData"         map { ad => ((ad \ "name").text -> ArrayData(ad))         } toMap
//  private val fixedRecordDataTypes   = fomDataTypes \ "fixedRecordDataTypes"     \ "fixedRecordData"   map { fr => ((fr \ "name").text -> FixedRecordData(fr))   } toMap
//  private val variantRecordDataTypes = fomDataTypes \ "variantRecordDataTypes"   \ "variantRecordData" map { vr => ((vr \ "name").text -> VariantRecordData(vr)) } toMap
//
//  /**
//   * Primitive data types map directly to a primitive type such as string or number.
//   */
//  val primitiveDataTypes: Map[String, PrimitiveDataType] = basicDataTypes ++ simpleDataTypes ++ enumeratedDataTypes
//  
//  /**
//   * Data types that require an object to represent properly, since they contain properties themselves.
//   */
//  val objectDataTypes: Map[String, ObjectDataType] = arrayDataTypes ++ fixedRecordDataTypes ++ variantRecordDataTypes
  
  def load(fomFile: String): Unit = {
    val fom = XML.load(new java.io.InputStreamReader(new java.io.FileInputStream(fomFile), "UTF-8"))
    val myFomObjects = fom \ "objects" \ "objectClass" \ "objectClass" map { obj => ObjectClass(obj) }
    val myName = fom \ "modelIdentification" \ "name" text 
    val myDescription = fom \ "modelIdentification" \ "description" text   
 
    val fomDataTypes           = fom \ "dataTypes"
    val basicDataTypes         = fomDataTypes \ "basicDataRepresentations" \ "basicData"         map { bd => ((bd \ "name").text -> BasicData(bd))         } toMap
    val simpleDataTypes        = fomDataTypes \ "simpleDataTypes"          \ "simpleData"        map { sd => ((sd \ "name").text -> SimpleData(sd))        } toMap
    val enumeratedDataTypes    = fomDataTypes \ "enumeratedDataTypes"      \ "enumeratedData"    map { ed => ((ed \ "name").text -> EnumeratedData(ed))    } toMap
    val arrayDataTypes         = fomDataTypes \ "arrayDataTypes"           \ "arrayData"         map { ad => ((ad \ "name").text -> ArrayData(ad))         } toMap
    val fixedRecordDataTypes   = fomDataTypes \ "fixedRecordDataTypes"     \ "fixedRecordData"   map { fr => ((fr \ "name").text -> FixedRecordData(fr))   } toMap
    val variantRecordDataTypes = fomDataTypes \ "variantRecordDataTypes"   \ "variantRecordData" map { vr => ((vr \ "name").text -> VariantRecordData(vr)) } toMap

    val myPrimitiveDataTypes = basicDataTypes ++ simpleDataTypes ++ enumeratedDataTypes
    val myObjectDataTypes: Map[String, ObjectDataType] = arrayDataTypes ++ fixedRecordDataTypes ++ variantRecordDataTypes
    
    this.name = myName
    this.description = myDescription
    
    this.fomObjects = if (this.fomObjects == null) myFomObjects else this.fomObjects ++ myFomObjects
    this.primitiveDataTypes = if (this.primitiveDataTypes == null) myPrimitiveDataTypes else this.primitiveDataTypes ++ myPrimitiveDataTypes
    this.objectDataTypes = if (this.objectDataTypes == null) myObjectDataTypes else this.objectDataTypes ++ myObjectDataTypes
  }
}