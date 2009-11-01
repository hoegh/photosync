package filesync

import java.util.Calendar
import Calendar.{MONTH, YEAR}
import java.io.File
import File.separator

class FileSync(here: String, there: String) {

  def filesHere = (new File(here)).listFiles
  
  def getFileDate(file: java.io.File): FileDate = getFileDate(file.lastModified)
  
  def getFileDate(filetime: Long) = {
    val cal = Calendar.getInstance 
    cal.setTimeInMillis(filetime)
    val year = cal.get(YEAR) - 2000
    val month = cal.get(MONTH) + 1
    
    FileDate(year, month)
  }
  
  def prefix(date: FileDate) = date.yearStr+date.monthStr+"-"
  
  def otherfile(file: File): File = otherfile(file, getFileDate(file))
  
  def otherfile(file: File, filedate: FileDate) = {
    val path = new File(List(there, filedate.yearStr, filedate.monthStr) mkString separator )
   
    new File(path, prefix(filedate)+file.getName)
  }
  
  def copy(from : File, to: File): Unit = {
    val filename = to getName;
    if (to exists) {
	  println("Skipping "+filename) 
	} else {
      val path = to getParentFile;
      if (!(path exists)) {
    	path mkdirs
      }
   
      val fromChannel = new java.io.FileInputStream(from).getChannel();
      val toChannel = new java.io.FileOutputStream(to).getChannel();
    
      toChannel.transferFrom(fromChannel, 0, fromChannel.size());
    
      toChannel.close();
      fromChannel.close();
      
      println("Wrote "+filename)
   }
  }

  def doSync(getfiles: ()=>Array[File], otherfile: (File) => File, copy: (File, File)=>Unit): Unit = {
	for(file <- getfiles()) 
      copy(file, otherfile(file))
  } 
  
  def doSync: Unit = doSync(filesHere _, otherfile, copy)
}

case class FileDate(year: Int, month: Int) {
  def tostr(i: Int) = "%02d" format i
  
  def yearStr = tostr(year)
  def monthStr = tostr(month)
}
