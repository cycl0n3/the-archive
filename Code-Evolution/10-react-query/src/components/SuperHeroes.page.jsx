import { useState, useEffect } from "react";
import axios from "axios";

export const SuperHeroesPage = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [data, setData] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    axios
      .get("http://localhost:4000/superheroes")
      .then((res) => {
        setData(res.data);
        setIsLoading(false);
        setError(null);
      })
      .catch((err) => {
        setData([]);
        setIsLoading(false);
        setError(err);
      });
  }, []);

  if (isLoading) {
    return <h2>Loading...</h2>;
  }

  if(error) {
    return <h2>Error: {error.message}</h2>;
  }

  return (
    <>
      <h2>Super Heroes Page</h2>
      {data.map((hero) => {
        return <div key={hero.id}>{hero.name}</div>;
      })}
    </>
  );
};
