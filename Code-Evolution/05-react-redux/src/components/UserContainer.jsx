import React, {
  useEffect
} from 'react'

import { connect } from 'react-redux'

import { fetchUsers } from '../redux'

const UserContainer = ({ userData, fetchUsers }) => {
  useEffect(() => {
    fetchUsers()
  }, [])

  return userData.loading ? (
    <h2>Loading</h2>
  ) : userData.error ? (
    <h2>{userData.error}</h2>
  ) : (
    <div>
    <h2>User List</h2>
      {userData && 
        userData.users && 
          userData.users.map(user => (
            <p key={user.id}>#{user.id} {user.name}</p>
          ))}
    </div>
  )
}

const mapStateToProps = (state) => {
  return {
    userData: state.users
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    fetchUsers: () => dispatch(fetchUsers())
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( UserContainer )
