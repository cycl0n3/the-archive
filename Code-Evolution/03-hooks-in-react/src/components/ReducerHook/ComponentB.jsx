import React from 'react'

import ComponentD from './ComponentD';

const ComponentB = () => {
  return (
    <div className='div'>
      Component B
      <ComponentD />
    </div>
  )
}

export default ComponentB;
