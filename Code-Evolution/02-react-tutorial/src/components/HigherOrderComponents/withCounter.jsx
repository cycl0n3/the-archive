import React from "react";

const withCounter = (WrappedComponent, inc) => {
  class WithCounter extends React.Component {
    constructor(props) {
      super(props)
    
      this.state = {
        count: 0
      }
    }
  
    increment = () => {
      this.setState((prevState) => {
        return {
          count: prevState.count + inc
        }
      })
    }

    render() {
      return (
        <WrappedComponent count={this.state.count} increment={this.increment} {...this.props} />
      )
    }
  }

  return WithCounter;
}

export default withCounter;
