package nl.tno.hla.fom

import scala.xml._

class FomWrapper() { //(fomFile: String) {
  var fomObjects: Seq[ObjectClass] = null
  var name = ""
  var description = ""
  
  var primitiveDataTypes: Map[String, PrimitiveDataType] = null
  var objectDataTypes: Map[String, ObjectDataType] = null
    
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