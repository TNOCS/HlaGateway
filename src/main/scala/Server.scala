package main.scala

import main.fom.FomWrapper

object Server {
  def main(args: Array[String]): Unit = {
    println("Load FOM and extract elements!")
    val cwd = System.getProperty("user.dir")
    println("Working directory: " + cwd)
    
    val fomFile = "RPR_FOM_v2.0_draft20_1516-2010.xml"
    val fom = new FomWrapper()
    fom.load(fomFile)
    fom.load("Unmanned_v1_2.xml")

    val creator = new SwaggerFileGenerator()
    creator.create(fom, "localhost:8080")
    creator.save("swagger.yaml")
  }
}