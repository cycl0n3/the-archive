import { useState } from 'react'

import { Button } from './stories/Button/Button'

import './App.css'

document.title = 'Welcome to storyboard'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="App">
      <Button
        backgroundColor="#e01894"
        label={`count is ${count}`}
        onClick={() => setCount((count) => count + 1)}
        primary
        size="large"
      />
    </div>
  )
}

export default App
