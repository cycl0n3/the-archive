import React, {
  useReducer
} from 'react'

type CounterState = {
  count: number
}

type UpdateAction = {
  type: 'increment' | 'decrement'
  payload: number
}

type ResetAction = {
  type: 'reset'
}

type CounterAction = UpdateAction | ResetAction

const initialState: CounterState = {
  count: 0
}

const reducer = (state: CounterState, action: CounterAction) => {
  switch (action.type) {
    case 'increment':
      return {
        ...state,
        count: state.count + action.payload
      } as CounterState

    case 'decrement':
      return {
        ...state,
        count: state.count - action.payload
      } as CounterState
    case 'reset':
      return initialState
    default:
      return state
  }
}

const Counter = () => {
  const [state, dispatch] = useReducer(reducer, initialState)

  return (
    <div>
      Count: {state.count}
      <p>
        <button type='button' onClick={() => dispatch({ type: 'increment', payload: 5 })}>Increment 5</button>
        <button type='button' onClick={() => dispatch({ type: 'decrement', payload: 5 })}>Decrement 5</button>
        <button type='button' onClick={() => dispatch({ type: 'reset' })}>Reset</button>
      </p>
    </div>
  )
}

export default Counter
