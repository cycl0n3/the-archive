import React, { useState } from "react";
import { useQuery } from "react-query";

import { nanoid } from "nanoid";
import { useSuperHeroesData } from "../hooks/useSuperHeroesData";

import { Link } from "react-router-dom";


const onSuccessfulFetch = (data) => {
  console.log("onSuccessfulFetch", data);
};

const onFailedFetch = (error) => {
  console.log("onFailedFetch", error);
};

export const RQSuperHeroesPage = () => {
  const { isLoading, isError, error, isFetching, refetch, data } =
    useSuperHeroesData(onSuccessfulFetch, onFailedFetch);

  if (isLoading) {
    return <h2>Loading...</h2>;
  }

  if (isFetching) {
    return <h2>Updating...</h2>;
  }

  if (isError) {
    return <h2>Error: {error.message}</h2>;
  }

  return (
    <div>
      <h2>RQ Super Heroes Page</h2>

      {data.map((hero) => {
        return <div key={nanoid(5)}>
          <p>
            <Link to={`/rq-super-heroes/${hero.id}`}>{hero.name}</Link>
          </p>
        </div>;
      })}
    </div>
  );
};
