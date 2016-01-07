package nl.tno.hla.gateway

import nl.tno.hla.fom.FomWrapper
import nl.tno.hla.swagger.SwaggerGenerator

object Generator {
  type OptionMap = Map[Symbol, Any]

  def main(args: Array[String]) {
    val options = parseOptions(args)
    //println(options)

    val inputFiles = getInputFilesOrExit(options)
    println("Processing files: " + inputFiles)    

    val fom = new FomWrapper()
    inputFiles foreach(f => fom.load(f))

    val host = options('host).asInstanceOf[String]
    val useObjects =  options('useObject).asInstanceOf[List[String]]
    val useInteractions = options('useInteraction).asInstanceOf[List[String]]
    
    val creator = new SwaggerGenerator(host, useObjects, useInteractions)
    creator.create(new nl.tno.hla.swagger.FomWrapper(fom))
    creator.save("swagger.yaml")
  }
  
  private def getInputFilesOrExit(options: OptionMap): List[String] = {
    val inputFiles = options('infile).asInstanceOf[List[String]]
    if (inputFiles.size > 0) return inputFiles
    // Try the current working directory
    val cwd = System.getProperty("user.dir")
    val xmlFiles = new java.io.File(cwd).listFiles.filter(_.getName.endsWith(".xml")).map(_.getName).toList
    if (xmlFiles.size > 0) return xmlFiles
    println(s"No input files found in folder {cwd}. Exiting.")    
    sys.exit(1)    
  }
  
  private def parseOptions(args: Array[String]): OptionMap = {
    def nextOption(map : OptionMap, list: List[String]) : OptionMap = {
      def isSwitch(s : String) = (s(0) == '-')
      val infiles = map('infile).asInstanceOf[List[String]]
      val objects = map('useObject).asInstanceOf[List[String]]
      val interac = map('useInteraction).asInstanceOf[List[String]]
      list match {
        case Nil => map
        case "--host" :: value :: tail => nextOption(map ++ Map('host -> value), tail)
        case "--useObject" :: value :: tail => nextOption(map ++ Map('useObject -> (value :: objects)), tail)
        case "--useInteraction" :: value :: tail => nextOption(map ++ Map('useInteraction -> (value :: interac)), tail)
        case str :: opt2 :: tail if isSwitch(opt2) => nextOption(map ++ Map('infile -> (str :: infiles)), list.tail)
        case str :: Nil  => nextOption(map ++ Map('infile -> (str :: infiles)), list.tail)
        case str :: tail if !isSwitch(str) => nextOption(map ++ Map('infile -> (str :: infiles)), list.tail)
        case option :: tail => println("Unknown option: " + option)
                               println
                               printHelpMessage
                               sys.exit(1)
      }
    }
    val options = nextOption(Map(
        'host -> "localhost:8080",
        'infile -> List[String](), 
        'useObject -> List[String](), 
        'useInteraction -> List[String]()
        ), args.toList)
    return options
  }
  
  /**
   * Print a help message
   */
  private def printHelpMessage() {
    val message = """Usage: generator inputFiles [--useObject AnObjectName] [--useInteraction AnOInteractionName]
     |
     |Where:
     |* inputFiles:       A list of XML files that together make up the FOM (default: all XML files in folder)
     |* --host:           Optionally, specify the host and port to use (default: localhost:8080). 
     |* --useObject:      Optionally, specify the ObjectClass to use (default: process all). 
     |                    Note that each object must be specified separately.
     |* --useInteraction: Optionally, specify the InteractionClass to use (default: process all). 
     |                    Note that each object must be specified separately.   
     |""".stripMargin
     println(message)
  }
  
}
