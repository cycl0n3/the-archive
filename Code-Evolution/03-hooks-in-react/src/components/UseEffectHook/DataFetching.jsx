import React, {
  useState,
  useEffect
} from 'react';

import axios from 'axios';

const DataFetching = () => {
  const [id, setId] = useState(1);
  const [post, setPost] = useState({id: 0, title: ''});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  const [idFromButtonClick, setIdFromButtonClick] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      setError(false);
      setLoading(true);

      try {
        const response = await axios.get(`https://jsonplaceholder.typicode.com/posts/${idFromButtonClick}`);
        setPost(response.data);
        setLoading(false);
      } catch (error) {
        setError(true);
        setLoading(false);
      }
    };

    fetchData();
  }, [idFromButtonClick]);

  return (
    <div>
      <input type="text" value={id} onChange={e => setId(e.target.value)} />

      {loading && <p>Loading...</p>}
      {error && <p>Error :(</p>}
      <h2>
        {post.title}
      </h2>
      {/* <ul>
        {data.map(item => (
          <li key={item.id}>{item.title}</li>
        ))}
      </ul> */}
      <button type="button" onClick={() => setIdFromButtonClick(id)}>
        Search
      </button>
    </div>
  )
}

export default DataFetching;
