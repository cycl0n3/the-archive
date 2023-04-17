import axios from 'axios'
import React, { Component } from 'react'

class PostForm extends Component {
  constructor(props) {
    super(props)
  
    this.state = {
       userId: '',
       title: '',
       body: ''
    }
  }

  handleChange = event => {
    this.setState({
      [event.target.name]: event.target.value
    })
  }

  handleSubmit = (e) => {
    e.preventDefault()
    axios.post('https://jsonplaceholder.typicode.com/posts', this.state)
      .then((response) => {
        console.log(response)
      })
      .catch((error) => console.log(error))
  }

  render() {
    const { userId, title, body } = this.props;

    return (
      <div>
        <form onSubmit={this.handleSubmit}>
          <div>
            User Id:<input type="text" name="userId" value={userId} onChange={this.handleChange} />
          </div>
          <div>
            Title:<input type="text" name="title" value={title} onChange={this.handleChange} />
          </div>
          <div>
            Body:<input type="text" name="body" body={body} onChange={this.handleChange} />
          </div>
          <div>
            <button>Submit</button>
          </div>
        </form>
      </div>
    )
  }
}

export default PostForm;
