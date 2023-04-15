package com.parking

import scala.collection.mutable.Map

import org.joda.time._

class AirportParkingLot extends ParkingLot {
  override val feeStructure: Map[Vehicle, Any] = Map[Vehicle, Any](
    Motorcycle -> List(
      ((0, 1), 0),
      ((1, 8), 40),
      ((8, 24), 60),
      ((24, Int.MaxValue), 80),
    ),
    Scooter -> List(
      ((0, 1), 0),
      ((1, 8), 40),
      ((8, 24), 60),
      ((24, Int.MaxValue), 80),
    ),
    
    Car -> List(
      ((0, 12), 60),
      ((12, 24), 80),
      ((24, Int.MaxValue), 100),
    ),
    SUV -> List(
      ((0, 12), 60),
      ((12, 24), 80),
      ((24, Int.MaxValue), 100),
    )
  )

  override def getHoursAndDelta(startHours: Long, endHours: Long): (Long, Long) = {
    val hour = endHours % 24
    val delta = endHours / 24
    (hour, delta)
  }

  override val parkingSpots: Map[Vehicle, Int] = Map[Vehicle, Int](
    TwoWheeler -> 200,
    ThreeWheeler -> 500,
    FourWheeler -> 100,
  )

  override def toString(): String = {
    s"AirportParkingLot(${feeStructure}, ${parkingSpots})"
  }
}