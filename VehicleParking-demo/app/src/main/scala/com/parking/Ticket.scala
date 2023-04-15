package com.parking

import org.joda.time._

class Ticket(pVehicle: Vehicle, pDescription: String) {

  private val _id = Ticket.getNextId()
  private var _valid = true
  private val _entryDate = new DateTime()
  private var _exitDate: DateTime = null

  private val _vehicle: Vehicle = pVehicle
  private val _description: String = pDescription

  private var _fee: Double = 0.0

  def id = _id
  def entryDate = _entryDate
  def vehicle = _vehicle
  def description = _description

  def valid = _valid
  def valid_=(pValid: Boolean): Unit = { _valid = pValid }

  def exitDate = _exitDate
  def exitDate_=(pExitDate: DateTime): Unit = { _exitDate = pExitDate }

  def fee = _fee
  def fee_=(pFee: Double): Unit = { _fee = pFee }

  override def toString(): String = {
    s"""Ticket(
      ${id}, 
      ${vehicle},
      ${valid},
      ${entryDate},
      ${exitDate},
      ${fee}, 
      ${description}
    )"""
  }
}

object Ticket {

  private var currentId: Int = 111

  def getNextId(): Int = {
    currentId = currentId + 1
    currentId
  }
}
