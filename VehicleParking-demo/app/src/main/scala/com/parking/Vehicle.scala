package com.parking

trait Vehicle

class TwoWheeler  extends Vehicle
object TwoWheeler extends TwoWheeler

class ThreeWheeler  extends Vehicle
object ThreeWheeler extends ThreeWheeler

class FourWheeler  extends Vehicle
object FourWheeler extends FourWheeler

object Motorcycle extends TwoWheeler
object Scooter    extends TwoWheeler

object Car extends ThreeWheeler
object SUV extends ThreeWheeler

object Bus   extends FourWheeler
object Truck extends FourWheeler
