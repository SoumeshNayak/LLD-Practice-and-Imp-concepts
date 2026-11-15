import java.util.*;
import java.time.*;


enum VehicleType {
  CAR, BIKE, SCOOTY, TRUCK
}
enum SpotType {
  CAR_SPOT, BIKE_SCOOTY_SPOT, TRUCK_SPOOT
}
enum Status {
  ACTIVE, PAID, LOST
}

enum PaymentStatus {
  SUCCESS, FAILED, PENDING
}

class Vehicle{
  private String noPlate;
  private VehicleType type;
  Vehicle(String noPlate,VehicleType type){
    this.noPlate=noPlate;
    this.type=type;
  }
  public String getNoPlate(){
    return this.noPlate;
  }
  public VehicleType getVehicleType(){
    return this.type;
  }
}

class ParkingSpot{
  private Long id;
  private SpotType spotType;
  private Vehicle vehicle;
  ParkingSpot(Long id, SpotType spotType){
    this.id=id;
    this.spotType=spotType;
  }
  public void  setVehicle(Vehicle vehicle){
    if(!isFree()){
      throw new IllegalStateException("Spot is not available");
    }
    System.out.println("Vehicle Parked successfully");
    this.vehicle=vehicle;
  }
  public void unPark(Vehicle vehicle){
      if(isFree()){
      throw new IllegalStateException("Spot is empty");
    }
    this.vehicle=null;
    System.out.println("Vehicle unParked successfully");
  } 
  public Vehicle getVehicle(){
    return this.vehicle;
  }
  public Long getId(){ return this.id;}
  public SpotType getSpotType(){ return this.spotType;}
  public boolean isFree(){ return this.vehicle==null;}
}

class Ticket{
  private Long ticketId;
  private LocalTime currTime;
  private Status status;
  private String noPlate;
  private Long spotId;
  private VehicleType VehicleType;
  private SpotType spotType;
  private ParkingSpot parkingSpot;
  Ticket(Long ticketId,Vehicle vehicle,ParkingSpot spot){
    this.ticketId=ticketId;
    this.currTime=LocalTime.now();

    this.status=Status.ACTIVE;
    this.noPlate=vehicle.getNoPlate();
    this.VehicleType=vehicle.getVehicleType();

    this.spotId=spot.getId();
    this.spotType=spot.getSpotType();
    this.parkingSpot=spot;
  }

  public ParkingSpot getParkingSpot() { return this.parkingSpot; }
  public Long getTicketId(){ return this.ticketId; }
  public LocalTime getCurrTime(){ return this.currTime; }
  public Status getStatus(){ return this.status; }
  public void setStatus(Status status){ this.status = status; }
  public String getNoPlate(){ return this.noPlate; }
  public Long getSpotId(){ return this.spotId; }
  public VehicleType getVehicleType(){ return this.VehicleType; }
  public SpotType getSpotType(){ return this.spotType; }
}

class PaymentReceipt {
  private String receiptId;
  private double amount;
  private PaymentStatus paymentStatus;

  PaymentReceipt(String receiptId, double amount, PaymentStatus paymentStatus) {
    this.receiptId = receiptId;
    this.amount = amount;
    this.paymentStatus = paymentStatus;
  }

  public String getReceiptId() { return this.receiptId; }
  public double getAmount() { return this.amount; }
  public PaymentStatus getPaymentStatus() { return this.paymentStatus; }
}
// There can be strategy for getting paking spot 
// strategy can be firstspot, distance from exit etc
interface ParkingSpotAssignmentStrategy {
  ParkingSpot findSpot(List<ParkingSpot> parkingSpots, Vehicle vehicle);
}
class FirstAvailableSpotStrategy implements ParkingSpotAssignmentStrategy {
  @Override
  public ParkingSpot findSpot(List<ParkingSpot> parkingSpots, Vehicle vehicle) {
    VehicleType vehicleType = vehicle.getVehicleType();
    for (ParkingSpot parkingSpot : parkingSpots) {
      boolean compatible =
        (vehicleType == VehicleType.CAR && parkingSpot.getSpotType() == SpotType.CAR_SPOT) ||
        ((vehicleType == VehicleType.BIKE || vehicleType == VehicleType.SCOOTY) &&
          parkingSpot.getSpotType() == SpotType.BIKE_SCOOTY_SPOT) ||
        (vehicleType == VehicleType.TRUCK && parkingSpot.getSpotType() == SpotType.TRUCK_SPOOT);

      if (compatible && parkingSpot.isFree()) {
        return parkingSpot;
      }
    }
    return null;
  }
}

class ParkingLot {
  private List<ParkingSpot> parkingSpots = new ArrayList<>();
  private ParkingSpotAssignmentStrategy assignmentStrategy =
    new FirstAvailableSpotStrategy();
  private long nextTicketId = 1;

  public void setAssignmentStrategy(ParkingSpotAssignmentStrategy assignmentStrategy) {
    this.assignmentStrategy = Objects.requireNonNull(assignmentStrategy);
  }

  public void addParkingSpot(ParkingSpot parkingSpot) {
    parkingSpots.add(parkingSpot);
  }

  public List<ParkingSpot> getAllParkingSpots() {
    return new ArrayList<>(parkingSpots);
  }

  public ParkingSpot getParkingSpotById(Long id) {
    for (ParkingSpot parkingSpot : parkingSpots) {
      if (parkingSpot.getId().equals(id)) {
        return parkingSpot;
      }
    }
    return null;
  }

  public Ticket assignParkingSpotToVehicle(Vehicle vehicle) {
    ParkingSpot parkingSpot = assignmentStrategy.findSpot(parkingSpots, vehicle);
    if (parkingSpot == null) {
      return null;
    }

    parkingSpot.setVehicle(vehicle);
    return new Ticket(nextTicketId++, vehicle, parkingSpot);
  }
}


class ParkingManager {
  private static final ParkingManager INSTANCE = new ParkingManager();

  private ParkingManager() {}

  public static ParkingManager getInstance() {
    return INSTANCE;
  }

  public Ticket assignParkingSpot(ParkingLot parkingLot, Vehicle vehicle) {
    return Objects.requireNonNull(parkingLot)
      .assignParkingSpotToVehicle(Objects.requireNonNull(vehicle));
  }

  public PaymentReceipt processPayment(Ticket ticket, LocalTime endTime) {
    Objects.requireNonNull(ticket, "ticket must not be null");
    Objects.requireNonNull(endTime, "endTime must not be null");
    if (ticket.getStatus() != Status.ACTIVE) {
      throw new IllegalStateException("Only active tickets can be paid");
    }

    Duration duration = Duration.between(ticket.getCurrTime(), endTime);
    if (duration.isNegative()) {
      duration = duration.plusDays(1);
    }
    long seconds = duration.getSeconds();
    long hours = seconds == 0 ? 0 : (seconds + 3599) / 3600;

    double hourlyRate;
    switch (ticket.getVehicleType()) {
      case CAR:
        hourlyRate = 100;
        break;
      case BIKE:
      case SCOOTY:
        hourlyRate = 50;
        break;
      case TRUCK:
        hourlyRate = 200;
        break;
      default:
        throw new IllegalArgumentException("Unsupported vehicle type");
    }

    PaymentReceipt receipt = new PaymentReceipt(
      UUID.randomUUID().toString(), hours * hourlyRate, PaymentStatus.SUCCESS);
    ParkingSpot spot = ticket.getParkingSpot();
    spot.unPark(spot.getVehicle());
    ticket.setStatus(Status.PAID);
    return receipt;
  }
}

public class Main {
    public static void main(String[] args) {
      System.out.println("Hello, World!");
    }
}
