package filesync

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class FileSyncSpec extends Spec with ShouldMatchers{
	describe("A Filedate") {
	  val singleDigit = FileDate(5, 7)
	  val doubleDigit = FileDate(12, 13)
   
	  it("should prepend 0 to single digit values") {
	    singleDigit tostr(1) should be ("01")
	  }
   
	  it("should not change double digit values") {
	    singleDigit tostr(11) should be ("11")
	  }
   
	  it("should format years with yearstr") {
	    {singleDigit yearStr} should be ("05");
	    {doubleDigit yearStr} should be ("12")
      }
	  
	  it("should format dates with datestr") {
	    {singleDigit monthStr} should be ("07");
	    {doubleDigit monthStr} should be ("13")
	  }
	}
 
	describe("A FileSync") {
	  val fs = new FileSync("from", "to")
	  
	  describe("has a method getFileDate that")	{
        
		  it("should return year and date for a file") {
			  val cal = java.util.Calendar.getInstance
			  cal clear()
			  cal.set(2001,2,4) //month are zero-based
			  
			  fs.getFileDate(cal getTimeInMillis) should be (FileDate(1,3));
		  }
	  }
   
	  describe("has a method prefix that") {
	    it("should return a String formattet as 'YYMM-'") {
	      fs.prefix(FileDate(1,2)) should be ("0102-")
	    }
	  }
   
	  describe("can construct target filenames such that the new filename") {
	    it("has the form 'targetdir/YY/MM/YYMM-orgininalFilename'") {
	      val ps = java.io.File.separator
	      val filedate = FileDate(1,2)
	      val filename = "filename"
	      val file = fs.otherfile(new java.io.File(filename), filedate)

	      file.getPath should be ("to"+ps+"01"+ps+"02"+ps+fs.prefix(filedate)+filename)
	    }
	  }
	}
}
