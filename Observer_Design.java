
import java.util.*;
interface Observer{
    void update();
}
interface Observable{
    void addUser(Observer o);
    void removeUser(Observer o);
    void notifyUser();
}
class Youtube implements Observable{
    private String name;
    private String content="";
    Youtube(String name){
        this.name=name;
    }
    List<Observer> obj=new ArrayList<>();
    public void addUser(Observer o){
        if(!obj.contains(o)){
            obj.add(o);
        }
    }
    public void removeUser(Observer o){
        if(obj.contains(o)){
            obj.remove(o);
        }
    }
    public void notifyUser(){
        for(Observer o:obj){
            o.update();
        }
    }
    public void addContent(String data){
        this.content=data;
        notifyUser();
    }
    public String getContent(){
        return "For you: "+name+ " "+content;
    }
}
class User implements Observer{
    private Youtube youtube;
    private String name;
    User(Youtube youtube,String name){
        this.youtube=youtube;
        this.name=name;
    }
    public void update(){
        String data=youtube.getContent();
        System.out.println("Hi "+ name+"!! " + "Youtube. "+data);
    }
}
class Main {
    public static void main(String[] args) {
        Youtube y1=new Youtube("LLD HUB");
        Observer p1=new User(y1,"Soumesh");
        Observer p2=new User(y1,"Aayu");
        
        y1.addUser(p1);
        y1.addUser(p2);
        y1.addContent("DSA Segment Tree Series!!");
        
        y1.removeUser(p1);
        y1.addContent("DSA DP Series!!");
        
    }
}
