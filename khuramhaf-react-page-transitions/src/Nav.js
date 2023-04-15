
import { useNavigate } from 'react-router-dom';


import './App.css';


function Nav(props) {

  const  navigate=useNavigate();

function myfun1(e){
e.preventDefault();
setTimeout(()=>{
    navigate('/')
    var array1= ["navstart", "navstart", "navstart", "navstart"]
props.setstate(array1);
}, 1000)

var array1= ["navendfull", "navendfull", "navendfull", "navendfull"]
props.setstate(array1);
}


function myfun2(e){
    e.preventDefault();

    setTimeout(()=>{
        navigate('/about')
        var array1= ["navstart", "navstart", "navstart", "navstart"]
    props.setstate(array1);
    }, 1000)

    var array1= ["navendfull", "navendfull", "navendfull", "navendfull"]
    props.setstate(array1);
    
}


function myfun3(e){
    e.preventDefault();
    setTimeout(()=>{
        navigate('/services')
        var array1= ["navstart", "navstart", "navstart", "navstart"]
    props.setstate(array1);
    }, 1000)
    
    var array1= ["navendfull", "navendfull", "navendfull", "navendfull"]
    props.setstate(array1);
}


function myfun4(e){
    e.preventDefault();
    setTimeout(()=>{
    navigate('/contact')
    var array1= ["navstart", "navstart", "navstart", "navstart"]
props.setstate(array1);
}, 1000)

var array1= ["navendfull", "navendfull", "navendfull", "navendfull"]
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