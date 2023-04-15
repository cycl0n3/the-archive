import logo from './logo.svg';
import './App.css';
import {useState, useEffect} from 'react'
import Nav from './Nav';
import Home from './Home';
import About from './About';
import Contact from './Contact';
import Services from './Services';
import Render from './Render';


function App() {

  var array1= ["navstart", "navstart", "navstart", "navstart"]
const [navstate, setnavstate] = useState("navstart")
const [compstate, setcompstate]=useState(array1);

const [homestate, sethomestate] = useState("navstart")
const [aboutstate, setaboutstate] = useState("navstart")
const [servicesstate, setservicesstate] = useState("navstart")
const [contactstate, setcontactstate] = useState("navstart")

const [routestate, setroutestate] = useState(1)


const [previousstate, setpreviousstate] = useState(0)

useEffect(()=>{

setnavstate("navend")
})



useEffect(()=>{



  window.addEventListener('popstate',(e)=>{
console.log("estate :"+e.state)
console.log(window.pstate)
if (window.location.pathname==='/'){
if (e.state<window.pstate){
  sethomestate("popstatestart");
}

else {
  sethomestate("navstart");
}
  var array2= [false, "navstart", "navstart", "navstart"]

  setcompstate(array2)

  setroutestate(1)

 window.pstate = e.state;
}

else if (window.location.pathname==='/about'){

  if (e.state<window.pstate){
    setaboutstate("popstatestart");
  }
  
  else {
    setaboutstate("navstart");
  }

  var array2= ["navstart", false, "navstart", "navstart"]

  setcompstate(array2)
  setroutestate(2)
  window.pstate = e.state;
}

else if (window.location.pathname==='/services'){

  if (e.state<window.pstate){
    setservicesstate("popstatestart");
  }
  
  else {
    setservicesstate("navstart");
  }

  var array2= ["navstart", "navstart", false, "navstart"]

  setcompstate(array2)
  setroutestate(3)

  window.pstate = e.state;
}

else if (window.location.pathname==='/contact'){

  if (e.state<window.pstate){
    setcontactstate("popstatestart");
  }
  
  else {
    setcontactstate("navstart");
  }

  var array2= ["navstart", "navstart", "navstart", false]

  setcompstate(array2)
  setroutestate(4)

  window.pstate = e.state;
}

else {

}
  }
  
  
  )
}, [])


useEffect(()=>{

  if(window.location.pathname === '/'){
    setroutestate(1)
  }

  else if (window.location.pathname === '/about'){
    setroutestate(2)
  }

  else if (window.location.pathname === '/services'){
    setroutestate(3)
  }

  else if (window.location.pathname === '/contact'){
    setroutestate(4)
  }

  else{

  }

  window.pstate = localStorage.getItem("previousstate")

  window.history.pushState(0, null, null)
}, [])

  return (
<div>
    <div className={navstate}>
<Nav previousstate = {setpreviousstate} setroutestate={setroutestate} servicesstate={servicesstate} setservicesstate={setservicesstate} contactstate={contactstate} setcontactstate={setcontactstate} aboutstate={aboutstate} setaboutstate={setaboutstate} homestate={homestate} sethomestate={sethomestate} state={compstate} setstate={setcompstate}/>


</div>
<Render compstate={compstate} setcompstate={setcompstate} routestate={routestate} servicesstate={servicesstate} setservicesstate={setservicesstate} contactstate={contactstate} setcontactstate={setcontactstate} aboutstate={aboutstate} setaboutstate={setaboutstate} homestate={homestate} sethomestate={sethomestate}/>

</div>

  );
}

export default App;
