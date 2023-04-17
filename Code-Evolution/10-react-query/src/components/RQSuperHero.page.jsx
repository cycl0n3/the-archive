import React from "react";
import { useQuery } from "react-query";
import { useParams } from "react-router-dom";

import { useSuperHeroData } from "../hooks/useSuperHeroData";

const onSuccessfulFetch = (data) => {
  console.log("onSuccessfulFetch", data);
};

const onFailedFetch = (error) => {
  console.log("onFailedFetch", error);
};

export const RQSuperHeroPage = () => {
  const { heroId } = useParams();

  const { isLoading, isError, error, isFetching, refetch, data } =
    useSuperHeroData({ heroId, onSuccessfulFetch, onFailedFetch });
  
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
      <h2>RQ Super Hero Page</h2>
      <p>{data.name}</p>
    </div>
  )
};
