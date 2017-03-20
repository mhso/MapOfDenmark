package dk.itu.n.danmarkskort;

public class TimerUtil{
    private long onTime = 0;
    private long offTime = 0;
    private long elapsedSeconds = 0;
    private long elapsedMilliseconds = 0;
    private long elapsedNanoseconds = 0;
    private long hours = 0;
    private long minutes = 0;
    private long seconds = 0;
    private long milliseconds = 0;
    private long nanoseconds = 0;
    private String nameHour, nameHours, nameMinute, nameMinutes, nameSecond, nameSeconds, nameMillisecond, nameMilliseconds, nameNanosecond, nameNanoseconds;
    
    public TimerUtil(){
        initDescriptionNames();
    }
    
    public TimerUtil(long timeInNanoseconds){
        initDescriptionNames();
        onTime  = 0;
        if(timeInNanoseconds >0) {
            offTime = timeInNanoseconds;
        }
        splitTimeParts();
    }
    
    private void initDescriptionNames(){
        nameHour = "Hour";
        nameHours = "Hours";
        nameMinute = "Minute";
        nameMinutes = "Minutes";
        nameSecond = "Second";
        nameSeconds = "Seconds";
        nameMillisecond = "Millisecond";
        nameMilliseconds = "Milliseconds";
        nameNanosecond  = "Nanosecond";
        nameNanoseconds = "Nanoseconds";
    }
    
    public void on(){
        onTime = System.nanoTime();
    }
    
    public void off(){
        offTime = System.nanoTime();
        splitTimeParts();
    }
    
    public long elapsedSeconds(){
        return elapsedSeconds;
    }
    
    public long elapsedMilliseconds(){
        return elapsedMilliseconds;
    }
    
    public long elapsedNanoseconds(){
        return elapsedNanoseconds;
    }
    
    private void splitTimeParts(){
        final long HOUR_TAG =  3600000000000L;
        final long MINUTE_TAG =  60000000000L; 
        final long SECOND_TAG =  1000000000L; 
        final long MILLISECOND_TAG =  1000000L;
        
        elapsedNanoseconds = offTime-onTime;
        elapsedMilliseconds = (offTime-onTime)/MILLISECOND_TAG;
        elapsedSeconds = (offTime-onTime)/SECOND_TAG;
        long splitElapsedTime = elapsedNanoseconds;
        
        if(splitElapsedTime >= HOUR_TAG){
            hours = splitElapsedTime/HOUR_TAG;
            splitElapsedTime = splitElapsedTime - hours*HOUR_TAG;
        }
        
        if(splitElapsedTime >= MINUTE_TAG){
            minutes = splitElapsedTime/MINUTE_TAG;
            splitElapsedTime = splitElapsedTime - minutes*MINUTE_TAG;
        }
        
        if(splitElapsedTime >= SECOND_TAG){
            seconds = splitElapsedTime/SECOND_TAG;
            splitElapsedTime = splitElapsedTime - seconds*SECOND_TAG;
        }
        
        if(splitElapsedTime >= MILLISECOND_TAG){
            milliseconds = splitElapsedTime/MILLISECOND_TAG;
            splitElapsedTime = splitElapsedTime - milliseconds*MILLISECOND_TAG;
        }
        
        if(splitElapsedTime > 0){
            nanoseconds = splitElapsedTime;
        }
    }
    
    private int floorLongToInt(long value){
        return (int)Math.floor(value);
    }
    
    public int getHours(){
        return floorLongToInt(hours);
    }
    
    public int getMinutes(){
        return floorLongToInt(minutes);
    }
    
    public int getSeconds(){
        return floorLongToInt(seconds);
    }
    
    public int getMilliseconds(){
        return floorLongToInt(milliseconds);
    }
    
    public int getNanoseconds(){
        return floorLongToInt(milliseconds);
    }
    
    public String getHoursNamed(){
        int value = floorLongToInt(hours);
        String str = value + " ";
        if(value == 1) {
            str += nameHour;
        } else {
            str += nameHours;
        }
        return str;
    }
    
    public String getMinutesNamed(){
        int value = floorLongToInt(minutes);
        String str = "";
        if(value < 10){str = "0";}
        str = value + " ";
        if(value == 1) {
            str += nameMinute;
        } else {
            str += nameMinutes;
        }
        return str;
    }
    
    public String getSecondsNamed(){
        int value = floorLongToInt(seconds);
        String str = "";
        if(value < 10){str = "0";}
        str = value + " ";
        if(value == 1) {
            str += nameSecond;
        } else {
            str += nameSeconds;
        }
        return str;
    }
    
    public String getMillisecondsNamed(){
        int value = floorLongToInt(milliseconds);
        String str = "";
        if(value < 10){str = "00";}
        if(value >= 10 && value < 100){str = "0";}
        str = value + " ";
        if(value == 1) {
            str += nameMillisecond;
        } else {
            str += nameMilliseconds;
        }
        return str;
    }
    
    public String getNanosecondsNamed(){
        int value = floorLongToInt(nanoseconds);
        String str = "";
        if(value < 10){str = "00000";}
        if(value >= 10 && value < 100){str = "0000";}
        if(value >= 100 && value < 1000){str = "000";}
        if(value >= 1000 && value < 10000){str = "00";}
        if(value >= 10000 && value < 100000){str = "0";}
        str = value + " ";
        if(value == 1) {
            str += nameNanosecond;
        } else {
            str += nameNanoseconds;
        }
        return str;
    }
    
    public void setDescriptions(String nameHour, String nameHours, String nameMinute, String nameMinutes, String nameSecond, String nameSeconds, String nameMillisecond, String nameMilliseconds, String nameNanosecond, String nameNanoseconds){
        this.nameHour = nameHour;
        this.nameHours = nameHours;
        this.nameMinute = nameMinute;
        this.nameMinutes = nameMinutes;
        this.nameSecond = nameSecond;
        this.nameSeconds = nameSeconds;
        this.nameMillisecond = nameMillisecond;
        this.nameMilliseconds = nameMilliseconds;
        this.nameNanosecond = nameNanosecond;
        this.nameNanoseconds = nameNanoseconds;  
    }
    
    public String toString(){
        return getHoursNamed()+" "+getMinutesNamed()+" "+getSecondsNamed()+" "+getMillisecondsNamed()+ " "+getNanosecondsNamed();
    }
}