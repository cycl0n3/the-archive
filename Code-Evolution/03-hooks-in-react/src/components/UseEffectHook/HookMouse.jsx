import React,{
  useState,
  useEffect
} from 'react'

const HookMouse = () => {
  const [x, setX] = useState(0)
  const [y, setY] = useState(0)

  const logMousePosition = (e) => {
    console.log("Mouse event: " + e);

    setX(e.clientX)
    setY(e.clientY)
  }

  useEffect(() => {
    console.log('useEffect called');

    console.log('adding mousemove event');
    window.addEventListener('mousemove', logMousePosition)
    
    return () => {
      console.log('removing mousemove event');
      window.removeEventListener('mousemove', logMousePosition)
    }
  }, [])

  return (
    <div>
      <h3>Mouse Hooks</h3>
      <p>X: {x}</p>
      <p>Y: {y}</p>
    </div>
  )
}

export default HookMouse;
