import React, {
  createContext,
  useState
} from "react"

type AuthUser = {
  name: string,
  email: string
}

type UserContextType = {
  user: AuthUser | undefined,
  setUser: React.Dispatch<React.SetStateAction<AuthUser | undefined>>
}

export const UserContext = createContext<UserContextType | undefined>(undefined)

type UserContextProviderProps = {
  children: React.ReactNode
}

export const UserContextProvider = ({ children }: UserContextProviderProps) => {
  const [user, setUser] = useState<AuthUser>()

  return <UserContext.Provider value={{user, setUser}}>
    { children }
  </UserContext.Provider>
}
