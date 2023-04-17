import React, { 
  createContext
} from "react";

const UserContext = createContext('John Connor');

const UserProvider = UserContext.Provider
const UserConsumer = UserContext.Consumer

export { UserProvider, UserConsumer };

export default UserContext;


{/* <UserProvider value={`John Wick`}>
  <ComponentC />
</UserProvider> */}
