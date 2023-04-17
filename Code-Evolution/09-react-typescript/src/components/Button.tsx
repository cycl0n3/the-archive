import React from 'react'

import { nanoid } from 'nanoid'

type ButtonProps = {
  handleClick: (e: React.MouseEvent<HTMLButtonElement>, id: String) => void
}

const Button = (props: ButtonProps) => {
  let id = nanoid(5)

  return (
    <div>
      <button type='button' onClick={e => props.handleClick(e, id)}>
        Click here
      </button>
    </div>
  )
}

export default Button
