import React, {
  useState,
  useRef,
  useEffect
} from 'react'

const MutableRef = () => {
  const [timer, setTimer] = useState(0)
  const intervalRef = useRef<number | undefined>(undefined)

  const stopTimer = () => {
    window.clearInterval(intervalRef.current)
  }

  useEffect(() => {
    intervalRef.current = window.setInterval(() => {
      setTimer((timer) => timer + 1)
    }, 1000)

    return () => {
      stopTimer()
    }
  }, [])

  return (
    <div>
      HookTimer - {timer} - 
      <button type='button' onClick={() => stopTimer()}>Stop Timer</button>
    </div>
  )
}

export default MutableRef
