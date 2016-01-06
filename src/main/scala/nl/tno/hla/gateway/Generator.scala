package nl.tno.hla.gateway

import nl.tno.hla.fom.FomWrapper
import nl.tno.hla.swagger.Generator

object Generator {
  def main(args: Array[String]): Unit = {
    println("Load FOM and extract elements!")
    val cwd = System.getProperty("user.dir")
    println("Working directory: " + cwd)
    
    val fomFile = "RPR_FOM_v2.0_draft20_1516-2010.xml"
    val fom = new FomWrapper()
    fom.load(fomFile)
    fom.load("Unmanned_v1_2.xml")

    val creator = new Generator()
    creator.create(new nl.tno.hla.swagger.FomWrapper(fom), "localhost:8080")
    creator.save("swagger.yaml")
  }
  
}
