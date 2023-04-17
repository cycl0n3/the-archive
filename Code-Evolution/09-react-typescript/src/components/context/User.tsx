import React, {
  useContext
} from 'react'

import { UserContext } from './UserContext'

const User = () => {
  const handleLogin = () => {
    userContext?.setUser({
      name: 'John',
      email: 'john@gmail.com'
    })
  }

  const handleLogout = () => {
    userContext?.setUser(undefined)
  }

  const userContext = useContext(UserContext)

  return (
    <div>
      {
        (userContext && userContext.user) && <>
        <button type='button' onClick={handleLogout}>Logout</button>
        <div>User name is {userContext.user.name} and email is {userContext.user.email}</div>
        </>
      }

      {
        !(userContext && userContext.user) && <>
        <button type='button' onClick={handleLogin}>Login</button>
        </>
      }
    </div>
  )
}

export default User
