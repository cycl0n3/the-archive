import React from 'react'

type GreetProps = {
  name: String,
  messageCount?: number,
  isLoggedIn: boolean,
}

const Greet = (props: GreetProps) => {
  const { messageCount = 0} = props

  return (
    <h2>
      {props.isLoggedIn ?
        <>
          <p>
            Welcome {props.name}. 
          </p>
          <p>
            You have {messageCount} new messages.
          </p> 
        </>: <p>Welcome Guest !</p> 
      }
      <hr />
    </h2>
  )
}

export default Greet
