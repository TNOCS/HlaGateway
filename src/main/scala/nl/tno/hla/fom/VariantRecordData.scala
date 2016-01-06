package nl.tno.hla.fom

import scala.xml._

/**
 * Class for mapping the alternatives in VariantRecordData
 */

case class Alternative(alternative: Node) {
  lazy val name       = (alternative \ "name"      ).text
  lazy val dataType   = (alternative \ "dataType"  ).text
  lazy val enumerator = (alternative \ "enumerator").text
  lazy val semantics  = (alternative \ "semantics" ).text + s" Note that this field is only valid of the discriminant is <code>$enumerator</code>."
}

case class VariantRecordData(variantRecord: Node) extends ObjectDataType {
  lazy val name         = variantRecord \ "name" text
  /** 
   *  The discriminant specifies which of the alternatives is valid
   */
  lazy val discriminant = variantRecord \ "discriminant" text
  lazy val dataType     = variantRecord \ "dataType" text
  lazy val encoding     = variantRecord \ "encoding" text
  lazy val semantics    = (variantRecord \ "semantics").text.filter(_ != '\n')
  lazy val alternatives = variantRecord \ "alternative" map { a => Alternative(a) }  
}