import React, { useState } from "react";

import "./App.css";

import Greet from "./components/Greet";
import Person from "./components/Person";
import PersonList from "./components/PersonList";
import Status from "./components/Status";
import Heading from "./components/Heading";
import Oscar from "./components/Oscar";
// import Button from './components/Button'
// import Input from './components/Input'
import Container from "./components/Container";
import LoggedIn from "./components/state/LoggedIn";
// import User from './components/state/User'
// import Counter from './components/state/Counter'
import { ThemeContextProvider } from "./components/context/ThemeContext";
import Box from "./components/context/Box";
// import User from './components/context/User'
import { UserContextProvider } from "./components/context/UserContext";
import DomRef from "./components/ref/DomRef";
import MutableRef from "./components/ref/MutableRef";
import Counter from "./components/class/Counter";
import Private from "./components/auth/Private";
import Profile from "./components/auth/Profile";
import List from "./components/generics/List";
import RandomNumbers from "./components/restriction/RandomNumbers";

document.title = "Typescript App";
import "react-toastify/dist/ReactToastify.css";
import Toast from "./components/templateliterals/Toast";
import Button from "./components/html/Button";
import Text from "./components/polymorphic/Text";

// const name = {
//   first: 'John',
//   last: 'Wick',
// }

// const names = [
//   {
//     first: 'Bruce',
//     last: 'Wayne',
//   },
//   {
//     first: 'Clark',
//     last: 'Kent',
//   },
//   {
//     first: 'Princess',
//     last: 'Diana',
//   }
// ]

function App() {
  return (
    <div className="App">
      {/* <Greet name='John' messageCount={25} isLoggedIn={false} /> */}

      {/* <Person name={name} /> */}

      {/* <PersonList names={names} /> */}

      {/* <Status status={'success'} /> */}

      {/* <Oscar>
        <Heading>
          Oscar goes to Leonardo De`Caprio.
        </Heading>
      </Oscar> */}

      {/* <Greet name={'John'} isLoggedIn={true}/> */}

      {/* <Button handleClick={(e, id) => {
        console.log('button clicked', id)
      }} /> */}

      {/* <Input value='Sample' handleChange={(e, id) => {
        console.log('text changed', id)
      }} /> */}

      {/* <Container styles={{border: '2px solid black', padding: '10px', display: 'block'}} /> */}

      {/* <LoggedIn /> */}

      {/* <User /> */}

      {/* <Counter /> */}

      {/* <ThemeContextProvider>
        <Box />
      </ThemeContextProvider> */}

      {/* <UserContextProvider>
        <User />
      </UserContextProvider> */}

      {/* <DomRef />1 */}

      {/* <MutableRef /> */}

      {/* <Counter message='The count value is:'/> */}

      {/* <Private isLoggedIn={true} Component={Profile} /> */}

      {/* <List 
        items={['Batman', 'Superman', 'Wonder Woman']} 
        onClick={(item) => console.log(item)} /> */}

      {/* <List 
        items={[0, 1, 2, 3, 4, 5]} 
        onClick={(item) => console.log(item)} /> */}

      {/* <List 
        items={[
          { id: 1, firstname: 'Bruce', lastname: 'Wayne' },
          { id: 2, firstname: 'Clark', lastname: 'Kent' },
          { id: 3, firstname: 'Princess', lastname: 'Diana' },
        ]} 
        onClick={(item) => console.log(item)} /> */}

      {/* <RandomNumbers value={10} isPositive /> */}

      {/* <Toast position='center' /> */}

      {/* <Button variant='primary' onClick={() => console.log('Button is clicked')}>
          Primary Button
        </Button> */}

      <Text as="h1" size="lg">
        Heading
      </Text>
      <Text as="div" size="md">
        Sub Heading
      </Text>
      <Text as="h2" size="sm">
        Paragraph
      </Text>
      <Text as="label" htmlFor="aaa">
        Label
      </Text>
    </div>
  );
}

export default App;
