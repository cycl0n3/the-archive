import { BUY_CAKE } from "./cakeTypes"

export const buyCake = (n = 1) => {
  return {
    type: BUY_CAKE,
    payload: n,
  }
}
