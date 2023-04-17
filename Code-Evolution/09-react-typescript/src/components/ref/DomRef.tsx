import React, {
  useRef,
  useEffect
} from 'react'

const DomRef = () => {
  const inputRef = useRef<HTMLInputElement>(null!)

  useEffect(() => {
    inputRef.current.focus()
  }, [])

  return (
    <div>
      <h2>Dom Ref</h2>

      <input type="text" ref={inputRef} placeholder='sample text field' />
    </div>
  )
}

export default DomRef
