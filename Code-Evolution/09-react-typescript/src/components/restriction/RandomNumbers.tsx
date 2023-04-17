import React, { useEffect } from 'react'

type RandomNumberType = {
  value: number,
}

type PositiveNumber = RandomNumberType & {
  isPositive: boolean,
  isNegative?: never,
  isZero?: never,
}

type NegativeNumber = RandomNumberType & {
  isNegative: boolean,
  isPositive?: never,
  isZero?: never,
}

type ZeroNumber = RandomNumberType & {
  isZero: boolean,
  isPositive?: never,
  isNegative?: never,
}

type RandomNumbersProps = PositiveNumber | NegativeNumber | ZeroNumber

const RandomNumbers = (
  { value, isPositive, isNegative, isZero }: RandomNumbersProps
) => {
  return (
    <div>
      <h2>RandomNumbers</h2>
      {value} is {isPositive && 'positive'} {isNegative && 'negative'} {isZero && 'zero'}
    </div>
  )
}

export default RandomNumbers
