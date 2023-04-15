import logo from './logo.svg';
import './App.css';
import {Routes, Route, Link, NavLink} from 'react-router-dom'
import {useState, useEffect} from 'react'
import Nav from './Nav';
import Home from './Home';
import About from './About';
import Contact from './Contact';
import Services from './Services';


function App() {

  var array1= ["navstart", "navstart", "navstart", "navstart"]
const [navstate, setnavstate] = useState("navstart")
const [compstate, setcompstate]=useState(array1);

useEffect(()=>{

setnavstate("navend")
})

  return (
<div>
    <div className={navstate}>
<Nav state={compstate} setstate={setcompstate}/>
</div>
<Routes>
<Route path='/' element={<Home state={compstate} setstate={setcompstate}/>}></Route>
<Route path='/about' element={<About state={compstate} setstate={setcompstate}/>}></Route>
<Route path='/services' element={<Services state={compstate} setstate={setcompstate}/>}></Route>
<Route path='/contact' element={<Contact state={compstate} setstate={setcompstate}/>}></Route>

</Routes>
</div>

  );
}

export default App;
