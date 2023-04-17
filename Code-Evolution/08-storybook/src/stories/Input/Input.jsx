import React from "react";

import './input.css'

export const Input = (props) => {
  const { size = 'medium', ...rest } = props

  return (
    <input className={`input ${size}`} {...rest} />
  )
}
