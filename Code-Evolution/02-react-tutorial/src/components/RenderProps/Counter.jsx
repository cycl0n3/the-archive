import React, { Component } from 'react'

class Counter extends Component {
  constructor(props) {
    super(props)
  
    this.state = {
       count: 0
    }
  }
  
  increment = () => {
    this.setState(prevState => ({ count: prevState.count + 1 }))
  }

  render() {
    return (
      <div>
        {this.props.render(this.state.count, this.increment)}
      </div>
    )
  }
}

export default Counter;

{/*
<Counter render={(count, increment) => <ClickCounter2 count={count} increment={increment} />} />
<Counter render={(count, increment) => <HoverCounter2 count={count} increment={increment} />} />
 */}
 