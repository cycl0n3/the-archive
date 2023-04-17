import React, { Component } from 'react'

class HeroErrorBoundary extends Component {

  constructor(props) {
    super(props)
  
    this.state = {
       hasError: false
    }
  }

  static getDerivedStateFromError(e) {
    return {
      hasError: true
    }
  }

  componentDidCatch(e, info) {
    console.log(e, info);
  }

  render() {
    if(this.state.hasError) {
      return <h1>Something went wrong</h1>;
    }

    return this.props.children;
  }
}

export default HeroErrorBoundary;
