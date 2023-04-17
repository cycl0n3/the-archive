import React from 'react'

import { PersonProps } from './Person.types'

const Person = ({ name }: PersonProps) => {
  return (
    <h2>
      {name.first} {name.last}
    </h2>
  )
}

export default Person
