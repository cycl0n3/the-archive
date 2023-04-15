package com.parking

import scala.collection.mutable.Map

class MallParkingLot extends ParkingLot {

  override val feeStructure: Map[Vehicle, Any] = Map[Vehicle, Any](
    Motorcycle -> 10,
    Scooter -> 10,
    
    Car -> 20,
    SUV -> 20,
    
    Bus -> 50,
    Truck -> 50,
  )

  override val parkingSpots: Map[Vehicle, Int] = Map[Vehicle, Int](
    TwoWheeler -> 100,
    ThreeWheeler -> 80,
    FourWheeler -> 10,
  )

  override def toString(): String = {
    s"MallParkingLot(${feeStructure}, ${parkingSpots})"
  }
}
