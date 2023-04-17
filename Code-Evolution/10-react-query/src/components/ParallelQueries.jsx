import axios from "axios";
import React from "react";
import { useQuery } from "react-query";

const fetchSuperHeroes = () => {
  return axios.get("http://localhost:4000/superheroes");
}

const fetchFriends = () => {
  return axios.get("http://localhost:4000/friends");
}

const ParallelQueries = () => {
  const { data: superHeroes } = useQuery("superHeroes", fetchSuperHeroes);
  const { data: friends } = useQuery("friends", fetchFriends);

  console.log(superHeroes);
  console.log(friends);

  return <div>
    <h1>Parallel Queries</h1>
  </div>;
};

export default ParallelQueries;
