import React, { Component } from 'react'

class ClickCounter2 extends Component {

  render() {
    const { count, increment } = this.props;

    return (
      <div>
        <button onClick={increment}>
          Clicked {count} times
        </button>
      </div>
    )
  }
}

export default ClickCounter2;
