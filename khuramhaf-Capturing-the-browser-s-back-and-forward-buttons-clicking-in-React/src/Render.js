import Home from './Home';
import About from './About';
import Contact from './Contact';
import Services from './Services';




function Render(props) {
    if (props.routestate === 1){
         return <Home homestate={props.homestate} sethomestate={props.sethomestate} state={props.compstate} setstate={props.setcompstate}/>
        }
        else if (props.routestate===2){
       return <About aboutstate={props.aboutstate} setaboutstate={props.setaboutstate} state={props.compstate} setstate={props.setcompstate}/>
        }
        else if (props.routestate===3){
       return <Services servicesstate={props.servicesstate} setservicesstate={props.setservicesstate} state={props.compstate} setstate={props.setcompstate}/>
        }
        else if (props.routestate===4){
      return  <Contact contactstate={props.contactstate} setcontactstate={props.setcontactstate} state={props.compstate} setstate={props.setcompstate}/>
        }
        
        else {
        
        
        }
}

export default Render ;