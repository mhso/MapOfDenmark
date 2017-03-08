package dk.itu.n.danmarkskort;

public class Main {
    
    public static String  appName    = "DuckMap";
    public static String  appVersion = "0.1";
    public static boolean debug      = true;
    
    public static void main(String[] args) {
        start();
        main();
        end();
    }
    
    public static void start() {
        log("Program has started.");
    }
    
    public static void main() {
        
    }
    
    public static void end() {
        log("Program has ended.");
    }
    
    public static void log(String text) {
        System.out.println("[" + appName + " " + appVersion + "] " + text);
    }
    
}
