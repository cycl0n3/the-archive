import React, { useState } from "react";
import { useQuery } from "react-query";

import axios from "axios";

const fetchSuperHeroes = () => {
  return axios.get("http://localhost:4000/superheroes");
};

export const useSuperHeroesData = (onSuccessfulFetch, onFailedFetch) => {
  return useQuery("superHeroes", fetchSuperHeroes, {
    onSuccess: onSuccessfulFetch,
    onError: onFailedFetch,
    select: (data) => {
      const superHeroes = data.data;
      return superHeroes;
    },
    staleTime: 60000,
  });
};
