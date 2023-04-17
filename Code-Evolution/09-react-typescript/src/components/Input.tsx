import React from 'react'

import { nanoid } from 'nanoid'

type InputProps = {
  value: string,
  handleChange: (e: React.ChangeEvent<HTMLInputElement>, id: string) => void
}

const Input = ({value, handleChange}: InputProps) => {
  let id = nanoid(5)

  return (
    <div>
      <input type='text' value={value} 
        onChange={(e) => handleChange(e, id)} title='Sample' />
    </div>
  )
}

export default Input
