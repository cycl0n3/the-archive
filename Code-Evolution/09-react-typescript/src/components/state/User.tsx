import React, { useState } from 'react'

type AuthUser = {
  name: string,
  email: string
}

const User = () => {
  const [user, setUser] = useState<AuthUser>()

  const handleLogin = () => {
    setUser({
      name: 'John',
      email: 'Wick'
    })
  }

  const handleLogout = () => {
    setUser(undefined)
  }

  return (
    <div>
      { !user && <>
        <button type='button' onClick={handleLogin}>Login</button>
        </>
      }
      
      { user && <>
        <button type='button' onClick={handleLogout}>Logout</button>
        <div>User name is - {user.name}</div>
        <div>User email is -{user.email}</div>
        </>
      }
    </div>
  )
}

export default User
