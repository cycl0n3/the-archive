import React, { useState } from "react";
import { BrowserRouter, Routes, Route, Outlet, Link } from "react-router-dom";

import { QueryClient, QueryClientProvider } from "react-query";
import { ReactQueryDevtools } from "react-query/devtools";

import { HomePage } from "./components/Home.page";
import { RQSuperHeroesPage } from "./components/RQSuperHeroes.page";
import { SuperHeroesPage } from "./components/SuperHeroes.page";
import { NoMatch } from "./components/NoMatch.page";

import "./App.css";

import { RQSuperHeroPage } from "./components/RQSuperHero.page";
import ParallelQueries from "./components/ParallelQueries";

document.title = "React Query";

const queryClient = new QueryClient();

const Layout = () => {
  return (
    <>
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/super-heroes">Traditional Super Heroes</Link>
          </li>
          <li>
            <Link to="/rq-super-heroes">RQ Super Heroes</Link>
          </li>
          <li>
            <Link to="/rq-parallel-queries">RQ Parallel Queries</Link>
          </li>
        </ul>
      </nav>

      <Outlet />
    </>
  );
};

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<HomePage />} />
            <Route path="super-heroes" element={<SuperHeroesPage />} />
            <Route path="rq-super-heroes" element={<RQSuperHeroesPage />} />
            <Route path="rq-super-heroes/:heroId" element={<RQSuperHeroPage />} />
            <Route path="rq-parallel-queries" element={<ParallelQueries />} />
            <Route path="*" element={<NoMatch />} />
          </Route>
        </Routes>
      </BrowserRouter>
      <ReactQueryDevtools initialIsOpen={false} position="bottom-right" />
    </QueryClientProvider>
  );
}

export default App;
