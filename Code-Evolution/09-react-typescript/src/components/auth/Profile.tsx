import React from 'react'

export type ProfileProps = {
  name: string,
}

const Profile = ({ name }: ProfileProps) => {
  return (
    <div>Profile of user {name}</div>
  )
}

export default Profile
