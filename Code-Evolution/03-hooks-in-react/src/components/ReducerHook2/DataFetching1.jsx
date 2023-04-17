import React, {
  useState, 
  useEffect
} from 'react'

import axios from 'axios'

const DataFetching1 = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [post, setPost] = useState({});

  useEffect(() => {
    axios.get('https://jsonplaceholder.typicode.com/posts/10')
      .then((response) => {
        setLoading(false);
        setPost(response.data);
        setError('');
      }).catch((error) => {
        setLoading(false);
        setPost({});
        setError('Something went wrong');
      })
  }, [])

  return (
    <div>
      { loading ? 'Loading' : post.title }
      { error ? error : null }
    </div>
  )
}

export default DataFetching1;
