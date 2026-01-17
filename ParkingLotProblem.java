import java.util.*;
// Payment Strategy
abstract class Payment{
    abstract void calculateCost(double cost);
}
class UPI extends Payment{
    private String id;
    private String password;
    UPI(String id,String password){
        this.id=id;
        this.password=password;
    }
    public void calculateCost(double cost){
        System.out.println("Processing Password");
        System.out.println("Payent done with upi id "+id+" "+ "amount "+cost);
    }
}
class CraditCard extends Payment{
    private String id;
    
    CraditCard(String id){
        this.id=id;
    }
    public void calculateCost(double cost){
        System.out.println("Processing payment..");
        System.out.println("Payent done with CraditCard id "+id+" "+ "amount "+cost);
    }
}
class PaymentStrategy{
    private Payment payment;
    public void setPaymentStrategy(Payment payment){
        this.payment=payment;
    }
    public void calculateCost(double cost){
        payment.calculateCost(cost);
    }
}
// Exit
class Exit {
    private PaymentStrategy paymentStrategy;

    public Exit(PaymentStrategy paymentStrategy){
        this.paymentStrategy = paymentStrategy;
    }

    public void processExit(Ticket ticket, int exitTime){
        int duration = exitTime - ticket.getStartTime();
        double cost = ticket.getVehicle()
                            .getCost(duration);

        paymentStrategy.calculateCost(cost);
        ticket.getParkingSpot().unPark();
    }
}
// Entrance
class Entrance{
    private ParkingLot pl;
    private int ticket_number;
    Entrance(ParkingLot pl,int ticket_number){
        this.pl=pl;
        this.ticket_number=ticket_number;
    }
    Ticket createTicket(ParkingSpot ps,Vehicle v,int StartTime){
        return new Ticket(ticket_number,ps,v,StartTime);
    }
    Ticket bookTicket(Vehicle v){
        ParkingSpot ps=pl.parkVehicle(v);
        if(ps!=null){
            System.out.println("Booking is Done at "+ps.getSpotNumber());
            return createTicket(ps,v,4);
        }
        System.out.println("Unable to book a ticket");
        return null;
    }
}

// -------------PARKING LOT
class ParkingLot{
    private List<ParkingFloor> floors=new ArrayList<>();
    public void addParkingFloor(ParkingFloor f){
        floors.add(f);
    }
    public ParkingSpot findAvailableSpots(Vehicle v){
    for(ParkingFloor pf: floors){
        ParkingSpot ps = pf.findAvailableSpots(v);
        if(ps != null){
            return ps;
        }
    }
    return null;
}
    public ParkingSpot parkVehicle(Vehicle v){
        ParkingSpot ps=findAvailableSpots(v);
        if(ps!=null){
            ps.ParkVehicle(v);
            return ps;
        }
        return null;
    }
    public void unPark(ParkingSpot ps){
        if(ps.isOcupied()){
            ps.unPark();
        }else{
         System.out.println("Already unoccupied");   
        }
    }
    
}
//----------------Parkig Floor
class ParkingFloor{
    private List<ParkingSpot> spots=new ArrayList<>();
    private int floor_number;
    ParkingFloor(int floor_number){
        this.floor_number=floor_number;
    }
    public void addParkingSpot(ParkingSpot spot){
        spots.add(spot);
    }
    public ParkingSpot findAvailableSpots(Vehicle v){
        for(ParkingSpot spot: spots){
            if(spot.canParkVehicle(v) && !spot.isOcupied()){
                
                return spot;
            }
        }
        return null;
    }
    public List<ParkingSpot> getAllSpots(){
        return spots;
    }
}
//--------------Ticket
class Ticket{
    private int ticket_number;
    private ParkingSpot ps;
    private Vehicle v;
    private int StartTime;
    Ticket(int ticket_number,ParkingSpot ps,Vehicle v,int StartTime){
        this.ticket_number=ticket_number;
        this.ps=ps;
        this.v=v;
        this.StartTime=StartTime;
    }
    public int getStartTime(){
        return StartTime;
    }
    public ParkingSpot getParkingSpot(){
        return ps;
    }
    public Vehicle getVehicle(){
        return v;
    }
}
// ----------------Parking Spot---
abstract class ParkingSpot{
    private int spotNumber;
    private boolean occupied;
    private String spotType;
    public ParkingSpot(int spotNumber,String spotType){
        this.spotNumber=spotNumber;
        this.occupied=false;
        this.spotType=spotType;
    }
    public boolean isOcupied(){
        return occupied;
    }
    public String getSpotType(){
        return spotType;
    }
    public int getSpotNumber(){
        return spotNumber;
    }
    abstract  boolean canParkVehicle(Vehicle v);
    public void ParkVehicle(Vehicle v){
        if(canParkVehicle(v) && !isOcupied()){
            this.occupied=true;
            System.out.println("Congo!,You vehicle has been parked to "+ getSpotNumber());
        }else{
            System.out.println("Sorry!,You can't park your vehicle here");
        }
    }
    public void unPark(){
        if(isOcupied()){
            occupied=false;
        }
    }
}
class BikeSpot extends ParkingSpot{
     private int spotNumber;
     public BikeSpot(int spotNumber){
         super(spotNumber,"Bike");
     }
     public boolean canParkVehicle(Vehicle v){
         return v.getType()=="Bike";
     }
}
class CarSpot extends ParkingSpot{
     private int spotNumber;
     public CarSpot(int spotNumber){
         super(spotNumber,"Car");
     }
     public boolean canParkVehicle(Vehicle v){
         return v.getType()=="Car";
     }
}
//-------------Vehicle and its factory
abstract class Vehicle{
    private String number;
    private String type;
    public Vehicle(String number,String type){
        this.number=number;
        this.type=type;
    }
    public String getType(){
        return type;
    }
    public String getPlateNumber(){
        return number;
    }
    abstract double getCost(int time);
}
class Car extends Vehicle{
    private static final double price=10.0;
    private String number;
    private String type;
    Car(String number){
        super(number,"Car");
    }
    public double getCost(int time){
        return price*time;
    }
}
class Bike extends Vehicle{
    private static final double price=5.0;
    private String number;
    private String type;
    Bike(String number){
        super(number,"Bike");
    }
    public double getCost(int time){
        return price*time;
    }
}
class VehicleFactory{
   public Vehicle createVehicle(String type,String number){
       if(type=="Car"){
           return new Car(number);
       }else{
           return new Bike(number);
       }
   }
}
class Main {
    public static void main(String[] args) {
        ParkingLot pl=new ParkingLot(); 
        
        ParkingFloor floor1=new ParkingFloor(1);
        ParkingFloor floor2=new ParkingFloor(2);
        
        
        pl.addParkingFloor(floor1);
        pl.addParkingFloor(floor2);
        
        ParkingSpot a1=new CarSpot(1);
        ParkingSpot a2=new CarSpot(2);
        ParkingSpot a3=new BikeSpot(3);
        ParkingSpot a4=new BikeSpot(4);
       
        ParkingSpot b1=new CarSpot(5);
        ParkingSpot b2=new CarSpot(6);
        
        floor1.addParkingSpot(a1);
        floor1.addParkingSpot(a2);
        floor1.addParkingSpot(a3);
        floor1.addParkingSpot(a4);
        
        floor2.addParkingSpot(b1);
        floor2.addParkingSpot(b2);
        
        
        VehicleFactory vf=new VehicleFactory();
        Vehicle c1=vf.createVehicle("Car","soum");
        Vehicle c2=vf.createVehicle("Car","Aayu");
        Vehicle c3=vf.createVehicle("Bike","Boba");
        Vehicle c4=vf.createVehicle("Bike","Kri");
         Vehicle c5=vf.createVehicle("Car","Ashu");
        
        // Create Spots
        
        Entrance entrance=new Entrance(pl,1);
        Ticket t1=entrance.bookTicket(c1);
        Ticket t2=entrance.bookTicket(c2);
        Ticket t3=entrance.bookTicket(c3);
        Ticket t4=entrance.bookTicket(c4);
        // pl.parkVehicle(c1);
        // pl.parkVehicle(c2);
        // pl.parkVehicle(c3);
        // pl.parkVehicle(c4);
        
        PaymentStrategy paymentStrategy=new PaymentStrategy();
        paymentStrategy.setPaymentStrategy(new UPI("1234","Sou@345"));
        Exit exit=new Exit(paymentStrategy);
        exit.processExit(t4,6);
        
        ParkingSpot ps=pl.findAvailableSpots(c5);
        System.out.println(ps.getSpotNumber());
        
        
    }
}
