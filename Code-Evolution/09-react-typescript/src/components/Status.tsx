import React from 'react'

type StatusProps = {
  status: 'loading' | 'success' | 'error',
}

const Status = (props: StatusProps) => {
  let message

  if(props.status === 'loading') {
    message = 'Loading ...'
  }

  if(props.status === 'success') {
    message = 'Data fetched successfully'
  }

  if(props.status === 'error') {
    message = 'Error fetching data'
  }

  return (
    <div>
      <h2>{message}</h2>
    </div>
  )
}

export default Status
