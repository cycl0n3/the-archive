import React, { useState } from "react";
import { useQuery } from "react-query";

import axios from "axios";

const fetchSuperHero = (id) => {
  return axios.get("http://localhost:4000/superheroes/" + id);
};

export const useSuperHeroData = ({heroId, onSuccessfulFetch, onFailedFetch}) => {
  return useQuery(["superHero", heroId], () => fetchSuperHero(heroId), {
    onSuccess: onSuccessfulFetch,
    onError: onFailedFetch,
    select: (data) => {
      const superHero = data.data;
      return superHero;
    }
  })
}
