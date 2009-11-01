object FileSync {
  def dir(dirname: String) = {
    val dir = new java.io.File(dirname)
    dir.exists && dir.isDirectory
  } 
  
  def main(args : Array[String]) : Unit = {
    if (args.length < 2) {
      println("FileSync [fromdir] [todir]")
    } else if (!dir(args(0))) {
      println("Directory "+args(0)+" not found")
    } else if (!dir(args(1))) {
      println("Directory "+args(1)+" not found")
    } else {
    	new filesync.FileSync(args(0), args(1)).doSync
    }    
  }
}
