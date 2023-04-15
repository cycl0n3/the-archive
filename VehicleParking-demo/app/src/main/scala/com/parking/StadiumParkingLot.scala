package com.parking

import scala.collection.mutable.Map

import org.joda.time._

class StadiumParkingLot extends ParkingLot {
  override val feeStructure: Map[Vehicle, Any] = Map[Vehicle, Any](
    Motorcycle -> List(
      ((0, 4), 30),
      ((4, 12), 60),
      ((12, Int.MaxValue), 100),
    ),
    Scooter -> List(
      ((0, 4), 30),
      ((4, 12), 60),
      ((12, Int.MaxValue), 100),
    ),
    
    Car -> List(
      ((0, 4), 60),
      ((4, 12), 120),
      ((12, Int.MaxValue), 200),
    ),
    SUV -> List(
      ((0, 4), 60),
      ((4, 12), 120),
      ((12, Int.MaxValue), 200),
    )
  )

  override val parkingSpots: Map[Vehicle, Int] = Map[Vehicle, Int](
    TwoWheeler -> 1000,
    ThreeWheeler -> 1500,
    FourWheeler -> 0,
  )

  override def toString(): String = {
    s"StadiumParkingLot(${feeStructure}, ${parkingSpots})"
  }
}