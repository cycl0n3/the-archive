import React, { Component } from 'react';

import { UserConsumer } from './UserContext';

class ComponentF extends Component {
  render() {
    return (
      <div>
        <UserConsumer>
          {username => (
            <span>Hello <b>{username}</b></span>
          )}
        </UserConsumer>
      </div>
    )
  }
}

export default ComponentF;
