package com.parking

import scala.collection.mutable.Map

import org.joda.time._

object App {

  def main(args: Array[String]): Unit = {
    //mallParkingSimulation()
    //smallParkingSimulation()
    stadiumParkingSimulation()
    //airportParkingSimulation()
  }

  def mallParkingSimulation(): Unit = {
    var dateTime = new DateTime()
    val parking: ParkingLot = new MallParkingLot

    // First Ticket
    var ticket1 = parking.park(Motorcycle)
    println(ticket1)

    dateTime = dateTime.plusHours(3)
    dateTime = dateTime.plusMinutes(30)
    parking.setDateTime(dateTime)

    var ticket1_r = parking.unpark(ticket1)
    println(ticket1_r)

    // Second Ticket
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket2 = parking.park(Car)
    println(ticket2)

    dateTime = dateTime.plusHours(6)
    dateTime = dateTime.plusMinutes(1)
    parking.setDateTime(dateTime)

    var ticket2_r = parking.unpark(ticket2)
    println(ticket2_r)

    // Third Ticket
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket3 = parking.park(Truck)
    println(ticket3)

    dateTime = dateTime.plusHours(1)
    dateTime = dateTime.plusMinutes(59)
    parking.setDateTime(dateTime)

    var ticket3_r = parking.unpark(ticket3)
    println(ticket3_r)
  }

  def smallParkingSimulation(): Unit = {
    var dateTime = new DateTime()
    val parking: ParkingLot = new SmallParkingLot

    // First Ticket
    var ticket1 = parking.park(Motorcycle)
    println(ticket1)

    dateTime = dateTime.plusHours(3)
    dateTime = dateTime.plusMinutes(30)
    parking.setDateTime(dateTime)

    var ticket1_r = parking.unpark(ticket1)
    println(ticket1_r)

    // Second Ticket
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket2 = parking.park(Scooter)
    println(ticket2)

    dateTime = dateTime.plusHours(6)
    dateTime = dateTime.plusMinutes(1)
    parking.setDateTime(dateTime)

    var ticket2_r = parking.unpark(ticket2)
    println(ticket2_r)
  }

  def stadiumParkingSimulation(): Unit = {
    var dateTime = new DateTime();
    val parking: ParkingLot = new StadiumParkingLot

    // First Ticket
    var ticket1 = parking.park(Motorcycle)
    println(ticket1)

    dateTime = dateTime.plusHours(3)
    dateTime = dateTime.plusMinutes(40)
    parking.setDateTime(dateTime)

    var ticket1_r = parking.unpark(ticket1)
    println(ticket1_r)

    // Second Ticket
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket2 = parking.park(Scooter)
    println(ticket2)

    dateTime = dateTime.plusHours(14)
    dateTime = dateTime.plusMinutes(59)
    parking.setDateTime(dateTime)

    var ticket2_r = parking.unpark(ticket2)
    println(ticket2_r)

    // #3
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket3 = parking.park(SUV)
    println(ticket3)

    dateTime = dateTime.plusHours(11)
    dateTime = dateTime.plusMinutes(30)
    parking.setDateTime(dateTime)

    var ticket3_r = parking.unpark(ticket3)
    println(ticket3_r)

    // #4
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket4 = parking.park(SUV)
    println(ticket4)

    dateTime = dateTime.plusHours(13)
    dateTime = dateTime.plusMinutes(5)
    parking.setDateTime(dateTime)

    var ticket4_r = parking.unpark(ticket4)
    println(ticket4_r)
  }

  def airportParkingSimulation(): Unit = {
    var dateTime = new DateTime();
    val parking: ParkingLot = new AirportParkingLot

    // # 1
    var ticket1 = parking.park(Motorcycle)
    println(ticket1)

    dateTime = dateTime.plusHours(0)
    dateTime = dateTime.plusMinutes(55)
    parking.setDateTime(dateTime)

    var ticket1_r = parking.unpark(ticket1)
    println(ticket1_r)

    // # 2
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket2 = parking.park(Motorcycle)
    println(ticket2)

    dateTime = dateTime.plusHours(14)
    dateTime = dateTime.plusMinutes(59)
    parking.setDateTime(dateTime)

    var ticket2_r = parking.unpark(ticket2)
    println(ticket2_r)

    // # 3
    dateTime = new DateTime()
    parking.setDateTime(dateTime)

    var ticket3 = parking.park(Motorcycle)
    println(ticket3)

    dateTime = dateTime.plusDays(1)
    dateTime = dateTime.plusHours(12)
    parking.setDateTime(dateTime)

    var ticket3_r = parking.unpark(ticket3)
    println(ticket3_r)
  }
}
