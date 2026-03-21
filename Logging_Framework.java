import java.util.*;
enum LogLevel{
    INFO,WARNING,ERROR;
}
interface LogObserver{
    void update(String message,LogLevel level);
}
class ConsoleLogger implements LogObserver{
    public void update(String message,LogLevel level){
        System.out.println("["+level+"] "+message);
    }
}
class FileLogger implements  LogObserver{
    public void update(String message,LogLevel level){
        System.out.println("Writing to file: ["+level+"] "+message);
    }
}
abstract class Logger{
    private Logger nextLogger;
    private LogLevel level;
    private List<LogObserver> observers=new ArrayList<>();
    public Logger(LogLevel level){
        this.level=level;
    }
    public void setNext(Logger nextLogger){
        this.nextLogger=nextLogger;
    }
    public void addObserver(LogObserver observer){
        observers.add(observer);
    }
    public void notifyObserver(String message,LogLevel level){
        for(LogObserver observer:observers){
            observer.update(message,level);
        }
    }
    public void log(LogLevel level,String message){
        if(this.level.ordinal()<=level.ordinal()){
            write(message,level);
            notifyObserver(message,level);
        }
        if(nextLogger!=null){
            nextLogger.log(level,message);
        }
    }
     abstract void write(String message,LogLevel level);
}
class InfoLogger extends Logger{
    public InfoLogger(){
        super(LogLevel.INFO);
    }
    public void write(String message,LogLevel level){
        if(level==LogLevel.INFO){
             System.out.println("INFO handled: " + message);
        }
    }
}
class WarningLogger extends Logger{
    public WarningLogger(){
        super(LogLevel.WARNING);
    }
    public void write(String message,LogLevel level){
        if(level==LogLevel.WARNING){
             System.out.println("WARNING handled: " + message);
        }
    }
}
class ErrorLogger extends Logger{
    public ErrorLogger(){
        super(LogLevel.ERROR);
    }
    public void write(String message,LogLevel level){
        if(level==LogLevel.ERROR){
             System.out.println("ERROR handled: " + message);
        }
    }
}
class Main {
    public static void main(String[] args) {
       Logger infoLogger=new InfoLogger();
       Logger warningLogger=new WarningLogger();
       Logger errorLogger=new ErrorLogger();
       
       infoLogger.setNext(warningLogger);
       warningLogger.setNext(errorLogger);
       
       LogObserver console=new ConsoleLogger();
       LogObserver file=new FileLogger();
       
       infoLogger.addObserver(console);
       warningLogger.addObserver(console);
       warningLogger.addObserver(file);
       errorLogger.addObserver(file);
       
       infoLogger.log(LogLevel.INFO,"This is an info message");
       warningLogger.log(LogLevel.WARNING,"This is warning");
    }
}
