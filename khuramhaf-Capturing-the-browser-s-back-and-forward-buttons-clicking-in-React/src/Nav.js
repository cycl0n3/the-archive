


import './App.css';

var x = 1;
function Nav(props) {



function myfun1(e){
e.preventDefault();
setTimeout(()=>{
    props.setroutestate(1)
props.sethomestate("navstart");

window.history.pushState(x, null, '/')
x=x+1

window.pstate = x+2;

localStorage.setItem("previousstate", window.pstate);
}, 1000)

var array1= [false, "navendfull", "navendfull", "navendfull"]
props.setstate(array1);
}


function myfun2(e){
    e.preventDefault();

    setTimeout(()=>{
        props.setroutestate(2)
        props.setaboutstate("navstart");
        window.history.pushState(x, null, 'about')
x=x+1

window.pstate = x+2;
localStorage.setItem("previousstate", window.pstate);
    }, 1000)

    var array1= ["navendfull", false, "navendfull", "navendfull"]
    props.setstate(array1);
    
}


function myfun3(e){
    e.preventDefault();
    setTimeout(()=>{
        props.setroutestate(3)
        props.setservicesstate("navstart");
        window.history.pushState(x, null, '/services')
x=x+1
window.pstate = x+2;
localStorage.setItem("previousstate", window.pstate);
    }, 1000)
    
    var array1= ["navendfull", "navendfull", false, "navendfull"]
    props.setstate(array1);
}


function myfun4(e){
    e.preventDefault();
    setTimeout(()=>{
        props.setroutestate(4)
    props.setcontactstate("navstart");
    window.history.pushState(x, null, '/contact')
x=x+1
window.pstate = x+2;
localStorage.setItem("previousstate", window.pstate);
}, 1000)

var array1= ["navendfull", "navendfull", "navendfull", false]
props.setstate(array1);
}

    return ( 

        <div>

            <a onClick={myfun1} className='navbutton' href="">Home</a>
            <a onClick={myfun2} className='navbutton'  href="">About</a>
            <a onClick={myfun3} className='navbutton'  href="">Services</a>
            <a onClick={myfun4} className='navbutton'  href="">Contact</a>
        </div>
     );
}

export default Nav;