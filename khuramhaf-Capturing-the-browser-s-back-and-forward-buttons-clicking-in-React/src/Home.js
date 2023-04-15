import './App.css'



import React from 'react'
import {useState, useEffect} from 'react'


function Home(props) {

    useEffect(()=>{

        if(props.homestate==="popstatestart"){

            

            setTimeout(()=>{
                props.sethomestate("popstateend")
        
              }, 10) 
        }

        else{

  const timer =     setTimeout(()=>{

        if (props.homestate==="navstart"){
            props.sethomestate("navend")
           }
    
           else if(props.state[0]==='navendfull'){
    
            props.sethomestate("navendfull")
           }
    
    
           else{
        }

       

       }, 10)

       return ()=>{
        clearTimeout(timer)
       }
    }
    })



    return ( 

        <div className={props.homestate}>

<div className="home">

<h1 className = "h1" >We Design. We hello.</h1>


<div className="homediv">
<p className='homep'>

We are team of two talented developers with an aim to develop web user interfaces for future. We keep our eye on the evolving nature of the web development. We see frontend development specially single page app is the technology of the future and we can achieve many special things which were not possible before.
<br></br>
Contact us @ contact@musk-technology.com

</p>


</div>


</div>

</div>

     );
}

export default Home;