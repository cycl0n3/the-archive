import React, { Component } from 'react';
import axios from 'axios';


class PostList extends Component {

  constructor(props) {
    super(props)
  
    this.state = {
       posts: []
    }
  }

  componentDidMount() {
    axios.get('https://jsonplaceholder.typicode.com/posts')
      .then(response => {
        this.setState({ posts: response.data})
      })
      .catch(err => console.error(err))
  }

  render() {
    const { posts } = this.state;

    return (
      <div>
        <h2>List of posts</h2>
        {posts.map(post => (
          <div key={post.id}>
            <h3>{post.title}</h3>
            <p>{post.body}</p>
          </div>
        ))}
      </div>
    )
  }
}

export default PostList;
