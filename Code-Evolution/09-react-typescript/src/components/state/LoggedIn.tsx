import React, {
  useState
} from 'react'

const LoggedIn = () => {
  const [isLoggedIn, setLoggedIn] = useState(false)

  const handleLogin = () => {
    setLoggedIn(true)
  }
  const handleLogout = () => {
    setLoggedIn(false)
  }


  return (
    <div>
      <button type='button' onClick={handleLogin}>Login</button>
      <button type='button' onClick={handleLogout}>Logout</button>
      <div>User is {isLoggedIn ? 'logged in' : 'logged out'} </div>
    </div>
  )
}

export default LoggedIn
