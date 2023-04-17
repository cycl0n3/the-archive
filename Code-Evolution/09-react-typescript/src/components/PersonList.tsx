import React from 'react'

import Person from './Person'
import { Name } from './Person.types'

type PersonListProps = {
  names: Name[]
}

const PersonList = (props: PersonListProps) => {
  return (
    <div>
      {props.names.map((name) => <Person name={name} />)}
    </div>
  )
}

export default PersonList
