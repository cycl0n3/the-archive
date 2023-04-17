import { useState } from 'react'

import './App.css'

import IconExample from './components/IconExample'
import ToastExample from './components/ToastExample'
import ModalExample from './components/ModalExample'
import ToolTips from './components/ToolTips'
import CountUpEx from './components/CountUpEx'

document.title = 'Practical Application'

function App() {

  return (
    <div className="App">
      {/* <IconExample /> */}

      {/* <ToastExample /> */}
      
      {/* <ModalExample /> */}

      {/* <ToolTips /> */}

      <CountUpEx />
    </div>
  )
}

export default App
