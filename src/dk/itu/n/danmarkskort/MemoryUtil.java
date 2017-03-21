package dk.itu.n.danmarkskort;

public class MemoryUtil {
	    private long onMemoryLog = 0;
	    private long offMemoryLog = 0;
	    private long bytes = 0;
	    
	    public MemoryUtil(){
	    }
	    
	    public MemoryUtil(long valueInBytes){
	        onMemoryLog  = 0;
	        if(valueInBytes >0) {
	            offMemoryLog = valueInBytes;
	            bytes = offMemoryLog - onMemoryLog;
	        }
	    }
	    
	    public void on(){
	        onMemoryLog = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	    }
	    
	    public void off(){
	        offMemoryLog = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	        bytes = offMemoryLog - onMemoryLog;
	    }
	    
	    public double differenceGigabytes(){
	        return bytes / (Math.pow(1000, 3));
	    }
	    
	    public double differenceMegabytes(){
	        return bytes / (Math.pow(1000, 2));
	    }
	    
	    public double differenceKilobytes(){
	        return bytes / 1000d;
	    }
	    
	    public long differenceBytes(){
	        return bytes;
	    }
	    
	    // Source: http://programming.guide/java/formatting-byte-size-to-human-readable-format.html
	    public String humanReadableByteCount(boolean si) {
	        int unit = si ? 1000 : 1024;
	        if (bytes < unit) return bytes + " B";
	        int exp = (int) (Math.log(bytes) / Math.log(unit));
	        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	    }
}
