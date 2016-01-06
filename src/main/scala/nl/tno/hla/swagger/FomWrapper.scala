package nl.tno.hla.swagger

class FomWrapper(fom: nl.tno.hla.fom.FomWrapper) { 
  val name        = fom.name
  val description = fom.description
  
  val fomObjects         = fom.fomObjects map { o => ObjectClass(o) }
  
  val primitiveDataTypes: Map[String, PrimitiveDataType] = fom.primitiveDataTypes map {
    case (key, value) => {
      value match {
        case b: nl.tno.hla.fom.BasicData      => ( key -> BasicData(b))
        case s: nl.tno.hla.fom.SimpleData     => ( key -> SimpleData(s))
        case e: nl.tno.hla.fom.EnumeratedData => ( key -> EnumeratedData(e))
      }
    }
  } toMap

  val objectDataTypes: Map[String, ObjectDataType] = fom.objectDataTypes map {
    case (key, value) => {
      value match {
        case b: nl.tno.hla.fom.ArrayData         => ( key -> ArrayData(b))
        case f: nl.tno.hla.fom.FixedRecordData   => ( key -> FixedRecordData(f))
        case v: nl.tno.hla.fom.VariantRecordData => ( key -> VariantRecordData(v))
      }
    }
  } toMap

}