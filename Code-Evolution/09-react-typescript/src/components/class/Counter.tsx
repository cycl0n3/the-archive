import React, { Component } from 'react'

type CounterProps = {
  message: string
}

type CounterState = {
  count: number
}

export default class Counter extends Component<CounterProps, CounterState> {
  constructor(props: CounterProps) {
    super(props)
  
    this.state = {
       count: 0
    } as CounterState
  }

  handleClick = () => {
    this.setState((prevState) => ({count: prevState.count + 1})) 
  }

  render() {
    return (
      <div>
        <h2>Counter</h2>
        <button type='button' onClick={this.handleClick}>Increment</button>
        {this.props.message} {this.state.count}
      </div>
    )
  }
}
