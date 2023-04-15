package com.parking

import scala.collection.mutable.Map
import scala.annotation.tailrec

import org.joda.time._

abstract class ParkingLot {

  def feeStructure: Map[Vehicle, Any]

  def parkingSpots: Map[Vehicle, Int]

  private var dateTime: DateTime = new DateTime

  def setDateTime(pDateTime: DateTime): Unit = {
    dateTime = pDateTime
  }

  def park(vehicle: Vehicle): Ticket = {
    getParkingSpots(vehicle) match {
      case 0 => {
        val ticket = new Ticket(vehicle, s"No space available")
        ticket.valid = false
        ticket
      }
      case i: Int => {
        occupyParkingSpot(vehicle)
        new Ticket(
          vehicle, 
          s"${vehicle} parked at ${dateTime}. ${getParkingSpots(vehicle)} spots left."
        )
      }
    }
  }

  def unpark(ticket: Ticket): Ticket = {
    if(!ticket.valid) {
      return ticket
    }

    val currentDateTime = new DateTime()
    val duration = new Duration(currentDateTime, dateTime)
    val milliseconds = duration.getMillis()

    val seconds = (milliseconds / 1000) % 60
    val minutes = ((milliseconds / (1000*60)) % 60)
    var hours   = ((milliseconds / (1000*60*60)))

    //if(minutes > 0 || seconds > 0) hours += 1

    val fee = getFee(ticket.vehicle, hours)

    val rTicket = new Ticket(ticket.vehicle, s"Unparking ticket after ${hours} h: ${minutes} m")
    rTicket.fee = fee
    rTicket.exitDate = dateTime
    rTicket
  }

  def getFee(vehicle: Vehicle, hours: Long): Double = {
    val fs = feeStructure(vehicle)
    var fee = 0.0

    fs match {
      case x: Int => (hours * x).toDouble
      case z: List[_] => {
        
        @tailrec
        def _calculateFee[A](fee: Double, list: List[A]): Double = {
          list match {
            case Nil => fee
            case x :: y => {
              x match {
                case (p, q) => p match {
                  case (r, s) => {
                    val ri = r.asInstanceOf[Int]
                    val si = s.asInstanceOf[Int]
                    val qi = q.asInstanceOf[Int]

                    val delta = getHoursAndDelta(ri, hours)
                    val hour = delta._1

                    if(hour >= ri && hour < si) {
                      if(si == Int.MaxValue) {
                        _calculateFee(fee + delta._2 * qi, Nil)
                      } else {
                        _calculateFee(fee + qi, y)
                      }
                    } else {
                      _calculateFee(fee, y)
                    }
                  }
                }
              }
            }
          }
        }

        fee = fee + _calculateFee(0.0, z)

        fee
      }
    }
  }

  def getHoursAndDelta(startHours: Long, endHours: Long): (Long, Long) = {
    (startHours, endHours - startHours + 1)
  }

  private def getParkingSpots(vehicle: Vehicle): Int = {
    vehicle match {
      case v2: TwoWheeler => parkingSpots(TwoWheeler)
      case v3: ThreeWheeler => parkingSpots(ThreeWheeler)
      case v4: FourWheeler => parkingSpots(FourWheeler)
      case _ => 0
    }
  }

  private def occupyParkingSpot(vehicle: Vehicle): Unit = {
    vehicle match {
      case v2: TwoWheeler => parkingSpots(TwoWheeler) -= 1
      case v3: ThreeWheeler => parkingSpots(ThreeWheeler) -= 1
      case v4: FourWheeler => parkingSpots(FourWheeler) -= 1
      case _ =>
    }
  }

  private def freeParkingSpot(vehicle: Vehicle): Unit = {
    vehicle match {
      case v2: TwoWheeler => parkingSpots(TwoWheeler) += 1
      case v3: ThreeWheeler => parkingSpots(ThreeWheeler) += 1
      case v4: FourWheeler => parkingSpots(FourWheeler) += 1
      case _ =>
    }
  }
}
