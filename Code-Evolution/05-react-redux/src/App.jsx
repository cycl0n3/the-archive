import React, { useState } from 'react'
import { Provider } from 'react-redux'

import store from './redux/store'

import './App.css'

import CakeContainer from './components/CakeContainer'
import IceCreamContainer from './components/IceCreamContainer'
import CakeContainerCustom from './components/CakeContainerCustom'
import ItemContainer from './components/ItemContainer'
import UserContainer from './components/UserContainer'

function App() {

  return (
    <div className="App">
      <Provider store={store}>
        {/* <CakeContainer /> */}
        {/* <IceCreamContainer /> */}
        {/* <CakeContainerCustom /> */}
        {/* <ItemContainer cake /> */}
        {/* <ItemContainer /> */}
        <UserContainer />
      </Provider>
    </div>
  )
}

export default App
