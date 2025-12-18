import java.util.*;
// Strategy Design Pattern
interface NotificationStrategy{
    void send(String username,String text);
}
class Email implements NotificationStrategy{
    public void send(String username,String text){
        System.out.println("Email to "+ username+ " "+ text);
    }
}
class SMS implements NotificationStrategy{
    public void send(String username,String text){
        System.out.println("SMS to "+ username+ " "+ text);
    }
}

// User Observer Design pattern for Observer
interface Observer{
    void update(String text);
}
class User implements Observer{
    private String name;
    private NotificationStrategy ns;
    User(String name,NotificationStrategy ns){
        this.name=name;
        this.ns=ns;
    }
    public void update(String text){
        ns.send(name,text);
    }
}
// Singleton + Observable for manager
class NotificationManager{
    private static volatile NotificationManager instance=null;
    private List<Observer> li=new ArrayList<>();
    private String content="";
    private NotificationManager(){}
    
    public static NotificationManager getInstance(){
        if(instance==null){
            synchronized (NotificationManager.class){
                if(instance==null){
                    instance=new NotificationManager();
                }
            }
        }
        return instance;
    }
    public void addUser(Observer o){
        if(!li.contains(o)){
            li.add(o);
        }
    }
    public void removeUser(Observer o){
        if(li.contains(o)){
            li.remove(o);
        }
    }
    public void notifyUsers(String data){
        for(Observer o:li){
            o.update(data);
        }
    }
    public void addContent(String data){
        this.content=data;
        notifyUsers(content);
    }
    
}

class Main {
    public static void main(String[] args) {
        NotificationManager manager = NotificationManager.getInstance();
        Observer u1 = new User("Soumesh", new Email());
        Observer u2 = new User("Aayu", new SMS());
        manager.addUser(u1);
        manager.addUser(u2);
        manager.notifyUsers("You are selected for SDE-1 🎉");

    }
}
