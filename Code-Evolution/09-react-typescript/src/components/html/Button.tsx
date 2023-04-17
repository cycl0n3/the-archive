import React from 'react'

type ButtonProps = {
  variant: 'primary' | 'secondary',
  children: string,
} & Omit<React.ComponentProps<'button'>, 'children'>

const Button = ( { variant, children, ...rest }: ButtonProps ) => {
  return (
    <div>
      <button type='button' className={`btn btn-${variant}`} {...rest}>
        {children}
      </button>
    </div>
  )
}

export default Button
