/*
const Layout = () => {
  return (
    <>
      <ThemeProvider theme={darkTheme}>
        <CssBaseline />
        <nav>
          <ul>
            <li>
              <RouteLink to="/">Home</RouteLink>
            </li>
            <li>
              <RouteLink to="/about">About</RouteLink>
            </li>
            <li>
              <RouteLink to="/auth/sign-in">Sign In</RouteLink>
            </li>
            <li>
              <RouteLink to="/auth/sign-up">Sign Up</RouteLink>
            </li>
            <li>
              <RouteLink to="/album">Album</RouteLink>
            </li>
            <li>
              <RouteLink to="/app-bar">app bar</RouteLink>
            </li>
            <li>
              <RouteLink to="/other">404</RouteLink>
            </li>
          </ul>
        </nav>

        <Outlet />
      </ThemeProvider>
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
            <Route path="about" element={<AboutPage />} />
            <Route path="auth/sign-in" element={<SignIn />} />
            <Route path="auth/sign-up" element={<SignUp />} />
            <Route path="album" element={<Album />} />
            <Route path="app-bar" element={<ResponsiveAppBar />} />
            <Route path="*" element={<div>404</div>} />
          </Route>
        </Routes>
      </BrowserRouter>
      <ReactQueryDevtools initialIsOpen={false} position="bottom-right" />
    </QueryClientProvider>
  );
}

*/

export default function Base() {
  return (
    <div>
      <h1>Base</h1>
    </div>
  );
}
