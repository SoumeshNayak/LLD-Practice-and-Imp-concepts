import java.util.*;

class Indegrient{
    Map<String, Integer> map=new HashMap<>();
    
    public void addIndeg(String name, int cnt){
        map.put(name,map.getOrDefault(name,0)+cnt);
    }
    
    public void removeIndeg(String name, int q){
        if(map.containsKey(name)) {
            map.put(name,map.get(name)-q);
        }else{
            System.out.println(name+" not found");
        }
    }
    public Map<String, Integer> getAllIndeg(){
        return map;
    }

}

class Recipi{

private Map<String, Integer> list=new HashMap();

    public void addItem(String name, int cnt){
        list.put(name,list.getOrDefault(name,0)+cnt);
    }

    public Map<String, Integer> getAllItems(){
        return list;
    }
}

abstract class Coffee{

    private String name;
    private double price;

    public Coffee(String name,double price) {
        this.name=name;
        this.price=price;
    }
    public abstract Recipi getRecipi();
    public String getName(){
        return this.name;
    }
    public double getPrice(){
        return this.price;
    }
}

class Cappuccino extends Coffee{

private Recipi recipi;

    public Cappuccino(){
        super("Cappuccino",80.0);
        recipi=new Recipi();
    
        recipi.addItem("Milk", 1);
        
        recipi.addItem("Suger",3);
        
        recipi.addItem("CoffeePower",4);
    }
    public Recipi getRecipi(){
        return recipi;
    }
}

class Expresso extends Coffee{

    private Recipi recipi;

    public Expresso(){
        super("Expresso",60.0);

        recipi=new Recipi();
        
        recipi.addItem("Milk",1);
        
        recipi.addItem("Suger",2);
        
        recipi.addItem("CoffeePower",4);

    }


    public Recipi getRecipi(){
        return recipi;
    }
}



class Factory{
    public static Coffee getCoffee(String type){ 
        if(type.equalsIgnoreCase("Cappuccino")){ 
            return new Cappuccino();
        }else if(type.equalsIgnoreCase("Expresso")){
            return new Expresso();
        }else{
            throw new IllegalArgumentException("Invalid coffee type");
        }
    }
}


class CoffeeMachine{

    private Indegrient indeg;
    private PaymentStrategy ps;
    CoffeeMachine(){
        indeg=new Indegrient();
        ps=new PaymentStrategy();
    }
    
    
    
    public void addIndeg(String name, int q){
        indeg.addIndeg(name,q);
    }

    public boolean ifAvailble (Map<String, Integer> available, Map <String, Integer> needed) {
        for (Map. Entry<String, Integer> entry: needed.entrySet()){
        String name=entry.getKey();
        int q=entry.getValue(); 
        if(available.containsKey(name) && available .getOrDefault(name,0)>=q){ 
            continue;
        }
        else{
            System.out.println("Service not availble"); 
            return false; 
            }
        }
        return true;
    }
public synchronized  void deductAvailable (Map<String, Integer> available, Map<String,Integer> needed) {
    for (Map. Entry<String, Integer> entry: needed.entrySet()){
        String name=entry.getKey();
        int q=entry.getValue();
        available.put(name,available.getOrDefault(name,0)-q);
    }
}

    public Coffee chooseCoffeeAndGet(String name) {
        Coffee c1=Factory.getCoffee(name);
        Recipi r1=c1.getRecipi();
        Map<String, Integer> available=indeg.getAllIndeg();
        Map<String, Integer> needed=r1.getAllItems();
        if(ifAvailble(available, needed)) {
            // Payment logic
            Scanner sc=new Scanner(System.in);
            System.out.println("Enter the payment type 1> UPI 2>CraditCard");
            String type = sc.nextLine(); 
            System.out.println("Enter the cradential to pay");
            String id = sc.nextLine(); 
            ps.setPayment(type,id);
            ps.pay(c1.getPrice());
            // Payment logic ends
            deductAvailable(available, needed);
            return c1;
        }
        System.out.println("Service not available");
        return null;
    }
    public void getAllIndeg() {
        for(Map. Entry<String, Integer> items:indeg.getAllIndeg().entrySet()){
            String name=items.getKey();
            int q=items.getValue();
            System.out.println(name + "=>" + q);
        }
    }
}

abstract class Payment{
   public abstract void pay(double amount); 
}
class UPI extends Payment{
    private String upiId;
    public UPI(String upiId){
        this.upiId=upiId;
    }
    public void pay(double amount){
        System.out.println("Payment done successfully via UPI!!");
    }
}
class CraditCard extends Payment{
    private String password;
    public CraditCard(String password){
        this.password=password;
    }
    public void pay(double amount){
        System.out.println("Payment done successfully via Card!!");
    }
}
class PaymentStrategy{
    private Payment payment;
    public void setPayment(String type,String id){
        if(type=="UPI"){
            payment=new UPI(id);
        }else{
            payment=new CraditCard(id);
        }
    }
    public  void pay(double amount){
        payment.pay(amount);
    }
}


class Main {

public static void main(String[] args) {

// For coffeeMachine

    CoffeeMachine machine=new CoffeeMachine();
    
    machine.addIndeg("Milk",5);
    
    machine.addIndeg("Suger", 10);
    
    machine.addIndeg("CoffeePower",20);
    
    Coffee c1=machine.chooseCoffeeAndGet("Cappuccino");
    
    if (c1 != null) {
        System.out.println("Hi here is your order " + c1.getName());
    }else{
        System.out.println("Something went wrong");
    }
    
    //Check for avilable Indegrients
    machine.getAllIndeg();

    }
}
